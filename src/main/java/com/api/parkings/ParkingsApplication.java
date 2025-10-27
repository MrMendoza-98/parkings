package com.api.parkings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EntityScan("com.api.parkings")
@EnableJpaAuditing
public class ParkingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingsApplication.class, args);
	}

}
