package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Pet;
import com.example.pet_adopting.requests.CreatePetRequest;
import com.example.pet_adopting.requests.UpdatePetRequest;
import com.example.pet_adopting.services.PetService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {
    private static final String UPLOAD_DIR = "uploads/";

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<Pet> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    public Pet getPetById(@PathVariable Long id) {
        return petService.getPetById(id);
    }

    @PostMapping
    public Pet createPet(@RequestBody CreatePetRequest pet) {
        return petService.createPet(pet);
    }

    @PutMapping("/{id}")
    public Pet updatePet(@PathVariable Long id, @RequestBody UpdatePetRequest pet) {
        return petService.updatePet(id, pet);
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id) {
        petService.deletePet(id);
    }
    // Yeni: Resim yükleme
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) throws IOException {
        // Resmi dosya sistemine kaydet
        String filePath = UPLOAD_DIR + file.getOriginalFilename();
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        file.transferTo(new File(filePath));

        // Pet'in imagePath alanını güncelle
        petService.updatePetImagePath(id, filePath);

        return ResponseEntity.ok("Image uploaded successfully for Pet ID: " + id);
    }

    // Yeni: Resmi getirme
    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getPetImage(@PathVariable Long id) throws IOException {
        // Pet'i al ve imagePath bilgisini kontrol et
        String imagePath = petService.getPetImagePath(id);
        File file = new File(imagePath);
        if (!file.exists()) {
            throw new RuntimeException("Image not found for Pet ID: " + id);
        }

        Resource resource = new UrlResource(file.toURI());
        String contentType = Files.probeContentType(Paths.get(imagePath));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}