package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Adoption;
import com.example.pet_adopting.requests.CreateAdoptionRequest;
import com.example.pet_adopting.services.AdoptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {

    private final AdoptionService adoptionService;

    // Constructor Injection
    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    // POST: Create new adoption
    @PostMapping
    public ResponseEntity<Adoption> createAdoption(@RequestBody CreateAdoptionRequest adoptionRequest) {
        Adoption adoption = adoptionService.createAdoption(adoptionRequest);
        return ResponseEntity.ok(adoption);
    }

    // GET: Retrieve an adoption by ID
    @GetMapping("/{id}")
    public ResponseEntity<Adoption> getAdoptionById(@PathVariable long id) {
        Optional<Adoption> adoption = adoptionService.getAdoptionById(id);

        return adoption.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT: Update an existing adoption
    @PutMapping("/{id}")
    public ResponseEntity<Adoption> updateAdoption(@PathVariable long id, @RequestBody CreateAdoptionRequest adoptionRequest) {
        Optional<Adoption> updatedAdoption = adoptionService.updateAdoption(id, adoptionRequest);

        return updatedAdoption.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE: Delete an adoption by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoption(@PathVariable long id) {
        boolean isDeleted = adoptionService.deleteAdoption(id);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}