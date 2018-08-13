package com.curchat.service;

import com.curchat.model.UserAccount;
import com.curchat.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountService {
    @Autowired
    private UserAccountRepository userAccountRepository;

    public Optional<UserAccount> getUserAccount(SimpMessageHeaderAccessor headerAccessor) {
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        return userAccountRepository.findById(userId);
    }
}
