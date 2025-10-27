package com.api.parkings.vehicles.service;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.parkings.vehicles.model.Vehicles;
import com.api.parkings.vehicles.dto.VehicleInfoResponseDTO;
import com.api.parkings.vehicles.model.ParkingRecords;
import com.api.parkings.vehicles.repository.VehiclesRepository;
import com.api.parkings.vehicles.repository.ParkingRecordsRepository;

@Service
public class VehiclesService implements IVehiclesService {
    @Autowired
    private VehiclesRepository vehiclesRepository;
    
    @Autowired
    private ParkingRecordsRepository parkingRecordsRepository;
    
    private static final String PLATE_PATTERN = "^[A-Z]{3}[0-9]{2}[0-9A-Z]$";
    private static final Pattern COMPILED_PATTERN = Pattern.compile(PLATE_PATTERN);
    
    @Override
    public boolean isValidPlateFormat(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return false;
        }
        return COMPILED_PATTERN.matcher(licensePlate.toUpperCase()).matches();
    }
    
    @Override
    public Vehicles getVehicleByPlate(String licensePlate) {
        if (!isValidPlateFormat(licensePlate)) {
            throw new IllegalArgumentException("Formato de placa inválido. Use formato ABC123 o ABC12A");
        }
        return vehiclesRepository.findByPlate(licensePlate.toUpperCase()).orElse(null);
    }

    @Override
    public Vehicles createVehicles(Vehicles vehicle){
        if (!isValidPlateFormat(vehicle.getPlate())) {
            throw new IllegalArgumentException("Formato de placa inválido. Use formato ABC123 o ABC12A");
        }
        
        String plateUpper = vehicle.getPlate().toUpperCase();
        
        if (vehiclesRepository.findByPlate(plateUpper).isPresent()) {
            throw new IllegalArgumentException("Ya existe un vehículo registrado con la placa: " + vehicle.getPlate());
        }
        
        vehicle.setPlate(plateUpper);
        return vehiclesRepository.save(vehicle);
    }
    
    public VehicleResult findOrCreateVehicle(String plate, String email, String ownerName) {
        if (!isValidPlateFormat(plate)) {
            throw new IllegalArgumentException("Formato de placa inválido. Use formato ABC123 o ABC12A");
        }
        
        Vehicles existingVehicle = getVehicleByPlate(plate);
        
        if (existingVehicle != null) {
            return new VehicleResult(existingVehicle, false);
        }
        
        Vehicles newVehicle = new Vehicles();
        newVehicle.setPlate(plate.toUpperCase());
        newVehicle.setEmail(email);
        newVehicle.setOwnerName(ownerName);
        
        Vehicles savedVehicle = vehiclesRepository.save(newVehicle);
        return new VehicleResult(savedVehicle, true);
    }
    
    public static class VehicleResult {
        private final Vehicles vehicle;
        private final boolean wasCreated;
        
        public VehicleResult(Vehicles vehicle, boolean wasCreated) {
            this.vehicle = vehicle;
            this.wasCreated = wasCreated;
        }
        
        public Vehicles getVehicle() { return vehicle; }
        public boolean wasCreated() { return wasCreated; }
    }

    public VehicleInfoResponseDTO getVehiclesInfo(String plate) {
        if (!isValidPlateFormat(plate)) {
            throw new IllegalArgumentException("Formato de placa inválido. Use formato ABC123 o ABC12A");
        }
        
        Vehicles vehicle = vehiclesRepository.findByPlate(plate.toUpperCase())
            .orElseThrow(() -> new IllegalArgumentException("Vehículo con placa " + plate + " no encontrado"));
        
        Optional<ParkingRecords> activeRecord = 
            parkingRecordsRepository.findActiveRecordByPlate(plate.toUpperCase());
        
        return new VehicleInfoResponseDTO(
            vehicle.getId(),
            vehicle.getPlate(),
            vehicle.getOwnerName(),
            vehicle.getEmail(),
            vehicle.getCreatedAt(),
            vehicle.getUpdatedAt(),
            activeRecord.isPresent(),
            activeRecord.map(record -> record.getParkingId().getId()).orElse(null),
            activeRecord.map(record -> record.getEntryTime()).orElse(null)
        );
    }
}
