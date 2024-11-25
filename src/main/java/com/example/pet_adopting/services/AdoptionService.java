package com.example.pet_adopting.services;

import com.example.pet_adopting.entities.Adoption;
import com.example.pet_adopting.entities.User;
import com.example.pet_adopting.entities.Pet;
import com.example.pet_adopting.repos.AdoptionRepository;
import com.example.pet_adopting.repos.UserRepository;
import com.example.pet_adopting.repos.PetRepository;
import com.example.pet_adopting.requests.CreateAdoptionRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    // Constructor Injection
    public AdoptionService(AdoptionRepository adoptionRepository, UserRepository userRepository, PetRepository petRepository) {
        this.adoptionRepository = adoptionRepository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    // Create a new adoption
    public Adoption createAdoption(CreateAdoptionRequest adoptionRequest) {
        User user = userRepository.findById(adoptionRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Pet pet = petRepository.findById(adoptionRequest.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        Adoption adoption = new Adoption();
        adoption.setUser(user);
        adoption.setPet(pet);

        return adoptionRepository.save(adoption);
    }

    // Get adoption by ID
    public Optional<Adoption> getAdoptionById(long id) {
        return adoptionRepository.findById(id);
    }

    // Update an existing adoption
    public Optional<Adoption> updateAdoption(long id, CreateAdoptionRequest adoptionRequest) {
        Optional<Adoption> existingAdoption = adoptionRepository.findById(id);

        if (existingAdoption.isPresent()) {
            Adoption adoption = existingAdoption.get();
            User user = userRepository.findById(adoptionRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Pet pet = petRepository.findById(adoptionRequest.getPetId())
                    .orElseThrow(() -> new RuntimeException("Pet not found"));

            adoption.setUser(user);
            adoption.setPet(pet);

            return Optional.of(adoptionRepository.save(adoption));
        }
        return Optional.empty();
    }

    // Delete an adoption by ID
    public boolean deleteAdoption(long id) {
        Optional<Adoption> adoption = adoptionRepository.findById(id);

        if (adoption.isPresent()) {
            adoptionRepository.delete(adoption.get());
            return true;
        }
        return false;
    }
}