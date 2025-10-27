package com.api.parkings.vehicles.model;

import java.time.LocalDateTime;

import com.api.parkings.parkings.model.Parkings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParkingRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parkings parkingId;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicles vehicleId;

    @Column(nullable = false)
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;
    private Double totalPrice;
}
