package com.api.parkings.users.dto;

import com.api.parkings.users.model.EnumRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDTO {
    
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe ser un email válido")
    private String email;
    
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
    
    @NotNull(message = "El rol no puede ser nulo")
    private EnumRole role;
}