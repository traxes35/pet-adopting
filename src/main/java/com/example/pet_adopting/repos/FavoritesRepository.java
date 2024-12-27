package com.example.pet_adopting.repos;

import com.example.pet_adopting.entities.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    List<Favorites> findByUser_IdAndPet_Id(Long userId, Long petId);

    List<Favorites> findByUserId(Long userId);

    List<Favorites> findByPet_Id(Long petId);
}
