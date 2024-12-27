package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.City;
import com.example.pet_adopting.services.CityService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CityControllerTest {

    private CityController cityController;
    private CityService cityService;

    @Before
    public void setUp() {
        cityService = Mockito.mock(CityService.class);
        cityController = new CityController(cityService);
    }

    @Test
    public void whenGetAllCitiesCalled_andCitiesExist_itShouldReturnCityList() {
        // Arrange
        City city1 = new City();
        city1.setId(1L);
        city1.setName("New York");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Los Angeles");

        List<City> cities = List.of(city1, city2);
        when(cityService.getAllCities()).thenReturn(cities);

        // Act
        ResponseEntity<?> response = cityController.getAllCities();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cities, response.getBody());
        verify(cityService).getAllCities();
        System.out.println("Test Passed: Successfully fetched all cities.");
    }

    @Test
    public void whenGetAllCitiesCalled_andNoCitiesExist_itShouldReturnNotFound() {
        // Arrange
        when(cityService.getAllCities()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<?> response = cityController.getAllCities();

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("There is no added city", response.getBody());
        verify(cityService).getAllCities();
        System.out.println("Test Passed: No cities found as expected.");
    }

    @Test
    public void whenGetCityByIdCalledWithValidId_itShouldReturnCity() {
        // Arrange
        Long cityId = 1L;
        City city = new City();
        city.setId(cityId);
        city.setName("New York");

        when(cityService.getOneCitybyId(cityId)).thenReturn(city);

        // Act
        ResponseEntity<?> response = cityController.getCityById(cityId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(city, response.getBody());
        verify(cityService).getOneCitybyId(cityId);
        System.out.println("Test Passed: Successfully fetched city with ID " + cityId + ".");
    }

    @Test
    public void whenGetCityByIdCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long cityId = 99L;
        when(cityService.getOneCitybyId(cityId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = cityController.getCityById(cityId);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("City with ID 99 not found.", response.getBody());
        verify(cityService).getOneCitybyId(cityId);
        System.out.println("Test Passed: No city found with ID " + cityId + " as expected.");
    }

    @Test
    public void whenAddCityCalledWithValidCity_itShouldReturnCreatedCity() {
        // Arrange
        City city = new City();
        city.setName("New York");

        City savedCity = new City();
        savedCity.setId(1L);
        savedCity.setName(city.getName());

        when(cityService.saveOneCity(city)).thenReturn(savedCity);

        // Act
        ResponseEntity<?> response = cityController.addCity(city);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(savedCity, response.getBody());
        verify(cityService).saveOneCity(city);
        System.out.println("Test Passed: Successfully added city: " + city.getName() + ".");
    }

    @Test
    public void whenAddCityCalledWithInvalidCity_itShouldReturnBadRequest() {
        // Arrange
        City city = new City(); // Name is null

        // Act
        ResponseEntity<?> response = cityController.addCity(city);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("City name cannot be empty.", response.getBody());
        verify(cityService, never()).saveOneCity(city);
        System.out.println("Test Passed: Attempted to add invalid city and received Bad Request as expected.");
    }

    @Test
    public void whenUpdateCityCalledWithValidId_itShouldReturnUpdatedCity() {
        // Arrange
        Long cityId = 1L;
        City city = new City();
        city.setName("Updated New York");

        City existingCity = new City();
        existingCity.setId(cityId);
        existingCity.setName("New York");

        City updatedCity = new City();
        updatedCity.setId(cityId);
        updatedCity.setName(city.getName());

        when(cityService.getOneCitybyId(cityId)).thenReturn(existingCity);
        when(cityService.updateOneCity(cityId, city)).thenReturn(updatedCity);

        // Act
        ResponseEntity<?> response = cityController.updateCity(cityId, city);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedCity, response.getBody());
        verify(cityService).getOneCitybyId(cityId);
        verify(cityService).updateOneCity(cityId, city);
        System.out.println("Test Passed: Successfully updated city with ID " + cityId + ".");
    }

    @Test
    public void whenUpdateCityCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long cityId = 99L;
        City city = new City();
        city.setName("Non-existent City");

        when(cityService.getOneCitybyId(cityId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = cityController.updateCity(cityId, city);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("City with ID 99 not found.", response.getBody());
        verify(cityService).getOneCitybyId(cityId);
        verify(cityService, never()).updateOneCity(cityId, city);
        System.out.println("Test Passed: No city found to update with ID " + cityId + " as expected.");
    }

    @Test
    public void whenDeleteCityCalledWithValidId_itShouldReturnNoContent() {
        // Arrange
        Long cityId = 1L;
        City city = new City();
        city.setId(cityId);

        when(cityService.getOneCitybyId(cityId)).thenReturn(city);

        // Act
        cityController.deleteCity(cityId);

        // Assert
        verify(cityService).getOneCitybyId(cityId);
        verify(cityService).deleteCityById(cityId);
        System.out.println("Test Passed: Successfully deleted city with ID " + cityId + ".");
    }

    @Test
    public void whenDeleteCityCalledWithInvalidId_itShouldReturnNotFound() {
        // Arrange
        Long cityId = 99L;
        when(cityService.getOneCitybyId(cityId)).thenReturn(null);

        // Act
        cityController.deleteCity(cityId);

        // Assert
        verify(cityService).getOneCitybyId(cityId);
        verify(cityService, never()).deleteCityById(cityId);
        System.out.println("Test Passed: No city found to delete with ID " + cityId + " as expected.");
    }
}

