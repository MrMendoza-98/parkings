package com.api.parkings.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.parkings.users.model.EnumRole;
import com.api.parkings.users.service.UsersService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsersService usersService;

    @Override
    public void run(String... args) throws Exception {
        usersService.createUserIfNotExists("admin@mail.com", "admin", EnumRole.ADMIN);
    }
}