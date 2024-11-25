package com.example.pet_adopting.requests;

import lombok.Data;

@Data
public class UpdatePetRequest {
    private String name;
    private int age;
    private String gender;
    private boolean isVaccinated;
    private boolean isActive;
    private String description;
}