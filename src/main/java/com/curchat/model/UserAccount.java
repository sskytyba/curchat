package com.curchat.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class UserAccount extends AbstractEntity {
    private String name;
}
