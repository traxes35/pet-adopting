package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Favorites;
import com.example.pet_adopting.entities.Pet;
import com.example.pet_adopting.requests.FavoritesRequest;
import com.example.pet_adopting.services.FavoritesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @GetMapping()
    public ResponseEntity<List<Pet>> getAllFavoritesForCurrentUser() {
        try {
            // Favori pet listesini service üzerinden al
            List<Pet> pets = favoritesService.getFavoritesForCurrentUser();
            return ResponseEntity.ok(pets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping
    public ResponseEntity<Favorites> createFavorites(@RequestBody FavoritesRequest favorites) {
        Favorites createdFavorites = favoritesService.addFavorites(favorites);
        if (createdFavorites == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFavorites);
    }

    @DeleteMapping("/{favoritesId}")
    public void deleteFavorites(@PathVariable Long favoritesId) {
        favoritesService.deleteFavorites(favoritesId);
    }
}