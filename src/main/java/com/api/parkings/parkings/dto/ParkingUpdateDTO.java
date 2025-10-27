package com.api.parkings.parkings.dto;

import com.api.parkings.common.validation.UserExists;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ParkingUpdateDTO {
    private String name;
    
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacity;
    
    @DecimalMin(value = "0.01", message = "El precio por hora debe ser mayor a 0")
    private Double pricePerHour;
    
    @UserExists(message = "El propietario no existe en la base de datos")
    private Integer owner;
}