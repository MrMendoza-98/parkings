package com.api.parkings.email.service;

import com.api.parkings.email.dto.EmailRequestDTO;
import com.api.parkings.email.model.Emails;
import com.api.parkings.email.dto.EmailResponseDTO;

public interface IEmailService {
    public EmailResponseDTO sendEmail(EmailRequestDTO emailRequest);
    public Emails sendEmailAndReturn(String to, String subject, String body);
    public void sendNotification(com.api.parkings.vehicles.model.ParkingRecords parkingRecord, String emailType);
}
