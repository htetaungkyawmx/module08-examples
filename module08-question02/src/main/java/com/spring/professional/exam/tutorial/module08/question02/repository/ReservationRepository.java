package com.spring.professional.exam.tutorial.module08.question02.repository;

import com.spring.professional.exam.tutorial.module08.question02.ds.Reservation;
import com.spring.professional.exam.tutorial.module08.question02.ds.Room;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public interface ReservationRepository extends CrudRepository<Reservation, UUID> {

    Set<Reservation> findAllByReservationDate(LocalDate reservationDate);

    boolean existsByRoomAndReservationDate(Room room, LocalDate reservationDate);
}
