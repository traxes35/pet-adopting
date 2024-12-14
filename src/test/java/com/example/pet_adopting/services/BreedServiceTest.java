package com.example.pet_adopting.services;

import com.example.pet_adopting.entities.Breed;
import com.example.pet_adopting.entities.Type;
import com.example.pet_adopting.repos.BreedRepository;
import com.example.pet_adopting.requests.CreateBreedRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class BreedServiceTest {

    private BreedService breedService;
    private BreedRepository breedRepository;
    private TypeService typeService;

    @Before
    public void setUp() {
        breedRepository = Mockito.mock(BreedRepository.class);
        typeService = Mockito.mock(TypeService.class);
        breedService = new BreedService(breedRepository, typeService);
    }

    @Test
    public void whenCreateBreedCalledWithValidRequest_itShouldReturnBreed() {
        System.out.println("It checks if the request is valid and returns the created Breed");

        // Arrange
        CreateBreedRequest request = CreateBreedRequest.builder()
                .name("Golden Retriever")
                .typeId(1L)
                .build();

        Type type = Type.builder()
                .id(1L)
                .name("Dog")
                .build();

        Breed breed = Breed.builder()
                .name(request.getName())
                .type(type)
                .build();

        Mockito.when(typeService.getOneTypeById(request.getTypeId())).thenReturn(type);
        Mockito.when(breedRepository.save(Mockito.any(Breed.class))).thenReturn(breed);

        // Act
        Breed result = breedService.createBreed(request);

        // Assert
        assertNotNull(result); // Result should not be null
        assertEquals("Golden Retriever", result.getName());
        assertEquals(1L, (long) result.getType().getId());

        Mockito.verify(typeService).getOneTypeById(request.getTypeId());
        Mockito.verify(breedRepository).save(Mockito.any(Breed.class));
        System.out.println("Breed creation was successful and verified.");
    }

    @Test
    public void whenCreateBreedCalledWithInvalidType_itShouldReturnNull() {
        System.out.println("It checks if the Type ID is invalid and returns null");

        // Arrange
        CreateBreedRequest createBreedRequest = CreateBreedRequest.builder()
                .name("Golden Retriever")
                .typeId(99L) // Geçersiz tür ID
                .build();

        Mockito.when(typeService.getOneTypeById(99L)).thenReturn(null); // Tür bulunamıyor

        // Act
        Breed result = breedService.createBreed(createBreedRequest);

        // Assert
        assertNull(result); // Null döndürülmesini bekliyoruz

        Mockito.verify(typeService).getOneTypeById(99L);
        Mockito.verifyNoInteractions(breedRepository); // Breed kaydı yapılmamalı
        System.out.println("Invalid Type ID was detected. Breed creation failed as expected.");
    }

    @Test
    public void whenGetBreedCalledWithValidId_itShouldReturnBreed() {
        System.out.println("It checks if a valid ID is provided and returns the corresponding Breed");

        // Arrange
        Breed breed = Breed.builder()
                .id(1L)
                .name("Golden Retriever")
                .build();

        Mockito.when(breedRepository.findById(1L)).thenReturn(Optional.of(breed));

        // Act
        Breed result = breedService.getBreedById(1L);

        // Assert
        assertNotNull(result); // Result should not be null
        assertEquals(1L, (long) result.getId());
        assertEquals("Golden Retriever", result.getName());

        Mockito.verify(breedRepository).findById(1L);
        System.out.println("Breed retrieval was successful.");
    }

    @Test
    public void whenGetBreedCalledWithInvalidId_itShouldReturnNull() {
        System.out.println("It checks if an invalid ID is provided and returns null");

        // Arrange
        Mockito.when(breedRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Breed result = breedService.getBreedById(99L);

        // Assert
        assertNull(result); // Result should be null

        Mockito.verify(breedRepository).findById(99L);
        System.out.println("Invalid Breed ID was detected. No Breed was retrieved.");
    }

    @Test
    public void whenGetAllBreedsCalled_itShouldReturnBreedList() {
        System.out.println("It retrieves all breeds and checks the list");

        // Arrange
        Breed breed1 = Breed.builder()
                .id(1L)
                .name("Golden Retriever")
                .build();

        Breed breed2 = Breed.builder()
                .id(2L)
                .name("Labrador Retriever")
                .build();

        List<Breed> breedList = List.of(breed1, breed2);

        Mockito.when(breedRepository.findAll()).thenReturn(breedList);

        // Act
        List<Breed> result = breedService.getAllBreeds();

        // Assert
        assertNotNull(result); // Result should not be null
        assertEquals(2, result.size()); // List size should match
        assertEquals("Golden Retriever", result.get(0).getName());
        assertEquals("Labrador Retriever", result.get(1).getName());

        Mockito.verify(breedRepository).findAll();
        System.out.println("Breed list retrieval was successful.");
    }

    @Test
    public void whenDeleteBreedCalledWithValidId_itShouldDeleteSuccessfully() {
        System.out.println("It checks if the delete operation succeeds with a valid ID");

        // Arrange
        Long breedId = 1L;

        Mockito.doNothing().when(breedRepository).deleteById(breedId);

        // Act
        breedService.deleteBreed(breedId);

        // Assert
        Mockito.verify(breedRepository).deleteById(breedId); // Verify deletion
        System.out.println("Breed deletion was successful.");
    }
}
