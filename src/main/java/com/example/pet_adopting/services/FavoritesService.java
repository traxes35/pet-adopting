package com.example.pet_adopting.services;

import com.example.pet_adopting.entities.Favorites;
import com.example.pet_adopting.entities.Pet;
import com.example.pet_adopting.entities.User;
import com.example.pet_adopting.repos.FavoritesRepository;
import com.example.pet_adopting.requests.FavoritesRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoritesService {
    private final UserService userService;
    private final PetService petService;
    private final FavoritesRepository favoritesRepository;

    public FavoritesService(UserService userService, PetService petService, FavoritesRepository favoritesRepository) {
        this.userService = userService;
        this.petService = petService;
        this.favoritesRepository = favoritesRepository;
    }

    public List<Favorites> getAllFavorites(Optional<Long> userId, Optional<Long> petId) {
        if (userId.isPresent() && petId.isPresent()) {
            return favoritesRepository.findByUser_IdAndPet_Id(userId.get(), petId.get());
        } else if (userId.isPresent()) {
            return favoritesRepository.findByUser_Id(userId.get());
        } else if (petId.isPresent()) {
            return favoritesRepository.findByPet_Id(petId.get());
        } else {
            return favoritesRepository.findAll();
        }
    }

    public Optional<Favorites> getFavoritesById(Long favoritesId) {
    return favoritesRepository.findById(favoritesId);
    }

    public Favorites addFavorites(FavoritesRequest favorites) {
        User user =userService.getOneUserbyId(favorites.getUserId());
        Pet pet = petService.getPetById(favorites.getPetId());
        if (user !=null &&pet!=null) {
            Favorites favoritestoSave = new Favorites();
            favoritestoSave.setUser(user);
            favoritestoSave.setPet(pet);
            favoritesRepository.save(favoritestoSave);
        }
        return null;
    }

    public void deleteFavorites(Long favoritesId) {
    if (favoritesRepository.existsById(favoritesId)) {
    favoritesRepository.deleteById(favoritesId);
    }
}}
