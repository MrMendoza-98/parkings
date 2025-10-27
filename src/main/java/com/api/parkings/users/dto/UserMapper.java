package com.api.parkings.users.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.api.parkings.users.model.Users;

@Component
public class UserMapper {
    
    public UserResponseDTO toResponseDTO(Users user) {
        if (user == null) {
            return null;
        }
        
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
    
    public List<UserResponseDTO> toResponseDTOList(List<Users> users) {
        return users.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public Users toEntity(UserCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Users user = new Users();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }
    
    public void updateEntityFromDTO(Users user, UserUpdateDTO dto) {
        if (dto == null || user == null) {
            return;
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
    }
}