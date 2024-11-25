package com.example.pet_adopting.repos;

import com.example.pet_adopting.entities.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    // Burada custom query'ler yazabiliriz, ancak şu anda sadece temel CRUD işlemleri yeterli
}