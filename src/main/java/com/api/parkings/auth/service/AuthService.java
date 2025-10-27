package com.api.parkings.auth.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.parkings.users.dto.UserResponseDTO;
import com.api.parkings.users.model.Users;
import com.api.parkings.users.repository.UsersRepository;
import com.api.parkings.auth.dto.LoginResponseDTO;
import com.api.parkings.auth.jwt.JwtTokenRevocationProvider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;

@Service
@Slf4j
public class AuthService implements IAuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenRevocationProvider jwtTokenRevocationProvider;

    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public UserResponseDTO validateUser(String email, String password) {
        Optional<Users> userOpt = Optional.ofNullable(usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid credentials")));
        Users user = userOpt.get();
        if (userOpt.isPresent() && passwordEncoder.matches(password, user.getPassword())) {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            return dto;
        }
        return null; 
    }

    @Override
    public LoginResponseDTO login(String email, String password) {
        UserResponseDTO user = validateUser(email, password);
        if (user == null) {
            throw new RuntimeException("Invalid credentials");
        }
        return generateJwtToken(user);
    }
    
    private LoginResponseDTO generateJwtToken(UserResponseDTO user) {
        try {
            Date expirationDate = new Date(System.currentTimeMillis() + (jwtExpiration * 1000));
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            String token = Jwts.builder()
                .claim("email", user.getEmail())
                .claim("role", user.getRole().toString())
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(key)
                .compact();
                
            return new LoginResponseDTO(token, jwtExpiration, user);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void logout(String token) {
        if (token != null && !token.trim().isEmpty()) {
            jwtTokenRevocationProvider.addRevokedToken(token);
        }
    }
}
