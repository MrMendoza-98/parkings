package com.api.parkings.auth.service;

import com.api.parkings.auth.dto.LoginResponseDTO;
import com.api.parkings.users.dto.UserResponseDTO;

public interface IAuthService {
    UserResponseDTO validateUser(String email, String password);
    LoginResponseDTO login(String email, String password);
    void logout(String token);
}
