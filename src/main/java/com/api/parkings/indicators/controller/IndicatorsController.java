package com.api.parkings.indicators.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkings.indicators.dto.FirstTimeParkedVehicle;
import com.api.parkings.indicators.dto.ParkedVehicleSearch;
import com.api.parkings.indicators.dto.ParkingEarnings;
import com.api.parkings.indicators.dto.TopVehiclesResponse;
import com.api.parkings.indicators.service.IIndicatorsService;

@RestController
@RequestMapping("/indicators")
public class IndicatorsController {
    
    @Autowired
    private IIndicatorsService indicatorsService;

    /**
     * Obtiene los 10 vehículos más frecuentes en todos los parqueaderos
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('SOCIO')")
    @GetMapping("/top-vehicles")
    public ResponseEntity<TopVehiclesResponse> getTop10MostFrequentVehicles() {
        TopVehiclesResponse result = indicatorsService.getTop10MostFrequentVehicles();
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene los 10 vehículos más frecuentes en un parqueadero específico
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('SOCIO')")
    @GetMapping("/top-vehicles/parking/{parkingId}")
    public ResponseEntity<TopVehiclesResponse> getTop10VehiclesByParking(@PathVariable Integer parkingId) {
        TopVehiclesResponse result = indicatorsService.getTop10VehiclesByParking(parkingId);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene vehículos parqueados por primera vez en un parqueadero
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('SOCIO')")
    @GetMapping("/first-time-vehicles/parking/{parkingId}")
    public ResponseEntity<List<FirstTimeParkedVehicle>> getFirstTimeParkedVehicles(@PathVariable Integer parkingId) {
        List<FirstTimeParkedVehicle> result = indicatorsService.getFirstTimeParkedVehicles(parkingId);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene las ganancias de un parqueadero por periodos
     */
    @PreAuthorize("hasRole('SOCIO')")
    @GetMapping("/earnings/{parkingId}")
    public ResponseEntity<ParkingEarnings> getParkingEarnings(@PathVariable Integer parkingId) {
        ParkingEarnings result = indicatorsService.getParkingEarnings(parkingId);
        return ResponseEntity.ok(result);
    }

    /**
     * Busca vehículos parqueados por fragmento de placa
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('SOCIO')")
    @GetMapping("/search-vehicles")
    public ResponseEntity<List<ParkedVehicleSearch>> searchParkedVehiclesByPlate(@RequestParam String partialPlate) {
        List<ParkedVehicleSearch> result = indicatorsService.searchParkedVehiclesByPlate(partialPlate);
        return ResponseEntity.ok(result);
    }
}
