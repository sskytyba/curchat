package com.curchat.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity
public class ChatMessage extends AbstractEntity {
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        SALE,
        PURCHASE,
    }

    private MessageType type;

    private String content;

    @ManyToOne
    private UserAccount sender;

    @OneToOne(cascade = CascadeType.ALL)
    private CurrencySale sale;

    @ManyToOne
    private Chat chat;
}
