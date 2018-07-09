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

    public UserAccount getSeller() {
        return seller;
    }

    public void setSeller(UserAccount seller) {
        this.seller = seller;
    }

    public UserAccount getBuyer() {
        return buyer;
    }

    public void setBuyer(UserAccount buyer) {
        this.buyer = buyer;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Currency getCurrencyBuy() {
        return currencyBuy;
    }

    public void setCurrencyBuy(Currency currencyBuy) {
        this.currencyBuy = currencyBuy;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Currency getCurrencySale() {
        return currencySale;
    }

    public void setCurrencySale(Currency currencySale) {
        this.currencySale = currencySale;
    }

    public CurrencySaleStatus getStatus() {
        return status;
    }

    public void setStatus(CurrencySaleStatus status) {
        this.status = status;
    }
}
