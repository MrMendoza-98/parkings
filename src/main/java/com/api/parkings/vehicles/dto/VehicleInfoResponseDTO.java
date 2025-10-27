package com.api.parkings.vehicles.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleInfoResponseDTO {
    
    private Integer id;
    private String plate;
    private String ownerName;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isCurrentlyParked;
    private Integer currentParkingId;
    private LocalDateTime currentEntryTime;
}