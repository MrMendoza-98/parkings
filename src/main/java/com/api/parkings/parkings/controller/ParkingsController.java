package com.api.parkings.parkings.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkings.common.exceptions.ResourceNotFoundException;
import com.api.parkings.parkings.dto.ParkingCreateDTO;
import com.api.parkings.parkings.dto.ParkingMapper;
import com.api.parkings.parkings.dto.ParkingResponseDTO;
import com.api.parkings.parkings.dto.ParkingUpdateDTO;
import com.api.parkings.parkings.model.Parkings;
import com.api.parkings.parkings.service.IParkingsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/parkings")
public class ParkingsController {
    
    @Autowired
    private IParkingsService parkingsService;
    
    @Autowired
    private ParkingMapper parkingMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ParkingResponseDTO> getAllParkings() {
        List<Parkings> parkings = parkingsService.getAllParkings();
        return parkingMapper.toResponseDTOList(parkings);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ParkingResponseDTO create(@Valid @RequestBody ParkingCreateDTO dto) {
        Parkings parking = parkingMapper.toEntity(dto); 
        Parkings savedParking = parkingsService.createParking(parking);
        return parkingMapper.toResponseDTO(savedParking);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ParkingResponseDTO getParkingById(@PathVariable Integer id) {
        Parkings parking = parkingsService.getParkingById(id);
        if (parking == null) {
            throw new ResourceNotFoundException("Parking not found");
        }
        return parkingMapper.toResponseDTO(parking);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ParkingResponseDTO updateParking(@PathVariable Integer id, @Valid @RequestBody ParkingUpdateDTO parkingUpdateDTO) {
        Parkings existingParking = parkingsService.getParkingById(id);
        if (existingParking == null) {
            throw new ResourceNotFoundException("Parking not found");
        }
        parkingMapper.updateEntityFromDTO(existingParking, parkingUpdateDTO);
        Parkings updatedParking = parkingsService.updateParking(id, existingParking);
        return parkingMapper.toResponseDTO(updatedParking);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteParking(@PathVariable Integer id) {
        Parkings existingParking = parkingsService.getParkingById(id);
        if (existingParking == null) {
            throw new ResourceNotFoundException("Parking not found");
        }
        parkingsService.deleteParking(id);
    }
}
