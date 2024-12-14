package com.example.pet_adopting.services;

import com.example.pet_adopting.entities.City;
import com.example.pet_adopting.repos.CityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class CityServiceTest {
    private CityService cityService;
    private CityRepository cityRepository;


    @Before
    public void setUp() throws Exception {
        cityRepository = Mockito.mock(CityRepository.class);
        cityService = new CityService(cityRepository);

    }

    @Test
    public void whenCreateCityCalledWithValidRequest_itShouldReturnCity() {
        // Arrange
        City city = City.builder()
                .id(1L)
                .name("Izmir")
                .plate(35)
                .build();

        Mockito.when(cityRepository.save(city)).thenReturn(city);

        // Act
        City result = cityService.saveOneCity(city);

        // Assert
        assertNotNull(result); // Dönen sonuç null olmamalı
        assertEquals(city, result); // Dönen sonuç, kaydedilen şehir olmalı

        // Verify
        Mockito.verify(cityRepository).save(city); // Save metodu doğru nesneyle çağrılmış mı
    }

    @Test
    public void whenCreateCityCalledWithNullRequest_itShouldThrowIllegalArgumentException() {
        // Arrange
        City nullCity = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cityService.saveOneCity(nullCity);
        });

        assertEquals("City cannot be null", exception.getMessage()); // Hata mesajını kontrol et
    }
    @Test
    public void whenGetCityCalledWithValidId_itShouldReturnCity() {
        // Arrange
        System.out.println("It call with Valid Id");
        Long cityId = 1L;
        City city = City.builder()
                .id(cityId)
                .name("Izmir")
                .plate(35)
                .build();

        Mockito.when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));

        // Act
        City result = cityService.getOneCitybyId(cityId);

        // Assert
        assertNotNull(result); // Sonuç null olmamalı
        assertEquals(city.getId(), result.getId()); // ID eşleşmeli
        assertEquals("Izmir", result.getName()); // İsim eşleşmeli
        assertEquals(35, result.getPlate()); // Plaka eşleşmeli

        // Verify
        Mockito.verify(cityRepository).findById(cityId); // findById çağrıldı mı kontrol et
        System.out.println("It returns true city");
    }
    @Test
    public void whenGetCityCalledWithInvalidId_itShouldReturnNull() {
        // Arrange
        Long invalidCityId = 99L;
        System.out.println("It call with Invalid Id");
        Mockito.when(cityRepository.findById(invalidCityId)).thenReturn(Optional.empty());

        // Act
        City result = cityService.getOneCitybyId(invalidCityId);

        // Assert
        assertNull(result); // Sonuç null olmalı

        // Verify
        Mockito.verify(cityRepository).findById(invalidCityId); // findById çağrıldı mı kontrol et
        System.out.println("It returns null");
    }
    @Test
    public void whenGetAllCitiesCalled_itShouldReturnCityList() {
        // Arrange
        City city1 = City.builder()
                .id(1L)
                .name("Izmir")
                .plate(35)
                .build();

        City city2 = City.builder()
                .id(2L)
                .name("Ankara")
                .plate(6)
                .build();

        List<City> cityList = List.of(city1, city2);

        Mockito.when(cityRepository.findAll()).thenReturn(cityList);

        // Act
        List<City> result = cityService.getAllCities();

        // Assert
        assertNotNull(result); // Sonuç null olmamalı
        assertEquals(2, result.size()); // Liste boyutu doğru olmalı
        assertEquals("Izmir", result.get(0).getName()); // İlk elemanın adı doğru olmalı
        assertEquals("Ankara", result.get(1).getName()); // İkinci elemanın adı doğru olmalı

        // Verify
        Mockito.verify(cityRepository).findAll(); // findAll çağrıldı mı kontrol et
        System.out.println("There are some cities and it returns that list");
    }
    @Test
    public void whenGetAllCitiesCalledWithNoCities_itShouldReturnEmptyList() {
        // Arrange
        Mockito.when(cityRepository.findAll()).thenReturn(List.of()); // Boş liste dön

        // Act
        List<City> result = cityService.getAllCities();

        // Assert
        assertNotNull(result); // Sonuç null olmamalı
        assertTrue(result.isEmpty()); // Liste boş olmalı

        // Verify
        Mockito.verify(cityRepository).findAll(); // findAll çağrıldı mı kontrol et
        System.out.println("There are no cities and it returns null");
    }
    @Test
    public void whenUpdateCityCalledWithValidId_itShouldUpdateAndReturnCity() {
        // Arrange
        Long cityId = 1L;

        City updatedCity = City.builder()
                .id(cityId)
                .name("Updated Izmir")
                .plate(35)
                .build();

        City existingCity = City.builder()
                .id(cityId)
                .name("Izmir")
                .plate(35)
                .build();

        Mockito.when(cityRepository.findById(cityId)).thenReturn(Optional.of(existingCity));
        Mockito.when(cityRepository.save(existingCity)).thenReturn(updatedCity);

        // Act
        City result = cityService.updateOneCity(cityId, updatedCity);

        // Assert
        assertNotNull(result); // Sonuç null olmamalı
        assertEquals(updatedCity.getName(), result.getName()); // Güncellenmiş isim eşleşmeli
        assertEquals(updatedCity.getPlate(), result.getPlate()); // Plaka eşleşmeli

        // Verify
        Mockito.verify(cityRepository).findById(cityId); // findById çağrılmış mı kontrol et
        Mockito.verify(cityRepository).save(existingCity); // save çağrılmış mı kontrol et
        System.out.println("They check id if it is same it updates");
    }
    @Test
    public void whenUpdateCityCalledWithInvalidId_itShouldReturnNull() {
        // Arrange
        Long invalidCityId = 99L;

        City updatedCity = City.builder()
                .id(invalidCityId)
                .name("Updated City")
                .plate(10)
                .build();

        Mockito.when(cityRepository.findById(invalidCityId)).thenReturn(Optional.empty());

        // Act
        City result = cityService.updateOneCity(invalidCityId, updatedCity);

        // Assert
        assertNull(result); // Sonuç null olmalı

        // Verify
        Mockito.verify(cityRepository).findById(invalidCityId); // findById çağrılmış mı kontrol et
        Mockito.verify(cityRepository, Mockito.never()).save(Mockito.any(City.class)); // save çağrılmamalı
        System.out.println("It check id's if it's different it returns null");
    }
    @Test
    public void whenDeleteCityCalledWithValidId_itShouldDeleteCity() {
        // Arrange
        Long cityId = 1L;

        Mockito.when(cityRepository.existsById(cityId)).thenReturn(true);
        Mockito.doNothing().when(cityRepository).deleteById(cityId);

        // Act
        cityService.deleteCityById(cityId);

        // Assert
        Mockito.verify(cityRepository).existsById(cityId); // existsById çağrılmış mı kontrol et
        Mockito.verify(cityRepository).deleteById(cityId); // deleteById çağrılmış mı kontrol et
        System.out.println("it checks id if there is a id it deletes");
    }
    @Test
    public void whenDeleteCityCalledWithInvalidId_itShouldDoNothing() {
        // Arrange
        Long invalidCityId = 99L;

        Mockito.when(cityRepository.existsById(invalidCityId)).thenReturn(false);

        // Act
        cityService.deleteCityById(invalidCityId);

        // Assert
        Mockito.verify(cityRepository).existsById(invalidCityId); // existsById çağrılmış mı kontrol et
        Mockito.verify(cityRepository, Mockito.never()).deleteById(invalidCityId); // deleteById çağrılmamalı
        System.out.println("it checks id if there is no id it don't delete");
    }
}