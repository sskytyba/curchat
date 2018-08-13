package com.curchat.service;

import com.curchat.model.Chat;
import com.curchat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    public Optional<Chat> getChat(SimpMessageHeaderAccessor headerAccessor) {
        Long chatId = (Long) headerAccessor.getSessionAttributes().get("chatId");
        return chatRepository.findById(chatId);
    }
}
