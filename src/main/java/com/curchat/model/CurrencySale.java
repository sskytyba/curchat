package com.curchat.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Currency;

@Entity
@Getter
@Setter
public class CurrencySale extends AbstractEntity {

    private CurrencySaleStatus status = CurrencySaleStatus.NEW;

    @ManyToOne
    private UserAccount seller;

    @ManyToOne
    private UserAccount buyer;

    private Integer amount;

    private Currency currencyBuy;

    private Double rate;

    @OneToOne
    private ChatMessage message;
}
