package com.example.pet_adopting.requests;

import lombok.Data;

@Data
public class UpdateBreedRequest {
    private int typeId;
    private String name;
}
