package com.example.pet_adopting.requests;

import lombok.Data;

@Data
public class CreateAdoptionRequest {
    private long userId;
    private long petId;
}