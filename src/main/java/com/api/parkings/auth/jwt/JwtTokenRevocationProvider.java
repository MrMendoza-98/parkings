package com.api.parkings.auth.jwt;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtTokenRevocationProvider {
    private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();
    
    public void addRevokedToken(String token) {
        if (token != null && !token.trim().isEmpty()) {
            revokedTokens.add(token);
        }
    }
    
    public boolean isTokenRevoked(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        boolean isRevoked = revokedTokens.contains(token);
        
        return isRevoked;
    }    
}
