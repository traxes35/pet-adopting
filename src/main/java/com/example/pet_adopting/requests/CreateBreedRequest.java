package com.example.pet_adopting.requests;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class CreateBreedRequest {
    private Long typeId;
    private String name;

    public CreateBreedRequest(Long typeId, String name) {
        this.typeId = typeId;
        this.name = name;
    }

    public CreateBreedRequest() {
    }
}
