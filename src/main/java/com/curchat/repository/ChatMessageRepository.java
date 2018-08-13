package com.curchat.repository;

import com.curchat.model.Chat;
import com.curchat.model.ChatMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findByChat(Chat chat);
}
