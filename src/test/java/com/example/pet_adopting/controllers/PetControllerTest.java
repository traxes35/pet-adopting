package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Pet;
import com.example.pet_adopting.entities.User;
import com.example.pet_adopting.repos.UserRepository;
import com.example.pet_adopting.requests.CreatePetRequest;
import com.example.pet_adopting.requests.UpdatePetRequest;
import com.example.pet_adopting.services.PetService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PetControllerTest {

    @InjectMocks
    private PetController petController; // Test edilen sınıf

    @Mock
    private PetService petService; // Mocklanan bağımlılık

    @Mock
    private UserRepository userRepository; // Mocklanan bağımlılık

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mock nesnelerini başlatır
    }


    @Test
    public void whenGetPetByIdCalledWithValidId_itShouldReturnPet() {
        // Arrange
        Long petId = 1L;
        Pet pet = new Pet();
        pet.setId(petId);
        pet.setName("Buddy");
        pet.setAge(2);
        pet.setGender("Male");
        pet.setDescription("Friendly dog");
        pet.setActive(true);

        when(petService.getPetById(petId)).thenReturn(pet);

        // Act
        ResponseEntity<Pet> response = petController.getPetById(petId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pet, response.getBody());
        verify(petService).getPetById(petId);
        System.out.println("WE GOT" + petId + " pet id");
    }

    @Test
    public void whenGetPetByIdCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long petId = 99L;
        when(petService.getPetById(petId)).thenReturn(null);

        // Act
        ResponseEntity<Pet> response = petController.getPetById(petId);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(petService).getPetById(petId);
        System.out.println("There is no" + petId + " pet id ");

    }

    @Test
    public void whenCreatePetCalledWithValidRequest_itShouldReturnCreatedPet() {
        // Arrange
        CreatePetRequest createPetRequest = CreatePetRequest.builder()
                .cityId(1L)
                .breedId(1L)
                .typeId(1L)
                .name("Buddy")
                .gender("Male")
                .description("Friendly dog")
                .age(2)
                .isVaccinated(true)
                .isActive(true)
                .imagePath("/images/buddy.jpg")
                .build();

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Pet expectedPet = new Pet();
        expectedPet.setId(1L);
        expectedPet.setUser(user);
        expectedPet.setName("Buddy");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("testuser");

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        Mockito.when(petService.createPetForUser(Mockito.any(CreatePetRequest.class), Mockito.any(User.class))).thenReturn(expectedPet);

        // Act
        ResponseEntity<Pet> response = petController.createPet(createPetRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Buddy", response.getBody().getName());
        assertEquals(user, response.getBody().getUser());

        Mockito.verify(userRepository).findByUsername("testuser");
        Mockito.verify(petService).createPetForUser(Mockito.any(CreatePetRequest.class), Mockito.any(User.class));
    }

    @Test
    public void whenUpdatePetCalledWithValidId_itShouldReturnUpdatedPet() {
        // Arrange
        Long petId = 1L;
        UpdatePetRequest request = new UpdatePetRequest();
        request.setName("Updated Buddy");
        request.setAge(3);
        request.setGender("Male");
        request.setDescription("Updated description");

        Pet updatedPet = new Pet();
        updatedPet.setId(petId);
        updatedPet.setName(request.getName());
        updatedPet.setAge(request.getAge());
        updatedPet.setGender(request.getGender());
        updatedPet.setDescription(request.getDescription());

        when(petService.updatePet(petId, request)).thenReturn(updatedPet);

        // Act
        ResponseEntity<Pet> response = petController.updatePet(petId, request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedPet, response.getBody());
        verify(petService).updatePet(petId, request);
        System.out.println("PET UPDATED SUCCESFULLY");
    }
    @Test
    public void whenUpdatePetCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long petId = 99L;
        UpdatePetRequest request = new UpdatePetRequest();
        request.setName("Non-existing Pet");
        when(petService.updatePet(petId, request)).thenReturn(null);

        // Act
        ResponseEntity<Pet> response = petController.updatePet(petId, request);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(petService).updatePet(petId, request);
    }
    @Test
    public void whenDeletePetCalledWithValidId_itShouldReturnNoContent() {
        // Arrange
        Long petId = 1L;
        Pet pet = new Pet();
        pet.setId(petId);
        pet.setName("Buddy");

        Mockito.when(petService.getPetById(petId)).thenReturn(pet);

        // Act
        ResponseEntity<Void> response = petController.deletePet(petId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        Mockito.verify(petService).getPetById(petId);
        Mockito.verify(petService).deletePet(petId);
    }
    @Test
    public void whenDeletePetCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long petId = 99L;
        when(petService.getPetById(petId)).thenReturn(null); // Geçersiz ID için null döner

        // Act
        ResponseEntity<Void> response = petController.deletePet(petId);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(petService).getPetById(petId); // ID kontrolü yapılmış mı?
        verify(petService, never()).deletePet(petId); // Silme işlemi çağrılmamalı
    }
}
