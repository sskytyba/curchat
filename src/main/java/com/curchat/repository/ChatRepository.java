package com.curchat.repository;

import com.curchat.model.Chat;
import org.springframework.data.repository.CrudRepository;

import java.util.Currency;

public interface ChatRepository extends CrudRepository<Chat, Long> {
    Chat findByCurrency(Currency currency);
}
