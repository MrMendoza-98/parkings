package com.api.parkings.email.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.parkings.email.model.Emails;

public interface EmailsRepository extends JpaRepository<Emails, Integer> {
    Optional<Emails> findByTo(String to);
}
