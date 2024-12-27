package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Breed;
import com.example.pet_adopting.requests.CreateBreedRequest;
import com.example.pet_adopting.requests.UpdateBreedRequest;
import com.example.pet_adopting.services.BreedService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BreedControllerTest {

    private BreedController breedController;
    private BreedService breedService;

    @Before
    public void setUp() {
        breedService = Mockito.mock(BreedService.class);
        breedController = new BreedController(breedService);
    }

    @Test
    public void whenGetAllBreedsCalled_itShouldReturnBreedList() {
        // Arrange
        Breed breed1 = new Breed();
        breed1.setId(1L);
        breed1.setName("Labrador");

        Breed breed2 = new Breed();
        breed2.setId(2L);
        breed2.setName("Beagle");

        List<Breed> breeds = List.of(breed1, breed2);
        when(breedService.getAllBreeds()).thenReturn(breeds);

        // Act
        ResponseEntity<List<Breed>> response = breedController.getAllBreeds();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(breeds, response.getBody());
        verify(breedService).getAllBreeds();
    }

    @Test
    public void whenGetBreedByIdCalledWithValidId_itShouldReturnBreed() {
        // Arrange
        Long breedId = 1L;
        Breed breed = new Breed();
        breed.setId(breedId);
        breed.setName("Labrador");

        when(breedService.getBreedById(breedId)).thenReturn(breed);

        // Act
        ResponseEntity<Breed> response = breedController.getBreedById(breedId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(breed, response.getBody());
        verify(breedService).getBreedById(breedId);
    }

    @Test
    public void whenGetBreedByIdCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long breedId = 99L;
        when(breedService.getBreedById(breedId)).thenReturn(null);

        // Act
        ResponseEntity<Breed> response = breedController.getBreedById(breedId);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(breedService).getBreedById(breedId);
    }

    @Test
    public void whenCreateBreedCalledWithValidRequest_itShouldReturnCreatedBreed() {
        // Arrange
        CreateBreedRequest request = new CreateBreedRequest();
        request.setName("Golden Retriever");

        Breed createdBreed = new Breed();
        createdBreed.setId(1L);
        createdBreed.setName(request.getName());

        when(breedService.createBreed(request)).thenReturn(createdBreed);

        // Act
        ResponseEntity<Breed> response = breedController.createBreed(request);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(createdBreed, response.getBody());
        verify(breedService).createBreed(request);
    }

    @Test
    public void whenUpdateBreedCalledWithValidId_itShouldReturnUpdatedBreed() {
        // Arrange
        Long breedId = 1L;
        UpdateBreedRequest request = new UpdateBreedRequest();
        request.setName("Updated Labrador");

        Breed updatedBreed = new Breed();
        updatedBreed.setId(breedId);
        updatedBreed.setName(request.getName());

        when(breedService.updateBreed(breedId, request)).thenReturn(updatedBreed);

        // Act
        ResponseEntity<Breed> response = breedController.updateBreed(breedId, request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedBreed, response.getBody());
        verify(breedService).updateBreed(breedId, request);
    }

    @Test
    public void whenUpdateBreedCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long breedId = 99L;
        UpdateBreedRequest request = new UpdateBreedRequest();
        request.setName("Non-existent Breed");

        when(breedService.updateBreed(breedId, request)).thenReturn(null);

        // Act
        ResponseEntity<Breed> response = breedController.updateBreed(breedId, request);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(breedService).updateBreed(breedId, request);
    }

    @Test
    public void whenDeleteBreedCalledWithValidId_itShouldReturnNoContent() {
        // Arrange
        Long breedId = 1L;
        Breed breed = new Breed();
        breed.setId(breedId);

        when(breedService.getBreedById(breedId)).thenReturn(breed);

        // Act
        ResponseEntity<Void> response = breedController.deleteBreed(breedId);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(breedService).getBreedById(breedId);
        verify(breedService).deleteBreed(breedId);
    }

    @Test
    public void whenDeleteBreedCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long breedId = 99L;
        when(breedService.getBreedById(breedId)).thenReturn(null);

        // Act
        ResponseEntity<Void> response = breedController.deleteBreed(breedId);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(breedService).getBreedById(breedId);
        verify(breedService, never()).deleteBreed(breedId);
    }
}