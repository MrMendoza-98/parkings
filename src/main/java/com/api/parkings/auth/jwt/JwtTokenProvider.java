package com.api.parkings.auth.jwt;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenProvider {
   @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private JwtTokenRevocationProvider jwtTokenRevocationProvider;
    
    public boolean isTokenRevoked(String token) {
        return jwtTokenRevocationProvider.isTokenRevoked(token);
    }
    
    public boolean validateToken(String token) {
        try {
            if (isTokenRevoked(token)) {
                return false;
            }
            
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
   
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("email", String.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    public String getUserIdFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
    
    public String getRoleFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("role", String.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        try {
            String role = getRoleFromToken(token);
            if (role != null) {
                return List.of(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
    
    
    private Claims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}