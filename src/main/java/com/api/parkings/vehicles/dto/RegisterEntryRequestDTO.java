package com.api.parkings.vehicles.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEntryRequestDTO {
    
    @NotBlank(message = "La placa es requerida")
    @Size(min = 6, max = 10, message = "La placa debe tener entre 6 y 10 caracteres")
    @Pattern(regexp = "^[A-Z]{3}\\d{2}[A-Z]?\\d?$", 
             message = "Formato de placa inválido. Use formato ABC123 o ABC12A")
    private String plate;
    
    @NotNull(message = "El ID del parqueadero es requerido")
    private Integer parkingId;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;
    
    @NotBlank(message = "El nombre del propietario es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String ownerName;
}