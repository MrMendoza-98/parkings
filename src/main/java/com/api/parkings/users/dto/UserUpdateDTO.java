package com.api.parkings.users.dto;

import com.api.parkings.users.model.EnumRole;

import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    
    @Email(message = "Debe ser un email válido")
    private String email;
    
    private String password;
    
    private EnumRole role;
}