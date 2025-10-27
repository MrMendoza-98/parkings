package com.api.parkings.parkings.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.parkings.parkings.model.Parkings;
import com.api.parkings.parkings.repository.ParkingsRepository;
import com.api.parkings.vehicles.repository.ParkingRecordsRepository;

@Service
public class ParkingsService implements IParkingsService {
    @Autowired
    private ParkingsRepository parkingsRepository;
    
    @Autowired
    private ParkingRecordsRepository parkingRecordsRepository;

    @Override
    public List<Parkings> getAllParkings() {
        return parkingsRepository.findAll();
    }

    @Override
    public Parkings getParkingById(Integer id) {
        Parkings parking = parkingsRepository.findById(id).orElse(null);
        return parking;
    }

    @Override
    public Parkings createParking(Parkings parking) {
        return parkingsRepository.save(parking);
    }

    @Override
    public Parkings updateParking(Integer id, Parkings parking) {
        Parkings existingParking = parkingsRepository.findById(id).orElse(null);
        if (existingParking == null) {
            return null;
        }
        Long activeVehicles = parkingRecordsRepository.countActiveVehiclesByParking(existingParking);
        
        if (parking.getCapacity() < activeVehicles) {
            throw new IllegalArgumentException(
                String.format("No se puede reducir la capacidad a %d. Hay %d vehículos actualmente parqueados. " +
                            "La nueva capacidad debe ser mayor o igual a %d", 
                            parking.getCapacity(), activeVehicles, activeVehicles)
            );
        }
        
        existingParking.setName(parking.getName());
        existingParking.setCapacity(parking.getCapacity());
        existingParking.setPricePerHour(parking.getPricePerHour());
        existingParking.setOwner(parking.getOwner());
        
        return parkingsRepository.save(existingParking);
    }

    @Override
    public void deleteParking(Integer id) {
        Parkings existingParking = parkingsRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("El parqueadero con ID " + id + " no existe"));
        
        Long totalRecords = parkingRecordsRepository.countByParkingId_Id(id);
        if (totalRecords > 0) {
            throw new IllegalArgumentException(
                String.format("No se puede eliminar el parqueadero '%s'. Existen %d registros históricos. " +
                            "Un parqueadero con historial no puede ser eliminado por integridad de datos.", 
                            existingParking.getName(), totalRecords)
            );
        }
        
        parkingsRepository.deleteById(id);
    }
}
