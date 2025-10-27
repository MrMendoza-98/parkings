package com.api.parkings.vehicles.service;

import com.api.parkings.vehicles.model.Vehicles;
import com.api.parkings.vehicles.dto.VehicleInfoResponseDTO;

public interface IVehiclesService {
    public Vehicles createVehicles(Vehicles vehicle);
    public boolean isValidPlateFormat(String licensePlate);
    public Vehicles getVehicleByPlate(String licensePlate);
    public VehiclesService.VehicleResult findOrCreateVehicle(String plate, String email, String ownerName);
    public VehicleInfoResponseDTO getVehiclesInfo(String plate);
}
