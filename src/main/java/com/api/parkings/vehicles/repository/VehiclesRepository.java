package com.api.parkings.vehicles.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.parkings.vehicles.model.Vehicles;

public interface VehiclesRepository extends JpaRepository<Vehicles, Integer> {
   
    Optional<Vehicles> findByPlate(String plate);
}
