package com.curchat.service;

import com.curchat.model.Chat;
import com.curchat.model.ChatMessage;
import com.curchat.model.CurrencySaleStatus;
import com.curchat.model.UserAccount;
import com.curchat.repository.ChatMessageRepository;
import com.curchat.repository.CurrencySaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;
import java.util.Optional;

@Service
public class ChatMessageService {
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private CurrencySaleRepository currencySaleRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ChatMessage saveMessage(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Optional<UserAccount> userAccount = userAccountService.getUserAccount(headerAccessor);
        Optional<Chat> chat = chatService.getChat(headerAccessor);

        if (!chat.isPresent() || !userAccount.isPresent()) {
            throw new Exception("Chat or user not found");
        }

        message.setChat(chat.get());
        message.setSender(userAccount.get());
        if (message.getType() == null) {
            throw new Exception("Message type is null");
        }
        if (message.getType().equals(ChatMessage.MessageType.SALE)) {
            message.getSale().setSeller(userAccount.get());
            message.getSale().setCurrencyBuy(Currency.getInstance(
                    message.getSale().getCurrencyBuy().getCurrencyCode()));
        }
        if (message.getType().equals(ChatMessage.MessageType.PURCHASE)) {
            message.getSale().setBuyer(userAccount.get());
            message.getSale().setCurrencyBuy(Currency.getInstance(
                    message.getSale().getCurrencyBuy().getCurrencyCode()));
        }
        message = chatMessageRepository.save(message);
        return message;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void buySell(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Optional<UserAccount> userAccount = userAccountService.getUserAccount(headerAccessor);
        Optional<Chat> chat = chatService.getChat(headerAccessor);

        if (!chat.isPresent() || !userAccount.isPresent()) {
            throw new Exception("Chat or user not found");
        }

        ChatMessage target = chatMessageRepository.findById(chatMessage.getId()).get();
        if (target.getSale().getStatus() != CurrencySaleStatus.NEW) {
            return;
        }
        if (target.getType() == ChatMessage.MessageType.SALE) {
            currencySaleRepository.sale(userAccount.get(), target.getSale().getId());
        } else if(target.getType() == ChatMessage.MessageType.PURCHASE) {
            currencySaleRepository.purchase(userAccount.get(), target.getSale().getId());
        } else {
            throw new Exception("Wrong message type: " + target.getType());
        }
    }
}
