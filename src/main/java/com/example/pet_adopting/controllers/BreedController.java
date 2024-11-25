package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Breed;
import com.example.pet_adopting.requests.CreateBreedRequest;
import com.example.pet_adopting.requests.UpdateBreedRequest;
import com.example.pet_adopting.services.BreedService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/breeds")
public class BreedController {

    private final BreedService breedService;

    public BreedController(BreedService breedService) {
        this.breedService = breedService;
    }

    @GetMapping
    public List<Breed> getAllBreeds() {
        return breedService.getAllBreeds();
    }

    @GetMapping("/{id}")
    public Breed getBreedById(@PathVariable Long id) {
        return breedService.getBreedById(id);
    }

    @PostMapping
    public Breed createBreed(@RequestBody CreateBreedRequest breed) {
        return breedService.createBreed(breed);
    }

    @PutMapping("/{id}")
    public Breed updateBreed(@PathVariable Long id, @RequestBody UpdateBreedRequest breed) {
        return breedService.updateBreed(id, breed);
    }

    @DeleteMapping("/{id}")
    public void deleteBreed(@PathVariable Long id) {
        breedService.deleteBreed(id);
    }
}