package com.api.parkings.indicators.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirstTimeParkedVehicle {
    private String plate;
    private LocalDateTime entryTime;
}