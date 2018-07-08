package com.curchat.model;

import javax.persistence.*;

@Entity
public class UserAccount extends AbstractEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
