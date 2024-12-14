package com.example.pet_adopting.services;

import com.example.pet_adopting.entities.Messages;
import com.example.pet_adopting.repos.MessagesRepository;
import org.springframework.stereotype.Service;

@Service
public class MessagesService {

    private final MessagesRepository messagesRepository;

    public MessagesService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public Messages saveMessage(Messages message) {
        return messagesRepository.save(message); // Mesajı kaydet ve geri döndür
    }
}