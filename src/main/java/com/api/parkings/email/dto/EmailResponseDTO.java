package com.api.parkings.email.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponseDTO {
    private boolean success;
    private String message;
    private Integer emailId;
    private String to;
    private String subject;
    private String body;
    private LocalDateTime sentAt;
    private String status;
    
    // Constructor para respuesta exitosa
    public EmailResponseDTO(boolean success, String message, Integer emailId, 
                                   String to, String subject, String body, LocalDateTime sentAt) {
        this.success = success;
        this.message = message;
        this.emailId = emailId;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.sentAt = sentAt;
        this.status = success ? "ENVIADO" : "ERROR";
    }
}
