package com.api.parkings.indicators.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingEarnings {
    private Double today;
    private Double week;
    private Double month;
    private Double year;
}