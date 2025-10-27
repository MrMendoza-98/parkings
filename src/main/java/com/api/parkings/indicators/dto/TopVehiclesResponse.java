package com.api.parkings.indicators.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopVehiclesResponse {
    
    private List<VehicleFrequency> vehicles;
    private Integer total;
    private VehicleSummary summary;
    private LocalDateTime timestamp;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleFrequency {
        private String plate;
        private Integer parkingId;
        private Integer count;
        
        public VehicleFrequency(String plate, Integer count) {
            this.plate = plate;
            this.count = count;
            this.parkingId = null;
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleSummary {
        private Integer totalRegistrations;
        private Double averageRegistrations;
        private String mostFrequentVehicle;
        private Integer mostFrequentParkingId;
        private Integer mostFrequentCount;
    }
}