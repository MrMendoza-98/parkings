package com.api.parkings.users.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.api.parkings.users.model.Users;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserResponseDTO toResponseDTO(Users user);

    List<UserResponseDTO> toResponseDTOList(List<Users> users);

    @Mapping(target = "id", ignore = true)
    Users toEntity(UserCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(UserUpdateDTO dto, @MappingTarget Users user);

}