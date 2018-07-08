package com.curchat.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Currency;

@Entity
public class CurrencySale extends AbstractEntity {

    @ManyToOne
    UserAccount seller;

    @ManyToOne
    UserAccount buyer;

    Integer amount;

    Currency currencyBuy;

    Integer rate;

    Currency currencySale;

    CurrencySaleStatus status;
}
