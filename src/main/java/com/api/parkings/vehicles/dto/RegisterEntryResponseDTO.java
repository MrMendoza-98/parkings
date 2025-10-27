package com.api.parkings.vehicles.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEntryResponseDTO {
    
    private boolean success;
    private String message;
    private ParkingRecordInfoDTO parkingRecord;
    private VehicleInfoDTO vehicle;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParkingRecordInfoDTO {
        private Integer id;
        private LocalDateTime entryTime;
        private String parkingName;
        private Double pricePerHour;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleInfoDTO {
        private Integer id;
        private String plate;
        private String ownerName;
        private LocalDateTime registeredAt;
    }
}