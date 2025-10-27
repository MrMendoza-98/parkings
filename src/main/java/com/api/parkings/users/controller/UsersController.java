package com.api.parkings.users.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkings.common.exceptions.ResourceNotFoundException;
import com.api.parkings.users.dto.UserCreateDTO;
import com.api.parkings.users.dto.UserMapper;
import com.api.parkings.users.dto.UserResponseDTO;
import com.api.parkings.users.dto.UserUpdateDTO;
import com.api.parkings.users.model.Users;
import com.api.parkings.users.service.IUsersService;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private IUsersService usersService;
    
    @Autowired
    private UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        List<Users> users = usersService.getAllUsers();
        return userMapper.toResponseDTOList(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        Users user = userMapper.toEntity(userCreateDTO);
        Users savedUser = usersService.createUser(user);
        return userMapper.toResponseDTO(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Integer id) {
        Users user = usersService.getUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return userMapper.toResponseDTO(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Integer id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        Users existingUser = usersService.getUserById(id);
        if (existingUser == null) {
            throw new ResourceNotFoundException("User not found");
        }
        
        userMapper.updateEntityFromDTO(existingUser, userUpdateDTO);
        Users updatedUser = usersService.updateUser(id, existingUser);
        return userMapper.toResponseDTO(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        Users user = usersService.getUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        usersService.deleteUser(id);
    }
}
