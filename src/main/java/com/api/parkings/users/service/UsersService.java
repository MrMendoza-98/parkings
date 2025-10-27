package com.api.parkings.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.parkings.users.model.Users;
import com.api.parkings.users.model.EnumRole;
import com.api.parkings.users.repository.UsersRepository;
import com.api.parkings.parkings.repository.ParkingsRepository;

import java.util.List;
@Service
public class UsersService implements IUsersService {
    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ParkingsRepository parkingsRepository;
    
    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        String userEmail = authentication.getName();
        return usersRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado en base de datos"));
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Users getUserById(Integer id) {
        Users user = usersRepository.findById(id).orElse(null);
        return user;
    }

    @Override
    public Users createUser(Users user) {
        if (user.getRole() == null) {
            throw new IllegalArgumentException("Rol de usuario no válido");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return usersRepository.save(user);
    }

    @Override
    public Users updateUser(Integer id, Users user) {
        Users existingUser = usersRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            existingUser.setRole(user.getRole());
            return usersRepository.save(existingUser);
        }
        return null;
    }

    @Override
    public void deleteUser(Integer id) {
        Users user = usersRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario con ID " + id + " no encontrado"));
        
        Long parkingCount = parkingsRepository.countByOwner(user);
        if (parkingCount > 0) {
            throw new IllegalArgumentException(
                String.format("No se puede eliminar el usuario con ID %d. " +
                            "Tiene %d parqueadero(s) asociado(s). " +
                            "Primero debe eliminar o transferir los parqueaderos.", 
                            id, parkingCount)
            );
        }
        
        usersRepository.deleteById(id);
    }

    @Override
    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void createUserIfNotExists(String email, String password, EnumRole role) {
        if (findByEmail(email) == null) {
            Users adminUser = new Users();
            adminUser.setEmail(email);
            adminUser.setPassword(password);
            adminUser.setRole(role);
            createUser(adminUser);
        }
    }
}
