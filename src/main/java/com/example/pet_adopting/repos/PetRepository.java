package com.example.pet_adopting.repos;

import com.example.pet_adopting.entities.Pet;
import com.example.pet_adopting.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByUser(User user);

}