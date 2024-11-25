package com.example.pet_adopting.requests;

import lombok.Data;

@Data
public class CreatePetRequest {
    private Long id;
    private Long userId;
    private Long cityId;
    private Long typeId;
    private Long breedId;
    private String name;
    private int age;
    private String gender;
    private boolean isVaccinated;
    private boolean isActive;
    private String description;
}