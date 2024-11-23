package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.City;
import com.example.pet_adopting.services.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cities")
public class CityController {
CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }
    @PostMapping
    public City createCity(@RequestBody City newCity) {
        return cityService.saveOneCity(newCity);
    }
    @GetMapping("/{cityId}")
    public Optional<City> getOneCity(@PathVariable long cityId) {
        return  cityService.getOneCitybyId(cityId);

    }
    @PutMapping("/{cityId}")
    public City updateOneCity(@PathVariable long cityId, @RequestBody City newCity) {
        return cityService.updateOneCity(cityId,newCity);
    }
    @DeleteMapping("/{cityId}")
    public void deleteCity(@PathVariable Long cityId){
        cityService.deleteCityById(cityId);
    }
}


