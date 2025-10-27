package com.api.parkings.vehicles.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.api.parkings.vehicles.dto.RegisterEntryRequestDTO;
import com.api.parkings.vehicles.dto.RegisterEntryResponseDTO;
import com.api.parkings.vehicles.dto.RegisterExitRequestDTO;
import com.api.parkings.vehicles.dto.RegisterExitResponseDTO;
import com.api.parkings.vehicles.model.ParkingRecords;
import com.api.parkings.vehicles.service.IParkingRecordsService;
import com.api.parkings.vehicles.service.IVehiclesService;
import com.api.parkings.vehicles.dto.VehicleInfoResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicles")
@Validated
public class VehiclesController {

    @Autowired
    private IParkingRecordsService parkingRecordsService;
    
    @Autowired
    private IVehiclesService vehiclesService;
    
    @PreAuthorize("hasRole('SOCIO')")
    @PostMapping("/register-entry")
    public ResponseEntity<RegisterEntryResponseDTO> registerEntry(@Valid @RequestBody RegisterEntryRequestDTO request) {
        
        ParkingRecords parkingRecord = parkingRecordsService.processVehicleEntry(
            request.getPlate(),
            request.getParkingId(), 
            request.getEmail(), 
            request.getOwnerName()
        );
        
        RegisterEntryResponseDTO.ParkingRecordInfoDTO parkingInfo = 
            new RegisterEntryResponseDTO.ParkingRecordInfoDTO(
                parkingRecord.getId(), 
                parkingRecord.getEntryTime(), 
                parkingRecord.getParkingId().getName(), 
                parkingRecord.getParkingId().getPricePerHour()
            );
            
        RegisterEntryResponseDTO.VehicleInfoDTO vehicleInfo = 
            new RegisterEntryResponseDTO.VehicleInfoDTO(
                parkingRecord.getVehicleId().getId(), 
                parkingRecord.getVehicleId().getPlate(), 
                parkingRecord.getVehicleId().getOwnerName(), 
                parkingRecord.getVehicleId().getCreatedAt()
            );

        String message = "Ingreso registrado exitosamente para vehículo " + parkingRecord.getVehicleId().getPlate();

        RegisterEntryResponseDTO response = new RegisterEntryResponseDTO(
            true, message, parkingInfo, vehicleInfo
        );
        
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('SOCIO')")
    @PostMapping("/register-exit")
    public ResponseEntity<RegisterExitResponseDTO> registerExit(@Valid @RequestBody RegisterExitRequestDTO request) {
        
        ParkingRecords parkingRecord = parkingRecordsService.processVehicleExit(
            request.getPlate(), 
            request.getParkingId()
        );
        
        RegisterExitResponseDTO response = new RegisterExitResponseDTO(
            true,
            "Salida registrada exitosamente",
            request.getPlate(),
            request.getParkingId(),
            parkingRecord.getExitTime(),
            parkingRecord.getTotalPrice()
        );
        
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SOCIO')")
    @GetMapping("parked/{parkingId}")
    public ResponseEntity<List<ParkingRecords>> listParkedVehicles(@PathVariable Integer parkingId) {
        return ResponseEntity.ok(parkingRecordsService.listVehiclesInParking(parkingId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SOCIO')")
    @GetMapping("info/{plate}")
    public ResponseEntity<VehicleInfoResponseDTO> getVehicleDetails(@PathVariable String plate) {
        VehicleInfoResponseDTO vehicleInfo = vehiclesService.getVehiclesInfo(plate);
        return ResponseEntity.ok(vehicleInfo);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SOCIO')")
    @GetMapping("validate/{plate}/{parkingId}")
    public ResponseEntity<Boolean> validateVehicle(@PathVariable String plate, @PathVariable Integer parkingId) {
        boolean isValid = parkingRecordsService.isVehicleParkedInSpecificParking(plate, parkingId);
        return ResponseEntity.ok(isValid);
    }
}