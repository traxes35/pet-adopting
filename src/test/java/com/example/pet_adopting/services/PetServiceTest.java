package com.example.pet_adopting.services;

import com.example.pet_adopting.entities.*;
        import com.example.pet_adopting.repos.PetRepository;
import com.example.pet_adopting.requests.CreatePetRequest;
import com.example.pet_adopting.requests.UpdatePetRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class PetServiceTest {
    private PetService petService;
    private PetRepository petRepository;
    private UserService userService;
    private CityService cityService;
    private TypeService typeService;
    private BreedService breedService;

    @Before
    public void setUp() throws Exception {
        petRepository = Mockito.mock(PetRepository.class);
        userService = Mockito.mock(UserService.class);
        cityService = Mockito.mock(CityService.class);
        typeService = Mockito.mock(TypeService.class);
        breedService = Mockito.mock(BreedService.class);
        petService = new PetService(petRepository, userService, cityService, typeService, breedService);
    }

    @Test
    public void whenCreatePetCalledWithValidRequest_itShouldReturnPet() {
        // Arrange
        CreatePetRequest createPetRequest = CreatePetRequest.builder()
                .cityId(1L)
                .breedId(1L)
                .typeId(1L)
                .name("test")
                .gender("male")
                .description("test description")
                .age(1)
                .isVaccinated(true)
                .isActive(true)
                .build();

        User user = User.builder()
                .id(1L)
                .name("Baris")
                .surname("Koc")
                .username("traxes")
                .password("qwert")
                .isActive(true)
                .verificationCode("12345")
                .verificationExpiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        City city = City.builder()
                .id(1L)
                .plate(35)
                .name("İzmir").build();

        Type type = Type.builder()
                .id(1L)
                .name("Dog")
                .build();

        Breed breed = Breed.builder()
                .id(1L)
                .type(type)
                .name("Golden").build();

        Pet pet = new Pet();
        pet.setUser(user);
        pet.setCity(city);
        pet.setType(type);
        pet.setBreed(breed);
        pet.setName(createPetRequest.getName());
        pet.setAge(createPetRequest.getAge());
        pet.setGender(createPetRequest.getGender());
        pet.setVaccinated(createPetRequest.isVaccinated());
        pet.setActive(createPetRequest.isActive());
        pet.setDescription(createPetRequest.getDescription());

        Mockito.when(userService.getOneUserbyId(1L)).thenReturn(user);
        Mockito.when(cityService.getOneCitybyId(1L)).thenReturn(city);
        Mockito.when(typeService.getOneTypeById(1L)).thenReturn(type);
        Mockito.when(breedService.getBreedById(1L)).thenReturn(breed);
        Mockito.when(petRepository.save(Mockito.any(Pet.class))).thenReturn(pet);

        // Act
        Pet result = petService.createPet(createPetRequest);

        // Assert
        assertNotNull(result);
        assertEquals(createPetRequest.getName(), result.getName());
        assertEquals(createPetRequest.getDescription(), result.getDescription());

        Mockito.verify(userService).getOneUserbyId(1L);
        Mockito.verify(cityService).getOneCitybyId(1L);
        Mockito.verify(typeService).getOneTypeById(1L);
        Mockito.verify(breedService).getBreedById(1L);
        Mockito.verify(petRepository).save(Mockito.any(Pet.class));
        System.out.println("selam");
    }
    @Test
    public void whenCreatePetCalledWithInvalidUser_itShouldReturnNull() {
        // Arrange
        CreatePetRequest createPetRequest = CreatePetRequest.builder()
                .cityId(1L)
                .breedId(1L)
                .typeId(1L)
                .name("test")
                .gender("male")
                .description("test description")
                .age(1)
                .isVaccinated(true)
                .isActive(true)
                .build();

        City city = City.builder()
                .id(1L)
                .plate(35)
                .name("İzmir").build();

        Type type = Type.builder()
                .id(1L)
                .name("Dog")
                .build();

        Breed breed = Breed.builder()
                .id(1L)
                .type(type)
                .name("Golden").build();

        Mockito.when(userService.getOneUserbyId(99L)).thenReturn(null); // Kullanıcı bulunamıyor
        Mockito.when(cityService.getOneCitybyId(1L)).thenReturn(city);
        Mockito.when(typeService.getOneTypeById(1L)).thenReturn(type);
        Mockito.when(breedService.getBreedById(1L)).thenReturn(breed);

        // Act
        Pet result = petService.createPet(createPetRequest);

        // Assert
        assertNull(result); // Null döndürülmesini bekliyoruz

        Mockito.verify(userService).getOneUserbyId(99L);
        Mockito.verifyNoInteractions(petRepository); // Pet kaydı yapılmamalı
    }
    @Test
    public void whenCreatePetCalledWithInvalidCity_itShouldReturnNull() {
        // Arrange
        CreatePetRequest createPetRequest = CreatePetRequest.builder()
                .cityId(99L) // Geçersiz şehir ID
                .breedId(1L)
                .typeId(1L)
                .name("test")
                .gender("male")
                .description("test description")
                .age(1)
                .isVaccinated(true)
                .isActive(true)
                .build();

        User user = User.builder()
                .id(1L)
                .name("Baris")
                .surname("Koc")
                .username("traxes")
                .password("qwert")
                .isActive(true)
                .verificationCode("12345")
                .verificationExpiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        Type type = Type.builder()
                .id(1L)
                .name("Dog")
                .build();

        Breed breed = Breed.builder()
                .id(1L)
                .type(type)
                .name("Golden").build();

        Mockito.when(userService.getOneUserbyId(1L)).thenReturn(user);
        Mockito.when(cityService.getOneCitybyId(99L)).thenReturn(null); // Şehir bulunamıyor
        Mockito.when(typeService.getOneTypeById(1L)).thenReturn(type);
        Mockito.when(breedService.getBreedById(1L)).thenReturn(breed);

        // Act
        Pet result = petService.createPet(createPetRequest);

        // Assert
        assertNull(result); // Null döndürülmesini bekliyoruz

        Mockito.verify(cityService).getOneCitybyId(99L);
        Mockito.verifyNoInteractions(petRepository); // Pet kaydı yapılmamalı
    }
    @Test
    public void whenCreatePetCalledWithInvalidType_itShouldReturnNull() {
        // Arrange
        CreatePetRequest createPetRequest = CreatePetRequest.builder()
                .cityId(1L)
                .breedId(1L)
                .typeId(99L) // Geçersiz tür ID
                .name("test")
                .gender("male")
                .description("test description")
                .age(1)
                .isVaccinated(true)
                .isActive(true)
                .build();

        User user = User.builder()
                .id(1L)
                .name("Baris")
                .surname("Koc")
                .username("traxes")
                .password("qwert")
                .isActive(true)
                .verificationCode("12345")
                .verificationExpiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        City city = City.builder()
                .id(1L)
                .plate(35)
                .name("İzmir").build();

        Breed breed = Breed.builder()
                .id(1L)
                .name("Golden").build();

        Mockito.when(userService.getOneUserbyId(1L)).thenReturn(user);
        Mockito.when(cityService.getOneCitybyId(1L)).thenReturn(city);
        Mockito.when(typeService.getOneTypeById(99L)).thenReturn(null); // Tür bulunamıyor
        Mockito.when(breedService.getBreedById(1L)).thenReturn(breed);

        // Act
        Pet result = petService.createPet(createPetRequest);

        // Assert
        assertNull(result); // Null döndürülmesini bekliyoruz

        Mockito.verify(typeService).getOneTypeById(99L);
        Mockito.verifyNoInteractions(petRepository); // Pet kaydı yapılmamalı
    }
    @Test
    public void whenGetPetCalledWithValidId_itShouldReturnPet() {
        // Arrange
        Long petId = 1L;

        Pet pet = new Pet();
        pet.setId(petId);
        pet.setName("Buddy");
        pet.setAge(2);
        pet.setGender("Male");
        pet.setDescription("Friendly dog");
        pet.setActive(true);

        Mockito.when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        // Act
        Pet result = petService.getPetById(petId);

        // Assert
        assertNotNull(result);
        assertEquals(petId, result.getId());
        assertEquals("Buddy", result.getName());
        assertEquals(2, result.getAge());
        Mockito.verify(petRepository).findById(petId);
    }
    @Test
    public void whenGetPetCalledWithInvalidId_itShouldReturnNull() {
        // Arrange
        Long invalidPetId = 99L;

        Mockito.when(petRepository.findById(invalidPetId)).thenReturn(Optional.empty());

        // Act
        Pet result = petService.getPetById(invalidPetId);

        // Assert
        assertNull(result);
        Mockito.verify(petRepository).findById(invalidPetId);
    }
    @Test
    public void whenGetAllPetsCalled_itShouldReturnPetList() {
        // Arrange
        Pet pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("Buddy");

        Pet pet2 = new Pet();
        pet2.setId(2L);
        pet2.setName("Max");

        List<Pet> petList = List.of(pet1, pet2);

        Mockito.when(petRepository.findAll()).thenReturn(petList);

        // Act
        List<Pet> result = petService.getAllPets();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Buddy", result.get(0).getName());
        assertEquals("Max", result.get(1).getName());
        Mockito.verify(petRepository).findAll();
    }
    @Test
    public void whenGetAllPetsCalledWithNoPets_itShouldReturnEmptyList() {
        // Arrange
        Mockito.when(petRepository.findAll()).thenReturn(List.of());

        // Act
        List<Pet> result = petService.getAllPets();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        Mockito.verify(petRepository).findAll();
    }
    @Test
    public void whenUpdatePetCalledWithValidId_itShouldUpdateAndReturnPet() {
        // Arrange
        Long petId = 1L;

        UpdatePetRequest updatePetRequest = new UpdatePetRequest();
        updatePetRequest.setName("Updated Name");
        updatePetRequest.setAge(5);
        updatePetRequest.setGender("Female");
        updatePetRequest.setVaccinated(true);
        updatePetRequest.setActive(false);
        updatePetRequest.setDescription("Updated description");

        Pet existingPet = new Pet();
        existingPet.setId(petId);
        existingPet.setName("Old Name");
        existingPet.setAge(3);
        existingPet.setGender("Male");
        existingPet.setVaccinated(false);
        existingPet.setActive(true);
        existingPet.setDescription("Old description");

        Pet updatedPet = new Pet();
        updatedPet.setId(petId);
        updatedPet.setName(updatePetRequest.getName());
        updatedPet.setAge(updatePetRequest.getAge());
        updatedPet.setGender(updatePetRequest.getGender());
        updatedPet.setVaccinated(updatePetRequest.isVaccinated());
        updatedPet.setActive(updatePetRequest.isActive());
        updatedPet.setDescription(updatePetRequest.getDescription());

        Mockito.when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        Mockito.when(petRepository.save(existingPet)).thenReturn(updatedPet);

        // Act
        Pet result = petService.updatePet(petId, updatePetRequest);

        // Assert
        assertNotNull(result);
        assertEquals(updatePetRequest.getName(), result.getName());
        assertEquals(updatePetRequest.getAge(), result.getAge());
        assertEquals(updatePetRequest.getGender(), result.getGender());
        assertEquals(updatePetRequest.isVaccinated(), result.isVaccinated());
        assertEquals(updatePetRequest.isActive(), result.isActive());
        assertEquals(updatePetRequest.getDescription(), result.getDescription());

        Mockito.verify(petRepository).findById(petId);
        Mockito.verify(petRepository).save(existingPet);
    }
    @Test
    public void whenUpdatePetCalledWithInvalidId_itShouldReturnNull() {
        // Arrange
        Long invalidPetId = 99L;

        UpdatePetRequest updatePetRequest = new UpdatePetRequest();
        updatePetRequest.setName("Updated Name");
        updatePetRequest.setAge(5);
        updatePetRequest.setGender("Female");
        updatePetRequest.setVaccinated(true);
        updatePetRequest.setActive(false);
        updatePetRequest.setDescription("Updated description");

        Mockito.when(petRepository.findById(invalidPetId)).thenReturn(Optional.empty());

        // Act
        Pet result = petService.updatePet(invalidPetId, updatePetRequest);

        // Assert
        assertNull(result);

        Mockito.verify(petRepository).findById(invalidPetId);
        Mockito.verify(petRepository, Mockito.never()).save(Mockito.any(Pet.class)); // Save çağrılmamalı
    }
    @Test
    public void whenDeletePetCalledWithInvalidId_itShouldNotDeleteAnything() {
        // Arrange
        Long invalidPetId = 99L;

        Mockito.when(petRepository.findById(invalidPetId)).thenReturn(Optional.empty());

        // Act
        petService.deletePet(invalidPetId);

        // Assert
        Mockito.verify(petRepository).findById(invalidPetId); // ID ile findById çağrılmalı
        Mockito.verify(petRepository, Mockito.never()).deleteById(Mockito.anyLong()); // Silme çağrılmamalı
    }
    @Test
    public void whenDeletePetCalledWithInvalidId_itShouldDoNothing() {
        // Arrange
        Long invalidPetId = 99L;

        Mockito.when(petRepository.existsById(invalidPetId)).thenReturn(false);

        // Act
        petService.deletePet(invalidPetId);

        // Assert
        Mockito.verify(petRepository, Mockito.never()).deleteById(invalidPetId); // Silme işlemi çağrılmamalı
    }
}