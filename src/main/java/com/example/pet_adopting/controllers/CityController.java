package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.City;
import com.example.pet_adopting.services.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {
CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCities() { // ResponseEntity<?> tipinde döndürme
        List<City> cities = cityService.getAllCities();

        if (cities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There is no added city");
        }
        return ResponseEntity.ok(cities); // Başarılı durumda 200 OK ile liste döndür
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCityById(@PathVariable Long id) {
        City city = cityService.getOneCitybyId(id);
        if (city == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("City with ID " + id + " not found.");
        }
        return ResponseEntity.ok(city);
    }
    @PostMapping
    public ResponseEntity<?> addCity(@RequestBody City city) {
        if (city.getName() == null || city.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("City name cannot be empty.");
        }
        City savedCity = cityService.saveOneCity(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCity);
    }
    @PutMapping("/{cityId}")
    public ResponseEntity<?> updateCity(@PathVariable Long cityId, @RequestBody City city) {
        City existingCity = cityService.getOneCitybyId(cityId);
        if (existingCity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("City with ID " + cityId + " not found.");
        }
        city.setId(cityId);
        City updatedCity = cityService.updateOneCity(cityId,city);
        return ResponseEntity.ok(updatedCity);
    }
    @DeleteMapping("/{cityId}")
    public void deleteCity(@PathVariable Long cityId) {
        City existingCity = cityService.getOneCitybyId(cityId);
        if (existingCity == null) {
             ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("City with ID " + cityId + " not found.");
        }
        cityService.deleteCityById(cityId);
        ResponseEntity.ok("City with ID " + cityId + " has been deleted successfully.");
    }
}


