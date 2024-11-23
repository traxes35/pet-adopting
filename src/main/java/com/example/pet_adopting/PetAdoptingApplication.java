package com.example.pet_adopting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
(exclude = {SecurityAutoConfiguration.class })

public class PetAdoptingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetAdoptingApplication.class, args);
	}

}
