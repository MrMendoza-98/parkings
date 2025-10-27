package com.api.parkings.parkings.dto;

import com.api.parkings.common.validation.UserExists;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParkingCreateDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;
    
    @NotNull(message = "La capacidad no puede ser nula")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacity;
    
    @NotNull(message = "El precio por hora no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio por hora debe ser mayor a 0")
    private Double pricePerHour;
    
    @NotNull(message = "El ID del propietario no puede ser nulo")
    @UserExists(message = "El propietario no existe en la base de datos")
    private Integer owner;
}
