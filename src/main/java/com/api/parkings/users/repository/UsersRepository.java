package com.api.parkings.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.parkings.users.model.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
}
