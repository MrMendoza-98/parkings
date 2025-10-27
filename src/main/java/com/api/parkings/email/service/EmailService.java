package com.api.parkings.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.api.parkings.email.dto.EmailRequestDTO;
import com.api.parkings.email.dto.EmailResponseDTO;
import com.api.parkings.email.model.Emails;
import com.api.parkings.email.repository.EmailsRepository;
import com.api.parkings.vehicles.model.ParkingRecords;

@Service
@Slf4j
public class EmailService implements IEmailService {
    @Autowired
    private EmailsRepository emailsRepository;

    @Override
    public EmailResponseDTO sendEmail(EmailRequestDTO emailRequest) {
        
        try {
            
            Emails savedEmail = sendEmailAndReturn(
                emailRequest.getTo(), 
                emailRequest.getSubject(), 
                emailRequest.getBody()
            );
            
            EmailResponseDTO response = new EmailResponseDTO(
                true,
                "Correo Enviado",
                savedEmail.getId(),
                savedEmail.getTo(),
                savedEmail.getSubject(),
                savedEmail.getBody(),
                savedEmail.getSentAt()
            );
            
            logEmailResponse(response);
            
            return response;
            
        } catch (Exception e) {
            return handleEmailError(emailRequest, e);
        }
    }
    
    @Override
    public Emails sendEmailAndReturn(String to, String subject, String body) {
        
        Emails emailEntity = new Emails();
        emailEntity.setTo(to);
        emailEntity.setSubject(subject);
        emailEntity.setBody(body);
        
        Emails savedEmail = emailsRepository.save(emailEntity);
        
        return savedEmail;
    }
    
    private void logEmailResponse(EmailResponseDTO response) {
        log.info("=== RESPUESTA DE CORREO PROCESADO ===");
        log.info("Estado: {} ({})", response.isSuccess() ? "SUCCESS" : "FAILURE", response.getStatus());
        log.info("Mensaje: {}", response.getMessage());
        log.info("ID del Email: {}", response.getEmailId());
        log.info("Destinatario: {}", response.getTo());
        log.info("Asunto: {}", response.getSubject());
        log.info("Cuerpo: {} caracteres", response.getBody() != null ? response.getBody().length() : 0);
        log.info("Fecha de Envío: {}", response.getSentAt());
        log.info("======================================\n");
    }
    
    private EmailResponseDTO handleEmailError(EmailRequestDTO emailRequest, Exception e) {
        EmailResponseDTO errorResponse = createErrorResponse(emailRequest, e);
        
        logEmailResponse(errorResponse);
        
        return errorResponse;
    }
    
    private EmailResponseDTO createErrorResponse(EmailRequestDTO emailRequest, Exception e) {
        EmailResponseDTO response = new EmailResponseDTO();
        response.setSuccess(false);
        response.setMessage("Error al enviar correo: " + e.getMessage());
        response.setStatus("ERROR");
        response.setTo(emailRequest.getTo());
        response.setSubject(emailRequest.getSubject());
        response.setBody(emailRequest.getBody());
        response.setEmailId(null);
        response.setSentAt(null);
        
        return response;
    }

    @Override
    public void sendNotification(ParkingRecords parkingRecord, String emailType) {
        try {
            EmailRequestDTO emailRequest = buildEmailRequest(parkingRecord, emailType);
            
            String actionText = "ENTRADA".equals(emailType) ? "entrada" : "salida";
            log.info("Enviando notificación de {} para vehículo: {}", actionText, parkingRecord.getVehicleId().getPlate());
            
            var response = sendEmail(emailRequest);
            
            if (response.isSuccess()) {
                log.info("Notificación de {} enviada exitosamente para vehículo: {} (Email ID: {})", 
                        actionText, parkingRecord.getVehicleId().getPlate(), response.getEmailId());
            } else {
                log.warn("Error al enviar notificación de {}: {}", actionText, response.getMessage());
            }
            
        } catch (Exception e) {
            String actionText = "ENTRADA".equals(emailType) ? "entrada" : "salida";
            log.error("Error al procesar notificación de {} para vehículo: {}", 
                     actionText, parkingRecord.getVehicleId().getPlate(), e);
        }
    }

    
    private EmailRequestDTO buildEmailRequest(ParkingRecords parkingRecord, String emailType) {
        String to = parkingRecord.getVehicleId().getEmail();
        String subject = buildEmailSubject(parkingRecord, emailType);
        String body = buildEmailBody(parkingRecord, emailType);
        
        return new EmailRequestDTO(to, subject, body);
    }

    
    private String buildEmailSubject(ParkingRecords parkingRecord, String emailType) {
        String subjectPrefix = "ENTRADA".equals(emailType) ? "Confirmación de Ingreso" : "Comprobante de Salida";
        return String.format("%s - Vehículo %s", subjectPrefix, parkingRecord.getVehicleId().getPlate());
    }

   
    private String buildEmailBody(ParkingRecords parkingRecord, String emailType) {
        StringBuilder body = new StringBuilder();
        
        body.append(String.format("Estimado/a %s,\n\n", parkingRecord.getVehicleId().getOwnerName()));
        
        if ("ENTRADA".equals(emailType)) {
            body.append("Su vehículo ha sido registrado exitosamente en nuestro sistema de parqueaderos.\n\n");
            body.append("DETALLES DEL INGRESO:\n");
        } else {
            body.append("Su vehículo ha salido exitosamente del parqueadero.\n\n");
            body.append("COMPROBANTE DE PAGO:\n");
        }
        
        body.append(String.format("• Placa del Vehículo: %s\n", parkingRecord.getVehicleId().getPlate()));
        body.append(String.format("• Parqueadero: %s\n", parkingRecord.getParkingId().getName()));
        body.append(String.format("• Fecha y Hora de Ingreso: %s\n", parkingRecord.getEntryTime().toString()));
        
        if ("ENTRADA".equals(emailType)) {
            body.append(String.format("• Tarifa por Hora: $%.2f\n\n", parkingRecord.getParkingId().getPricePerHour()));
            body.append("Por favor conserve este comprobante para su salida.\n\n");
        } else {
            java.time.Duration duration = java.time.Duration.between(
                parkingRecord.getEntryTime(), parkingRecord.getExitTime());
            long hours = (long) Math.ceil(duration.toMinutes() / 60.0);
            
            body.append(String.format("• Fecha y Hora de Salida: %s\n", parkingRecord.getExitTime().toString()));
            body.append(String.format("• Tiempo Total: %d hora(s)\n", hours));
            body.append(String.format("• Tarifa por Hora: $%.2f\n", parkingRecord.getParkingId().getPricePerHour()));
            body.append(String.format("• TOTAL A PAGAR: $%.2f\n\n", parkingRecord.getTotalPrice()));
            body.append("Gracias por utilizar nuestros servicios.\n\n");
        }
        
        body.append("Atentamente,\n");
        body.append("Sistema de Parqueaderos");
        
        return body.toString();
    }
}
