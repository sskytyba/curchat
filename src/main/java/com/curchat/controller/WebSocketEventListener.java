package com.curchat.controller;

import com.curchat.model.Chat;
import com.curchat.model.ChatMessage;
import com.curchat.model.UserAccount;
import com.curchat.repository.ChatMessageRepository;
import com.curchat.service.ChatService;
import com.curchat.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Optional<UserAccount> userAccount = userAccountService.getUserAccount(headerAccessor);
        Optional<Chat> chat = chatService.getChat(headerAccessor);
        if (userAccount.isPresent() && chat.isPresent()) {
            logger.info("User disconnected : " + userAccount.get().getName());

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(userAccount.get());
            chatMessage.setChat(chat.get());
            chatMessage = chatMessageRepository.save(chatMessage);

            template.convertAndSend("/topic/" + chat.get().getCurrency().getCurrencyCode() + "/message", chatMessage);
        }
    }
}