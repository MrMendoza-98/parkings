package com.api.parkings.users.dto;

import com.api.parkings.users.model.EnumRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserResponseDTO {
    
    @NotNull(message = "El ID no puede ser nulo")
    private Integer id;
    
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe ser un email válido")
    private String email;
    
    @NotNull(message = "El rol no puede ser nulo")
    private EnumRole role;
}