package com.example.pet_adopting.services;

import com.example.pet_adopting.entities.*;
import com.example.pet_adopting.repos.PetRepository;
import com.example.pet_adopting.requests.CreatePetRequest;
import com.example.pet_adopting.requests.UpdatePetRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserService userService;
    private final CityService cityService;
    private final TypeService typeService;
    private final BreedService breedService;

    public PetService(PetRepository petRepository, UserService userService, CityService cityService,
                      TypeService typeService, BreedService breedService) {
        this.petRepository = petRepository;
        this.userService = userService;
        this.cityService = cityService;
        this.typeService = typeService;
        this.breedService = breedService;
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet getPetById(Long id) {
        return petRepository.findById(id).orElse(null);
    }

       /* public Pet createPet(CreatePetRequest newPet) {
            User user = userService.getOneUserbyId(newPet.getUserId());
            City city = cityService.getOneCitybyId(newPet.getCityId());
            Type type = typeService.getOneTypeById(newPet.getTypeId());
            Breed breed = breedService.getBreedById(newPet.getBreedId());

            if (user == null || city == null || type == null || breed == null) {
                return null;
            }

            Pet pet = new Pet();
            pet.setUser(user);
            pet.setCity(city);
            pet.setType(type);
            pet.setBreed(breed);
            pet.setName(newPet.getName());
            pet.setAge(newPet.getAge());
            pet.setGender(newPet.getGender());
            pet.setVaccinated(newPet.isVaccinated());
            pet.setActive(newPet.isActive());
            pet.setDescription(newPet.getDescription());
            pet.setImagePath(newPet.getImagePath());

            return petRepository.save(pet);
        }*/

    public Pet updatePet(Long id, UpdatePetRequest updatedPet) {
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isPresent()) {
            Pet petToUpdate = pet.get();
            petToUpdate.setName(updatedPet.getName());
            petToUpdate.setAge(updatedPet.getAge());
            petToUpdate.setGender(updatedPet.getGender());
            petToUpdate.setVaccinated(updatedPet.isVaccinated());
            petToUpdate.setActive(updatedPet.isActive());
            petToUpdate.setDescription(updatedPet.getDescription());
            return petRepository.save(petToUpdate);
        }
        return null;
    }

    public void deletePet(Long id) {
        Pet pet = petRepository.findById(id).orElse(null);
        if (pet != null) {
            petRepository.deleteById(id);
        }
    }

    public void updatePetImagePath(Long id, String imagePath) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        pet.setImagePath(imagePath);
        petRepository.save(pet);
    }

    public String getPetImagePath(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        return pet.getImagePath();
    }

    public Pet createPetForUser(CreatePetRequest newPet, User user) {
        if (user == null) {
            return null; // Kullanıcı null ise işlem yapılmaz
        }

        City city = cityService.getOneCitybyId(newPet.getCityId());
        Type type = typeService.getOneTypeById(newPet.getTypeId());
        Breed breed = breedService.getBreedById(newPet.getBreedId());

        if (city == null || type == null || breed == null) {
            return null;
        }

        // Yeni pet oluştur
        Pet pet = new Pet();
        pet.setUser(user); // Giriş yapan kullanıcıyı bağla
        pet.setCity(city);
        pet.setType(type);
        pet.setBreed(breed);
        pet.setName(newPet.getName());
        pet.setAge(newPet.getAge());
        pet.setGender(newPet.getGender());
        pet.setVaccinated(newPet.isVaccinated());
        pet.setActive(newPet.isActive());
        pet.setDescription(newPet.getDescription());
        pet.setImagePath(newPet.getImagePath());

        return petRepository.save(pet);
    }

    public List<Pet> getPetsByUser(User user) {
        return petRepository.findAllByUser(user);
    }
}