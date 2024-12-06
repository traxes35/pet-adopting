package com.example.pet_adopting.repos;

import com.example.pet_adopting.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String verificationCode);
     Optional<User> findByUsername(String username);

}
