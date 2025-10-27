package com.api.parkings.parkings.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import com.api.parkings.users.model.Users;

import jakarta.persistence.Entity;
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
public class Parkings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer capacity;
    private Double pricePerHour;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Users owner;
}
