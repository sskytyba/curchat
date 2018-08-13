package com.curchat.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Currency;

@Getter
@Setter
@Entity
public class Chat extends AbstractEntity {
    private Currency currency;
}
