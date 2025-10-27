package com.api.parkings.vehicles.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.parkings.email.service.EmailService;
import com.api.parkings.parkings.model.Parkings;
import com.api.parkings.parkings.repository.ParkingsRepository;
import com.api.parkings.users.model.Users;
import com.api.parkings.users.service.UsersService;
import com.api.parkings.vehicles.model.ParkingRecords;
import com.api.parkings.vehicles.model.Vehicles;
import com.api.parkings.vehicles.repository.ParkingRecordsRepository;

@Service
public class ParkingRecordsService implements IParkingRecordsService {
    @Autowired
    private ParkingRecordsRepository parkingRecordsRepository;
    
    @Autowired
    private ParkingsRepository parkingsRepository;
    
    @Autowired
    private IVehiclesService vehiclesService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private UsersService usersService;
    
    @Override
    public Optional<Parkings> getParkingById(Integer parkingId) {
        return parkingsRepository.findById(parkingId);
    }
    
    private void validateParkingOwnership(Integer parkingId) {
        Users currentUser = usersService.getCurrentUser();
        Parkings parking = getParkingById(parkingId)
            .orElseThrow(() -> new IllegalArgumentException("El parqueadero con ID " + parkingId + " no existe"));
        
        if (!parking.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("No tiene permisos para acceder al parqueadero con ID " + parkingId);
        }
    }
    
    @Override
    public boolean hasAvailableCapacity(Parkings parking) {
        Long currentOccupancy = parkingRecordsRepository.countActiveVehiclesByParking(parking);
        return currentOccupancy < parking.getCapacity();
    }
    
    @Override
    public boolean isVehicleParkedByPlate(String plate) {
        return parkingRecordsRepository.findActiveRecordByPlate(plate.toUpperCase()).isPresent();
    }
    
    @Override
    public boolean isVehicleParkedInSpecificParking(String plate, Integer parkingId) {
        return parkingRecordsRepository.findActiveRecordByPlateAndParkingId(plate.toUpperCase(), parkingId).isPresent();
    }
    
    @Override
    public ParkingRecords registerVehicleEntry(ParkingRecords parkingRecord) {
        if (isVehicleParkedByPlate(parkingRecord.getVehicleId().getPlate())) {
            throw new IllegalArgumentException("El vehículo con placa " + parkingRecord.getVehicleId().getPlate() + " ya está parqueado");
        }
        
        Parkings parking = getParkingById(parkingRecord.getParkingId().getId())
            .orElseThrow(() -> new IllegalArgumentException("El parqueadero con ID " + parkingRecord.getParkingId().getId() + " no existe"));
        
        if (!hasAvailableCapacity(parking)) {
            Long currentOccupancy = parkingRecordsRepository.countActiveVehiclesByParking(parking);
            throw new IllegalArgumentException(
                String.format("El parqueadero '%s' está lleno. Capacidad máxima: %d, Ocupación actual: %d", 
                    parking.getName(), parking.getCapacity(), currentOccupancy)
            );
        }
        
        parkingRecord.setEntryTime(LocalDateTime.now());
        return parkingRecordsRepository.save(parkingRecord);
    }
    
    public ParkingRecords processVehicleEntry(String plate, Integer parkingId, String email, String ownerName) {
        validateParkingOwnership(parkingId);
        
        VehiclesService.VehicleResult vehicleResult = vehiclesService.findOrCreateVehicle(plate, email, ownerName);
        Vehicles vehicle = vehicleResult.getVehicle();
        Parkings parking = getParkingById(parkingId)
            .orElseThrow(() -> new IllegalArgumentException("El parqueadero con ID " + parkingId + " no existe"));
            
        ParkingRecords parkingRecord = new ParkingRecords();
        parkingRecord.setVehicleId(vehicle);
        parkingRecord.setParkingId(parking);
        
        ParkingRecords result = registerVehicleEntry(parkingRecord);
        
        if (!ownerName.trim().isEmpty() && !email.trim().isEmpty() && email.contains("@")) {
            emailService.sendNotification(result, "ENTRADA");
        }
        
        return result;
    }

    @Override
    public ParkingRecords processVehicleExit(String plate, Integer parkingId) {
        validateParkingOwnership(parkingId);
        
        ParkingRecords parkingRecord = parkingRecordsRepository.findActiveRecordByPlateAndParkingId(plate, parkingId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("No hay un registro activo para el vehículo con placa %s en el parqueadero con ID %d", plate, parkingId)
            ));

        LocalDateTime entryTime = parkingRecord.getEntryTime();
        LocalDateTime exitTime = LocalDateTime.now();
        java.time.Duration duration = java.time.Duration.between(entryTime, exitTime);
        
        double hours = Math.ceil(duration.toMinutes() / 60.0);
        Double pricePerHour = parkingRecord.getParkingId().getPricePerHour();
        Double totalPrice = hours * pricePerHour;

        parkingRecord.setExitTime(exitTime);
        parkingRecord.setTotalPrice(totalPrice);
        
        ParkingRecords result = parkingRecordsRepository.save(parkingRecord);
        
        if (!result.getVehicleId().getOwnerName().trim().isEmpty() && !result.getVehicleId().getEmail().trim().isEmpty()) {
            emailService.sendNotification(result, "SALIDA");
        }
        
        return result;
    }

    public List<ParkingRecords> listVehiclesInParking(Integer parkingId) {
        // Validar que el usuario tiene permisos para este parqueadero
        validateParkingOwnership(parkingId);
        
        getParkingById(parkingId)
            .orElseThrow(() -> new IllegalArgumentException("El parqueadero con ID " + parkingId + " no existe"));
        
        return parkingRecordsRepository.findActiveRecordsByParkingIdOrderedDesc(parkingId);
    }

}
