package com.api.parkings.parkings.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.parkings.parkings.model.Parkings;
import com.api.parkings.users.dto.UserMapper;
import com.api.parkings.users.service.IUsersService;

@Component
public class ParkingMapper {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private IUsersService usersService;
    
    public ParkingResponseDTO toResponseDTO(Parkings parking) {
        if (parking == null) {
            return null;
        }
        
        ParkingResponseDTO dto = new ParkingResponseDTO();
        dto.setId(parking.getId());
        dto.setName(parking.getName());
        dto.setCapacity(parking.getCapacity());
        dto.setPricePerHour(parking.getPricePerHour());
        dto.setOwner(userMapper.toResponseDTO(parking.getOwner()));
        return dto;
    }
    
    public List<ParkingResponseDTO> toResponseDTOList(List<Parkings> parkings) {
        return parkings.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public Parkings toEntity(ParkingCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Parkings parking = new Parkings();
        parking.setName(dto.getName());
        parking.setCapacity(dto.getCapacity());
        parking.setPricePerHour(dto.getPricePerHour());
        
        if (dto.getOwnerId() != null) {
            parking.setOwner(usersService.getUserById(dto.getOwnerId()));
        }
        
        return parking;
    }
    
    public void updateEntityFromDTO(Parkings parking, ParkingUpdateDTO dto) {
        if (dto == null || parking == null) {
            return;
        }
        
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            parking.setName(dto.getName());
        }
        
        if (dto.getCapacity() != null) {
            parking.setCapacity(dto.getCapacity());
        }
        
        if (dto.getPricePerHour() != null) {
            parking.setPricePerHour(dto.getPricePerHour());
        }
        
        if (dto.getOwnerId() != null) {
            parking.setOwner(usersService.getUserById(dto.getOwnerId()));
        }
    }
}