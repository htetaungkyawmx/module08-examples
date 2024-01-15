package com.spring.professional.exam.tutorial.module08.question01.service;

import com.google.common.collect.Sets;
import com.spring.professional.exam.tutorial.module08.question01.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question01.ds.Reservation;
import com.spring.professional.exam.tutorial.module08.question01.ds.Room;
import com.spring.professional.exam.tutorial.module08.question01.repository.ReservationRepository;
import com.spring.professional.exam.tutorial.module08.question01.repository.RoomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest {

    private static final LocalDate DATE = LocalDate.of(2020, 7, 21);
    private static final LocalDate DIFFERENT_DATE = LocalDate.of(2020, 7, 22);
    private static final String ROOM_NAME = "SOME_ROOM";
    private static final String DIFFERENT_ROOM_NAME = "DIFFERENT_ROOM_NAME";

    @InjectMocks
    private BookingService bookingService;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private Room room1;
    @Mock
    private Room room2;
    @Mock
    private Room room3;
    @Mock
    private Guest guest;
    @Mock
    private Reservation reservation;

    @Test
    public void shouldFindAvailableRoomAtDateWhenAllRoomsAvailable() {
        mockRooms(room1, room2, room3);
        mockNoRoomsReservedAtDate(DATE);

        Optional<Room> availableRoom = bookingService.findAvailableRoom(DATE);

        assertTrue(availableRoom.isPresent());
        assertThat(availableRoom.get())
                .isIn(room1, room2, room3);
    }

    @Test
    public void shouldFindLastRoomWhenAllOthersAreReserved() {
        mockRooms(room1, room2, room3);
        mockRoomsReservedAtDate(DATE, room1, room2);

        Optional<Room> availableRoom = bookingService.findAvailableRoom(DATE);

        assertTrue(availableRoom.isPresent());
        assertSame(room3, availableRoom.get());
    }

    @Test
    public void shouldFindRoomWhenAllReservedButOnDifferentDate() {
        mockRooms(room1, room2, room3);
        mockRoomsReservedAtDate(DIFFERENT_DATE, room1, room2, room3);

        Optional<Room> availableRoom = bookingService.findAvailableRoom(DATE);

        assertTrue(availableRoom.isPresent());
        assertThat(availableRoom.get())
                .isIn(room1, room2, room3);
    }

    @Test
    public void shouldNotFindRoomWhenAllReserved() {
        mockRooms(room1, room2, room3);
        mockRoomsReservedAtDate(DATE, room1, room2, room3);

        Optional<Room> availableRoom = bookingService.findAvailableRoom(DATE);

        assertFalse(availableRoom.isPresent());
    }

    @Test
    public void shouldReserveRoomByNameForGuestAtDate() {
        mockRoomSearchByName(ROOM_NAME, room1);
        mockReservationDoesNotExistsForRoomAtDate(room1, DATE);
        mockReservationSaved(room1, guest, DATE, reservation);

        Optional<Reservation> reservationResult = bookingService.bookRoom(ROOM_NAME, guest, DATE);

        assertTrue(reservationResult.isPresent());
        assertSame(reservation, reservationResult.get());
    }

    @Test
    public void shouldNotReserveRoomByNameWhenRoomAlreadyReserved() {
        mockRoomSearchByName(ROOM_NAME, room1);
        mockReservationExistsForRoomAtDate(room1, DATE);

        Optional<Reservation> reservationResult = bookingService.bookRoom(ROOM_NAME, guest, DATE);

        verifyReservationNotSaved();

        assertFalse(reservationResult.isPresent());
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenRoomByNameNotFound() {
        mockRoomSearchByName(ROOM_NAME, room1);

        bookingService.bookRoom(DIFFERENT_ROOM_NAME, guest, DATE);
    }

    @Test
    public void shouldReserveRoomByNameForGuestAtDateWhenReservationExistsForDifferentDate() {
        mockRoomSearchByName(ROOM_NAME, room1);
        mockReservationDoesNotExistsForRoomAtDate(room1, DATE);
        mockReservationExistsForRoomAtDate(room1, DIFFERENT_DATE);
        mockReservationSaved(room1, guest, DATE, reservation);

        Optional<Reservation> reservationResult = bookingService.bookRoom(ROOM_NAME, guest, DATE);

        assertTrue(reservationResult.isPresent());
        assertSame(reservation, reservationResult.get());
    }

    @Test
    public void shouldReserveRoomForGuestAtDate() {
        mockReservationDoesNotExistsForRoomAtDate(room1, DATE);
        mockReservationSaved(room1, guest, DATE, reservation);

        Optional<Reservation> reservationResult = bookingService.bookRoom(room1, guest, DATE);

        assertTrue(reservationResult.isPresent());
        assertSame(reservation, reservationResult.get());
    }

    @Test
    public void shouldNotReserveRoomWhenRoomAlreadyReserved() {
        mockReservationExistsForRoomAtDate(room1, DATE);

        Optional<Reservation> reservationResult = bookingService.bookRoom(room1, guest, DATE);

        verifyReservationNotSaved();

        assertFalse(reservationResult.isPresent());
    }

    @Test
    public void shouldReserveRoomForGuestAtDateWhenReservationExistsForDifferentDate() {
        mockReservationDoesNotExistsForRoomAtDate(room1, DATE);
        mockReservationExistsForRoomAtDate(room1, DIFFERENT_DATE);
        mockReservationSaved(room1, guest, DATE, reservation);

        Optional<Reservation> reservationResult = bookingService.bookRoom(room1, guest, DATE);

        assertTrue(reservationResult.isPresent());
        assertSame(reservation, reservationResult.get());
    }

    private void mockRooms(Room... rooms) {
        when(roomRepository.findAll()).thenReturn(Sets.newHashSet(rooms));
    }

    private void mockRoomsReservedAtDate(LocalDate date, Room... rooms) {
        Set<Reservation> reservations = Arrays.stream(rooms)
                .map(this::mockReservationForRoom)
                .collect(Collectors.toSet());

        when(reservationRepository.findAllByReservationDate(date)).thenReturn(reservations);
    }

    private void mockNoRoomsReservedAtDate(LocalDate date) {
        when(reservationRepository.findAllByReservationDate(date)).thenReturn(Collections.emptySet());
    }

    private Reservation mockReservationForRoom(Room room) {
        Reservation reservation = Mockito.mock(Reservation.class);
        when(reservation.getRoom()).thenReturn(room);
        return reservation;
    }

    private void mockRoomSearchByName(String roomName, Room room) {
        when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
    }

    private void mockReservationExistsForRoomAtDate(Room room, LocalDate date) {
        when(reservationRepository.existsByRoomAndReservationDate(room, date)).thenReturn(true);
    }

    private void mockReservationDoesNotExistsForRoomAtDate(Room room, LocalDate date) {
        when(reservationRepository.existsByRoomAndReservationDate(room, date)).thenReturn(false);
    }

    private void mockReservationSaved(Room room, Guest guest, LocalDate date, Reservation reservation) {
        when(reservationRepository.save(new Reservation(room, guest, date))).thenReturn(reservation);
    }

    private void verifyReservationNotSaved() {
        verify(reservationRepository, never()).save(any());
    }
}