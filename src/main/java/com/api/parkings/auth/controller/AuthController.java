package com.api.parkings.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkings.auth.dto.LoginRequestDTO;
import com.api.parkings.auth.dto.LoginResponseDTO;
import com.api.parkings.auth.dto.LogoutResponseDTO;
import com.api.parkings.auth.service.IAuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            LoginResponseDTO result = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en login: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                authHeader = request.getHeader("authorization");
            }
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                authService.logout(token);
                return ResponseEntity.ok(new LogoutResponseDTO("Logout successful"));
            }
            return ResponseEntity.badRequest().body("Token no proporcionado");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error en logout: " + e.getMessage());
        }
    }
}
