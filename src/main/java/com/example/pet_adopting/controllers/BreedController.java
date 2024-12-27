package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Breed;
import com.example.pet_adopting.requests.CreateBreedRequest;
import com.example.pet_adopting.requests.UpdateBreedRequest;
import com.example.pet_adopting.services.BreedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Breed>> getAllBreeds() {
        List<Breed> breeds = breedService.getAllBreeds();
        return ResponseEntity.ok(breeds);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Breed> getBreedById(@PathVariable Long id) {
        Breed breed = breedService.getBreedById(id);
        if (breed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(breed);
    }

    @PostMapping
    public ResponseEntity<Breed> createBreed(@RequestBody CreateBreedRequest breedRequest) {
        Breed createdBreed = breedService.createBreed(breedRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBreed);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Breed> updateBreed(@PathVariable Long id, @RequestBody UpdateBreedRequest breedRequest) {
        Breed updatedBreed = breedService.updateBreed(id, breedRequest);
        if (updatedBreed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedBreed);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBreed(@PathVariable Long id) {
        Breed breed = breedService.getBreedById(id);
        if (breed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        breedService.deleteBreed(id);
        return ResponseEntity.noContent().build();
    }
}