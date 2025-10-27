package com.api.parkings.vehicles.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationRequestDTO {
    
    @NotBlank(message = "El campo 'email' no puede estar vacío")
    @Email(message = "Debe ser un email válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;
    
    @NotBlank(message = "El campo 'placa' no puede estar vacío")
    @Size(min = 6, max = 10, message = "La placa debe tener entre 6 y 10 caracteres")
    private String placa;
    
    @NotBlank(message = "El campo 'mensaje' no puede estar vacío")
    @Size(max = 1000, message = "El mensaje no puede exceder 1000 caracteres")
    private String mensaje;
    
    @NotNull(message = "El campo 'parqueaderoId' no puede ser nulo")
    private Integer parqueaderoId;
}