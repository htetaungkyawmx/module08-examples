package com.spring.professional.exam.tutorial.module08.question07.repository;

import com.spring.professional.exam.tutorial.module08.question07.ds.Reservation;
import com.spring.professional.exam.tutorial.module08.question07.ds.Room;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ReservationRepository extends CrudRepository<Reservation, UUID> {

    List<Reservation> findAll();

    Set<Reservation> findAllByReservationDate(LocalDate reservationDate);

    boolean existsByRoomAndReservationDate(Room room, LocalDate reservationDate);
}
