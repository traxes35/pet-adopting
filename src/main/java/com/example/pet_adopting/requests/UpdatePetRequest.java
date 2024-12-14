package com.example.pet_adopting.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
@AllArgsConstructor
public class UpdatePetRequest {
    private String name;
    private int age;
    private String gender;
    private boolean isVaccinated;
    private boolean isActive;
    private String description;

    public UpdatePetRequest(String description, boolean isActive, boolean isVaccinated, String gender, int age, String name) {
        this.description = description;
        this.isActive = isActive;
        this.isVaccinated = isVaccinated;
        this.gender = gender;
        this.age = age;
        this.name = name;
    }

    public UpdatePetRequest() {
    }
}