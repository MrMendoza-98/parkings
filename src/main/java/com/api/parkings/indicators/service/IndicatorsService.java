package com.api.parkings.indicators.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.parkings.indicators.dto.FirstTimeParkedVehicle;
import com.api.parkings.indicators.dto.ParkingEarnings;
import com.api.parkings.indicators.dto.ParkedVehicleSearch;
import com.api.parkings.indicators.dto.TopVehiclesResponse;
import com.api.parkings.indicators.dto.TopVehiclesResponse.VehicleFrequency;
import com.api.parkings.indicators.dto.TopVehiclesResponse.VehicleSummary;
import com.api.parkings.vehicles.model.ParkingRecords;
import com.api.parkings.vehicles.repository.ParkingRecordsRepository;

@Service
public class IndicatorsService implements IIndicatorsService {

    @Autowired
    private ParkingRecordsRepository parkingRecordsRepository;

    @Override
    public TopVehiclesResponse getTop10MostFrequentVehicles() {
        List<ParkingRecords> allRecords = parkingRecordsRepository.findAll();
        
        List<VehicleFrequency> vehicleList = allRecords.stream()
            .collect(Collectors.groupingBy(
                record -> record.getVehicleId().getPlate() + "||" + record.getParkingId().getId(),
                Collectors.counting()
            ))
            .entrySet().stream()
            .map(entry -> {
                String[] parts = entry.getKey().split("\\|\\|");
                String plate = parts[0];
                Integer parkingId = Integer.valueOf(parts[1]);
                return new VehicleFrequency(plate, parkingId, entry.getValue().intValue());
            })
            .sorted((a, b) -> b.getCount().compareTo(a.getCount()))
            .limit(10)
            .collect(Collectors.toList());
        
        List<VehicleFrequency> top10 = vehicleList;
        
        VehicleSummary summary = calculateSummary(top10, true);
        
        return new TopVehiclesResponse(
            top10,
            top10.size(),
            summary,
            LocalDateTime.now()
        );
    }
 
    @Override
    public TopVehiclesResponse getTop10VehiclesByParking(Integer parkingId) {
        List<ParkingRecords> parkingRecords = parkingRecordsRepository.findByParkingId_Id(parkingId);
        
        
        List<VehicleFrequency> vehicleList = parkingRecords.stream()
            .collect(Collectors.groupingBy(
                record -> record.getVehicleId().getPlate(),
                Collectors.counting()
            )).entrySet().stream()
            .map(entry -> new VehicleFrequency(entry.getKey(), entry.getValue().intValue()))
            .sorted((a, b) -> b.getCount().compareTo(a.getCount()))
            .limit(10)
            .collect(Collectors.toList());
        
        VehicleSummary summary = calculateSummary(vehicleList, false);
        summary.setMostFrequentParkingId(parkingId);
        
        return new TopVehiclesResponse(
            vehicleList,
            vehicleList.size(),
            summary,
            LocalDateTime.now()
        );
    }

    @Override
    public List<FirstTimeParkedVehicle> getFirstTimeParkedVehicles(Integer parkingId) {
        
        List<ParkingRecords> currentRecords = parkingRecordsRepository
            .findByParkingId_IdAndExitTimeIsNull(parkingId);
        
        List<FirstTimeParkedVehicle> firstTimers = new ArrayList<>();
        
        for (ParkingRecords record : currentRecords) {
            String plate = record.getVehicleId().getPlate();
            
            Long totalRecords = parkingRecordsRepository
                .countByVehicleId_PlateAndParkingId_Id(plate, parkingId);
            
            if (totalRecords == 1) {
                firstTimers.add(new FirstTimeParkedVehicle(plate, record.getEntryTime()));
            }
        }
        
        return firstTimers;
    }
    
    @Override
    public ParkingEarnings getParkingEarnings(Integer parkingId) {
        LocalDateTime now = LocalDateTime.now();
        
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfWeek = now.toLocalDate()
            .minusDays(now.getDayOfWeek().getValue() - 1)
            .atStartOfDay();
        LocalDateTime startOfMonth = now.toLocalDate().withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfYear = now.toLocalDate().withDayOfYear(1).atStartOfDay();
        
        Double today = sumEarnings(parkingId, startOfDay, now);
        Double week = sumEarnings(parkingId, startOfWeek, now);
        Double month = sumEarnings(parkingId, startOfMonth, now);
        Double year = sumEarnings(parkingId, startOfYear, now);
        
        return new ParkingEarnings(today, week, month, year);
    }

    @Override
    public List<ParkedVehicleSearch> searchParkedVehiclesByPlate(String partialPlate) {
        List<ParkingRecords> records = parkingRecordsRepository
            .findByVehicleId_PlateContainingIgnoreCaseAndExitTimeIsNull(partialPlate);
        
        return records.stream()
            .map(record -> new ParkedVehicleSearch(
                record.getVehicleId().getPlate(),
                record.getEntryTime(),
                record.getParkingId().getId()
            ))
            .collect(Collectors.toList());
    }

    
    private Double sumEarnings(Integer parkingId, LocalDateTime start, LocalDateTime end) {
        List<ParkingRecords> records = parkingRecordsRepository
            .findByParkingId_IdAndExitTimeBetween(parkingId, start, end);
        
        return records.stream()
            .mapToDouble(record -> record.getTotalPrice() != null ? record.getTotalPrice() : 0.0)
            .sum();
    }

    private VehicleSummary calculateSummary(List<VehicleFrequency> vehicles, boolean includeParkingId) {
        if (vehicles.isEmpty()) {
            return new VehicleSummary(0, 0.0, "", null, 0);
        }
        
        Integer totalRegistrations = vehicles.stream()
            .mapToInt(VehicleFrequency::getCount)
            .sum();
        
        Double averageRegistrations = vehicles.stream()
            .mapToInt(VehicleFrequency::getCount)
            .average()
            .orElse(0.0);
        
        VehicleFrequency mostFrequent = vehicles.get(0);
        
        return new VehicleSummary(
            totalRegistrations,
            averageRegistrations,
            mostFrequent.getPlate(),
            includeParkingId ? mostFrequent.getParkingId() : null,
            mostFrequent.getCount()
        );
    }
}
