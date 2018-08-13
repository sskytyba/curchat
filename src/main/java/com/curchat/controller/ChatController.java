package com.curchat.controller;

import com.curchat.model.Chat;
import com.curchat.model.ChatMessage;
import com.curchat.model.UserAccount;
import com.curchat.repository.ChatMessageRepository;
import com.curchat.repository.ChatRepository;
import com.curchat.repository.UserAccountRepository;
import com.curchat.service.ChatMessageService;
import com.curchat.service.ChatService;
import com.curchat.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Currency;
import java.util.List;

@Controller
public class ChatController {

    private static Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage,
                            SimpMessageHeaderAccessor headerAccessor) throws Exception {
        logger.info("Send message to all users");
        chatMessage = chatMessageService.saveMessage(chatMessage, headerAccessor);
        template.convertAndSend("/topic/" + chatService.getChat(headerAccessor).get()
                .getCurrency().getCurrencyCode() + "/message", chatMessage);
    }

    @MessageMapping("/chat.buySell")
    public void buySell(@Payload ChatMessage chatMessage,
                            SimpMessageHeaderAccessor headerAccessor) throws Exception {
        logger.info("Send status change to all users");
        chatMessageService.buySell(chatMessage, headerAccessor);
        template.convertAndSend("/topic/" + chatService.getChat(headerAccessor).get()
                .getCurrency().getCurrencyCode() + "/status", chatMessageRepository.findById(chatMessage.getId()));
    }

    @MessageMapping("/chat.addUser")
    @SendToUser("/queue/reply")
    public List<ChatMessage> login(@Payload ChatMessage chatMessage,
                                   SimpMessageHeaderAccessor headerAccessor) {
        UserAccount sender = chatMessage.getSender();
        logger.info("User connected : " + sender.getName());
        UserAccount userAccount = userAccountRepository.findByName(sender.getName());
        if (userAccount == null) {
            userAccount = userAccountRepository.save(sender);
        }
        Currency currency = Currency.getInstance(chatMessage.getContent());
        Chat chat = chatRepository.findByCurrency(currency);
        if (chat == null) {
            chat = new Chat();
            chat.setCurrency(currency);
            chat = chatRepository.save(chat);
        }
        headerAccessor.getSessionAttributes().put("userId", userAccount.getId());
        headerAccessor.getSessionAttributes().put("chatId", chat.getId());
        chatMessage.setChat(chat);
        chatMessage.setSender(userAccount);
        chatMessage = chatMessageRepository.save(chatMessage);
        template.convertAndSend("/topic/" + chat.getCurrency().getCurrencyCode() + "/message", chatMessage);
        return chatMessageRepository.findByChat(chat);
    }
}