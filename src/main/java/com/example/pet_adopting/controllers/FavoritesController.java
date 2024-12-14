package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Favorites;
import com.example.pet_adopting.requests.FavoritesRequest;
import com.example.pet_adopting.services.FavoritesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {
private FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }
    @GetMapping()
    public List <Favorites> getAllFavorites(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> petId) {
        return favoritesService.getAllFavorites(userId,petId);
    }
   @GetMapping("/{favoritesId}")
    public Optional<Favorites> getFavorites(@PathVariable Long favoritesId) {
       return favoritesService.getFavoritesById(favoritesId);
    }
    @PostMapping
    public Favorites createFavorites(@RequestBody FavoritesRequest favorites) {
        return favoritesService.addFavorites(favorites);
    }
    @DeleteMapping("/{favoritesId}")
    public void deleteFavorites(@PathVariable Long favoritesId) {
        favoritesService.deleteFavorites(favoritesId);
    }
}