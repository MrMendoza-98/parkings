package com.api.parkings.indicators.service;

import java.util.List;

import com.api.parkings.indicators.dto.FirstTimeParkedVehicle;
import com.api.parkings.indicators.dto.ParkingEarnings;
import com.api.parkings.indicators.dto.ParkedVehicleSearch;
import com.api.parkings.indicators.dto.TopVehiclesResponse;

public interface IIndicatorsService {
    
    
    TopVehiclesResponse getTop10MostFrequentVehicles();
    TopVehiclesResponse getTop10VehiclesByParking(Integer parkingId);
    List<FirstTimeParkedVehicle> getFirstTimeParkedVehicles(Integer parkingId);
    ParkingEarnings getParkingEarnings(Integer parkingId);
    List<ParkedVehicleSearch> searchParkedVehiclesByPlate(String partialPlate);
    
}
