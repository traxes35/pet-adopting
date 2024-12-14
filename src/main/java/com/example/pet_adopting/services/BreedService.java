package com.example.pet_adopting.services;


import com.example.pet_adopting.entities.Breed;
import com.example.pet_adopting.entities.Type;
import com.example.pet_adopting.repos.BreedRepository;
import com.example.pet_adopting.requests.CreateBreedRequest;
import com.example.pet_adopting.requests.UpdateBreedRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BreedService {

    private final BreedRepository breedRepository;
    private final TypeService typeService;

    public BreedService(BreedRepository breedRepository, TypeService typeService) {
        this.breedRepository = breedRepository;
        this.typeService = typeService;
    }


    public List<Breed> getAllBreeds() {
        return breedRepository.findAll();
    }

    public Breed getBreedById(Long id) {
        return breedRepository.findById(id).orElse(null);
    }

    public Breed createBreed(CreateBreedRequest newBreed) {
        Type type = typeService.getOneTypeById(newBreed.getTypeId());
         if (type == null) {
             return null;
         }
    Breed toSave = new Breed();
         toSave.setName(newBreed.getName());
         toSave.setType(type);
         return breedRepository.save(toSave);
    }

    public Breed updateBreed(Long id, UpdateBreedRequest updatedBreed) {
        Optional<Breed> breed = breedRepository.findById(id);
        if (breed.isPresent()) {
        Breed breedToUpdate = breed.get();
        breedToUpdate.setName(updatedBreed.getName());
        return breedRepository.save(breedToUpdate);
        }
        return null;
    }

    public void deleteBreed(Long id) {
        breedRepository.deleteById(id);
    }
}