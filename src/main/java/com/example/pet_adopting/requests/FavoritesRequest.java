package com.example.pet_adopting.requests;

import lombok.Data;

@Data
public class FavoritesRequest {
    private long userId;
    private long petId;
}
