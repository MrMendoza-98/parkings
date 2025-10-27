package com.api.parkings.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkings.email.dto.EmailRequestDTO;
import com.api.parkings.email.dto.EmailResponseDTO;
import com.api.parkings.email.service.IEmailService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Validated
@Slf4j
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private IEmailService emailService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('SOCIO')")
    @PostMapping("/send")
    public ResponseEntity<EmailResponseDTO> sendEmail(@Valid @RequestBody EmailRequestDTO request) {
        log.info("Endpoint /email/send llamado con datos: {}", request);
        
        EmailResponseDTO response = emailService.sendEmail(request);
        
        
        return ResponseEntity.ok(response);
    }
}