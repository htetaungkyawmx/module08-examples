package com.spring.professional.exam.tutorial.module08.question08.ds;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"room_id", "reservationDate"})})
@Getter
@EqualsAndHashCode
@ToString
public class Reservation {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne
    private Guest guest;
    private LocalDate reservationDate;

    @SuppressWarnings("unused")
    Reservation() {
    }

    public Reservation(Room room, Guest guest, LocalDate reservationDate) {
        this.room = room;
        this.guest = guest;
        this.reservationDate = reservationDate;
    }
}
