package com.curchat.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
class AbstractEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
}
