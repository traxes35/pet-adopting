package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Pet;
import com.example.pet_adopting.requests.CreatePetRequest;
import com.example.pet_adopting.requests.UpdatePetRequest;
import com.example.pet_adopting.services.PetService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PetControllerTest {

    private PetController petController;
    private PetService petService;

    @Before
    public void setUp() {
        petService = Mockito.mock(PetService.class);
        petController = new PetController(petService);
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
        CreatePetRequest request = new CreatePetRequest();
        request.setName("Buddy");
        request.setAge(2);
        request.setGender("Male");
        request.setDescription("Friendly dog");

        Pet createdPet = new Pet();
        createdPet.setId(1L);
        createdPet.setName(request.getName());
        createdPet.setAge(request.getAge());
        createdPet.setGender(request.getGender());
        createdPet.setDescription(request.getDescription());

        when(petService.createPet(request)).thenReturn(createdPet);

        // Act
        ResponseEntity<Pet> response = petController.createPet(request);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(createdPet, response.getBody());
        verify(petService).createPet(request);
        System.out.println("NEW PET CREATED");

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

        // Act
        ResponseEntity<Void> response = petController.deletePet(petId);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(petService).deletePet(petId);
        System.out.println("WE DELETED" + petId + " pet id");
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
