package com.example.pet_adopting.requests;

import lombok.Data;

@Data
public class CreateBreedRequest {
    private Long id;
    private Long typeId;
    private String name;

}
