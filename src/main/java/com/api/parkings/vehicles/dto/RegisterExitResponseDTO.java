package com.api.parkings.vehicles.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterExitResponseDTO {
    
    private boolean success;
    private String message;
    private String plate;
    private Integer parkingId;
    private LocalDateTime exitTime;
    private Double totalPrice;
}
