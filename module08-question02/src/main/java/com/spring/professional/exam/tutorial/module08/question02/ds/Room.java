package com.spring.professional.exam.tutorial.module08.question02.ds;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode
@ToString
public class Room {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true)
    private String name;
    private String section;

    @SuppressWarnings("unused")
    Room() {
    }

    public Room(String name, String section) {
        this.name = name;
        this.section = section;
    }
}
