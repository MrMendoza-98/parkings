package com.api.parkings.parkings.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.api.parkings.parkings.model.Parkings;
import com.api.parkings.users.dto.UserMapper;
import com.api.parkings.users.service.IUsersService;

@Mapper(componentModel = "spring",
uses = {UserMapper.class, IUsersService.class},
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ParkingMapper {

    ParkingResponseDTO toResponseDTO(Parkings parking);
    @Mapping(target = "id", ignore = true)
    Parkings toEntity(ParkingCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    List<ParkingResponseDTO> toResponseDTOList(List<Parkings> parkings);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(@MappingTarget Parkings parking, ParkingUpdateDTO dto);
    
}