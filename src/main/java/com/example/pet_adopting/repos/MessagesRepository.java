package com.example.pet_adopting.repos;


import com.example.pet_adopting.entities.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessagesRepository extends JpaRepository<Messages, Long> {

    @Query("SELECT m FROM Messages m WHERE (m.sender.id = :userId1 AND m.receiver.id = :userId2) " +
            "OR (m.sender.id = :userId2 AND m.receiver.id = :userId1) ORDER BY m.createdDate")
    List<Messages> findMessagesBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}