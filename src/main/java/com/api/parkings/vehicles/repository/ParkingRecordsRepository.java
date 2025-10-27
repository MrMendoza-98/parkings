package com.api.parkings.vehicles.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.parkings.parkings.model.Parkings;
import com.api.parkings.vehicles.model.ParkingRecords;

public interface ParkingRecordsRepository extends JpaRepository<ParkingRecords, Integer> {
    @Query("SELECT pr FROM ParkingRecords pr " +
           "WHERE UPPER(pr.vehicleId.plate) = UPPER(:plate) " +
           "AND pr.parkingId.id = :parkingId " +
           "AND pr.exitTime IS NULL")
    Optional<ParkingRecords> findActiveRecordByPlateAndParkingId(@Param("plate") String plate,@Param("parkingId") Integer parkingId);
    
    @Query("SELECT pr FROM ParkingRecords pr WHERE pr.vehicleId.plate = :plate AND pr.exitTime IS NULL")
    Optional<ParkingRecords> findActiveRecordByPlate(@Param("plate") String plate);

    @Query("SELECT COUNT(pr) FROM ParkingRecords pr WHERE pr.parkingId = :parking AND pr.exitTime IS NULL")
    Long countActiveVehiclesByParking(@Param("parking") Parkings parking);
    
    @Query("SELECT pr FROM ParkingRecords pr " +
           "WHERE pr.parkingId.id = :parkingId " +
           "AND pr.exitTime IS NULL " +
           "ORDER BY pr.entryTime DESC")
    List<ParkingRecords> findActiveRecordsByParkingIdOrderedDesc(@Param("parkingId") Integer parkingId);
    
    // Métodos para IndicatorsService
    List<ParkingRecords> findByParkingId_Id(Integer parkingId);
    
    List<ParkingRecords> findByParkingId_IdAndExitTimeIsNull(Integer parkingId);
    
    Long countByVehicleId_PlateAndParkingId_Id(String plate, Integer parkingId);
    
    List<ParkingRecords> findByVehicleId_PlateContainingIgnoreCaseAndExitTimeIsNull(String partialPlate);
    
    List<ParkingRecords> findByParkingId_IdAndExitTimeBetween(Integer parkingId, LocalDateTime start, LocalDateTime end);
    
    Long countByParkingId_Id(Integer parkingId);
}
