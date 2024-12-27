package com.example.pet_adopting.requests;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode
public class CreatePetRequest {
    private Long cityId;
    private Long typeId;
    private Long breedId;
    private String name;
    private int age;
    private String gender;
    private boolean isVaccinated;
    private boolean isActive;
    private String description;
    private String imagePath;

    public CreatePetRequest() {

    }

    public CreatePetRequest( Long cityId, Long typeId, Long breedId, String name, int age, String gender, boolean isVaccinated, boolean isActive, String description, String imagePath) {
        this.cityId = cityId;
        this.typeId = typeId;
        this.breedId = breedId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.isVaccinated = isVaccinated;
        this.isActive = isActive;
        this.description = description;
        this.imagePath = imagePath;

    }
}