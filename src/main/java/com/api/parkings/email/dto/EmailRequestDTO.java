package com.api.parkings.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDTO {
    @NotBlank(message = "El campo 'to' no puede estar vacío")
    @Email(message = "Debe ser un email válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String to;
    @NotBlank(message = "El campo 'subject' no puede estar vacío")
    @Size(max = 100, message = "El asunto no puede exceder 100 caracteres")
    private String subject;
    @NotBlank(message = "El campo 'body' no puede estar vacío") 
    private String body;
}
