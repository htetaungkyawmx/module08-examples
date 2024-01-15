package com.spring.professional.exam.tutorial.module08.question02.service;

import com.google.common.collect.Sets;
import com.spring.professional.exam.tutorial.module08.question02.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question02.ds.Reservation;
import com.spring.professional.exam.tutorial.module08.question02.ds.Room;
import com.spring.professional.exam.tutorial.module08.question02.repository.ReservationRepository;
import com.spring.professional.exam.tutorial.module08.question02.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;

    public Optional<Room> findAvailableRoom(LocalDate date) {
        Set<Room> allRooms = roomRepository.findAll();
        Set<Reservation> reservationsOnDate = reservationRepository.findAllByReservationDate(date);

        Set<Room> roomsReservedAtDate = reservationsOnDate.stream()
                .map(Reservation::getRoom)
                .collect(Collectors.toSet());

        Set<Room> roomsAvailableAtDate = Sets.difference(allRooms, roomsReservedAtDate).immutableCopy();

        return roomsAvailableAtDate.stream()
                .findAny();
    }

    public Optional<Reservation> bookRoom(String roomName, Guest guest, LocalDate date) {
        return bookRoom(roomRepository.findByName(roomName).orElseThrow(), guest, date);
    }

    public Optional<Reservation> bookRoom(Room room, Guest guest, LocalDate date) {
        if (isRoomAvailableAtDate(room, date))
            return Optional.of(reservationRepository.save(
                    new Reservation(room, guest, date)
            ));
        else
            return Optional.empty();
    }

    private boolean isRoomAvailableAtDate(Room room, LocalDate date) {
        return !reservationRepository.existsByRoomAndReservationDate(room, date);
    }
}
