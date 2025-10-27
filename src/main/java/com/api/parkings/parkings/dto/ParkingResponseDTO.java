package com.api.parkings.parkings.dto;

import com.api.parkings.users.dto.UserResponseDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParkingResponseDTO {
    @NotNull
    private Integer id;
    
    @NotNull
    private String name;
    
    @NotNull
    private Integer capacity;
    
    @NotNull
    private Double pricePerHour;
    
    @NotNull
    private UserResponseDTO owner;
}