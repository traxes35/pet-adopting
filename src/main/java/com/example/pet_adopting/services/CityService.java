package com.example.pet_adopting.services;
import com.example.pet_adopting.entities.City;
import com.example.pet_adopting.repos.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    private  CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public void deleteCityById(Long cityId) {
        if (cityRepository.existsById(cityId)) {
            cityRepository.deleteById(cityId);
        }
    }
    public List<City> getAllCities() {
    return cityRepository.findAll();
    }

    public City saveOneCity(City city) {
        if (city == null) {
            throw new IllegalArgumentException("City cannot be null");
        }
        return cityRepository.save(city);
    }
    public City getOneCitybyId(long cityId) {
        return cityRepository.findById(cityId).orElse(null);
    }

    public City updateOneCity(long cityId, City newCity) {
        Optional<City> City = cityRepository.findById(cityId);
        if(City.isPresent()) {
            City foundCity = City.get();
            foundCity.setName(newCity.getName());
            return cityRepository.save(foundCity);
        }else{
            return null;
        }
    }
}
