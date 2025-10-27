package com.api.parkings.vehicles.service;

import java.util.Optional;

import com.api.parkings.parkings.model.Parkings;
import com.api.parkings.vehicles.model.ParkingRecords;

public interface IParkingRecordsService {
    public Optional<Parkings> getParkingById(Integer parkingId);
    public boolean hasAvailableCapacity(Parkings parking);
    public boolean isVehicleParkedByPlate(String plate);
    public boolean isVehicleParkedInSpecificParking(String plate, Integer parkingId);
    public ParkingRecords registerVehicleEntry(ParkingRecords parkingRecord);
    public ParkingRecords processVehicleEntry(String plate, Integer parkingId, String email, String ownerName);
    public ParkingRecords processVehicleExit(String plate, Integer parkingId);
    public java.util.List<ParkingRecords> listVehiclesInParking(Integer parkingId);
}
