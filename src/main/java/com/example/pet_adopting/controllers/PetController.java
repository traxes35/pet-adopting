package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Pet;
import com.example.pet_adopting.entities.User;
import com.example.pet_adopting.repos.UserRepository;
import com.example.pet_adopting.requests.CreatePetRequest;
import com.example.pet_adopting.requests.UpdatePetRequest;
import com.example.pet_adopting.services.PetService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pets")
public class PetController {

    private static final String UPLOAD_DIR = "uploads/";
    private final PetService petService;
    private final UserRepository userRepository;

    public PetController(PetService petService, UserRepository userRepository) {
        this.petService = petService;
        this.userRepository = userRepository;

    }

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petService.getAllPets();
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Pet pet = petService.getPetById(id);
        if (pet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(pet);
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody CreatePetRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kullanıcı doğrulama
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Giriş yapan kullanıcı bilgilerini al
        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Pet oluştur
        Pet createdPet = petService.createPetForUser(request, user.orElse(null));

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody UpdatePetRequest request) {
        Pet updatedPet = petService.updatePet(id, request);
        if (updatedPet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedPet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        Pet pet = petService.getPetById(id);
        if (pet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Geçersiz ID için 404 döner
        }
        petService.deletePet(id);
        return ResponseEntity.noContent().build(); // Başarılı silme için 204 döner
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