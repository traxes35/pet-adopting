package com.example.pet_adopting.controllers;

import com.example.pet_adopting.entities.Messages;
import com.example.pet_adopting.services.MessagesService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class MessagesController {

    private final MessagesService messagesService;

    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @MessageMapping("/sendMessage") // "/app/sendMessage" ile gelen mesajları dinler
    @SendTo("/topic/messages") // Mesajları "/topic/messages" abonelerine gönderir
    public Messages sendMessage(Messages message) {
        message.setCreatedDate(new Date()); // Mesajın gönderim zamanını ayarla
        return messagesService.saveMessage(message); // Mesajı veritabanına kaydet ve geri döndür
    }
}