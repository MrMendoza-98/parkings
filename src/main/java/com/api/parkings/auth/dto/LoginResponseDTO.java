package com.api.parkings.auth.dto;

import com.api.parkings.users.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    
    private String access_token;
    private Long expires_in;
    private UserResponseDTO user;
}