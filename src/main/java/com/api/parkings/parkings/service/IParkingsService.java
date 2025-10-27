package com.api.parkings.parkings.service;

import java.util.List;

import com.api.parkings.parkings.model.Parkings;

public interface IParkingsService {

    public List<Parkings> getAllParkings();

    public Parkings getParkingById(Integer id);

    public Parkings createParking(Parkings parking);

    public Parkings updateParking(Integer id, Parkings parking);

    public void deleteParking(Integer id);
} 
