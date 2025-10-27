package com.api.parkings.parkings.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api.parkings.parkings.model.Parkings;
import com.api.parkings.users.model.Users;

public interface ParkingsRepository extends JpaRepository<Parkings, Integer> {
    
    Long countByOwner(Users owner);
    
}
