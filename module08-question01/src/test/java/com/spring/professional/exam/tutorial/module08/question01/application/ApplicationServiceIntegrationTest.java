package com.spring.professional.exam.tutorial.module08.question01.application;

import com.spring.professional.exam.tutorial.module08.question01.configuration.ApplicationConfiguration;
import com.spring.professional.exam.tutorial.module08.question01.ds.BookingResult;
import com.spring.professional.exam.tutorial.module08.question01.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question01.ds.Reservation;
import com.spring.professional.exam.tutorial.module08.question01.ds.Room;
import com.spring.professional.exam.tutorial.module08.question01.repository.ReservationRepository;
import com.spring.professional.exam.tutorial.module08.question01.repository.RoomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Set;

import static com.spring.professional.exam.tutorial.module08.question01.configuration.DefaultDataConfiguration.*;
import static com.spring.professional.exam.tutorial.module08.question01.ds.BookingResult.BookingState.NO_ROOM_AVAILABLE;
import static com.spring.professional.exam.tutorial.module08.question01.ds.BookingResult.BookingState.ROOM_BOOKED;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class ApplicationServiceIntegrationTest {

    private static final String JOHN = "John";
    private static final String DOE = "Doe";
    private static final LocalDate DATE_2020_JULY_20 = LocalDate.of(2020, 7, 20);

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DirtiesContext
    public void shouldBookAnyRoomForNewGuest() {
        BookingResult bookingResult = applicationService.bookAnyRoomForNewGuest(JOHN, DOE, DATE_2020_JULY_20);

        assertReservationAcceptedAndSaved(bookingResult, DATE_2020_JULY_20);
        assertGuestRegisteredCorrectly(bookingResult, JOHN, DOE);
    }

    @Test
    @DirtiesContext
    public void shouldRegisterGuest() {
        Guest registerGuest = applicationService.registerGuest(JOHN, DOE);

        assertGuestRegisteredCorrectly(registerGuest, JOHN, DOE);
    }

    @Test
    @DirtiesContext
    public void shouldBookAnyRoomForRegisteredGuest() {
        Guest registerGuest = applicationService.registerGuest(JOHN, DOE);
        BookingResult bookingResult = applicationService.bookAnyRoomForRegisteredGuest(registerGuest, DATE_2020_JULY_20);

        assertReservationAcceptedAndSaved(bookingResult, DATE_2020_JULY_20);
    }

    @Test
    @DirtiesContext
    public void shouldRejectReservationWhenNoRoomsAvailable() {
        roomRepository.deleteAll();

        BookingResult bookingResult = applicationService.bookAnyRoomForNewGuest(JOHN, DOE, DATE_2020_JULY_20);

        assertReservationRejected(bookingResult);
    }

    @Test
    @DirtiesContext
    public void shouldRejectReservationWhenAllRoomsBooked() {
        Guest registerGuest = applicationService.registerGuest(JOHN, DOE);

        BookingResult bookingResult1 = applicationService.bookSpecificRoomForRegisteredGuest(registerGuest, GREEN_ROOM, DATE_2020_JULY_20);
        BookingResult bookingResult2 = applicationService.bookSpecificRoomForRegisteredGuest(registerGuest, YELLOW_ROOM, DATE_2020_JULY_20);
        BookingResult bookingResult3 = applicationService.bookSpecificRoomForRegisteredGuest(registerGuest, BLUE_ROOM, DATE_2020_JULY_20);

        BookingResult bookingResult = applicationService.bookAnyRoomForRegisteredGuest(registerGuest, DATE_2020_JULY_20);

        assertReservationRejected(bookingResult);

        assertReservationForSpecificRoom(bookingResult1, GREEN_ROOM);
        assertReservationForSpecificRoom(bookingResult2, YELLOW_ROOM);
        assertReservationForSpecificRoom(bookingResult3, BLUE_ROOM);

        assertReservationAcceptedAndSaved(bookingResult1, DATE_2020_JULY_20);
        assertReservationAcceptedAndSaved(bookingResult2, DATE_2020_JULY_20);
        assertReservationAcceptedAndSaved(bookingResult3, DATE_2020_JULY_20);
    }

    @Test
    @DirtiesContext
    public void shouldBookSpecificRoom() {
        Guest registerGuest = applicationService.registerGuest(JOHN, DOE);
        BookingResult bookingResult = applicationService.bookSpecificRoomForRegisteredGuest(registerGuest, YELLOW_ROOM, DATE_2020_JULY_20);

        assertReservationAccepted(bookingResult);
        assertReservationForSpecificRoom(bookingResult, YELLOW_ROOM);
        assetReservationAtDate(bookingResult, DATE_2020_JULY_20);
        assertReservationSaved(bookingResult, getReservationFromRepository(bookingResult));
    }

    private void assertReservationAcceptedAndSaved(BookingResult bookingResult, LocalDate date) {
        assertReservationAccepted(bookingResult);
        assetReservationAtDate(bookingResult, date);
        assertRoomReservedFromAvailableRooms(bookingResult, roomRepository.findAll());
        assertReservationSaved(bookingResult, getReservationFromRepository(bookingResult));
    }

    private void assertReservationAccepted(BookingResult bookingResult) {
        assertEquals(ROOM_BOOKED, bookingResult.getBookingState());
        assertTrue(bookingResult.getReservation().isPresent());
        assertNotNull(bookingResult.getReservation().get().getId());
    }

    private void assertReservationRejected(BookingResult bookingResult) {
        assertEquals(NO_ROOM_AVAILABLE, bookingResult.getBookingState());
        assertFalse(bookingResult.getReservation().isPresent());
    }

    private void assetReservationAtDate(BookingResult bookingResult, LocalDate expectedReservationDate) {
        LocalDate reservationDate = bookingResult.getReservation().orElseThrow().getReservationDate();

        assertEquals(expectedReservationDate, reservationDate);
    }

    private void assertGuestRegisteredCorrectly(BookingResult bookingResult, String firstName, String lastName) {
        Guest guest = bookingResult.getReservation().orElseThrow().getGuest();

        assertGuestRegisteredCorrectly(guest, firstName, lastName);
    }

    private void assertGuestRegisteredCorrectly(Guest guest, String firstName, String lastName) {
        assertNotNull(guest.getId());
        assertEquals(firstName, guest.getFirstName());
        assertEquals(lastName, guest.getLastName());
    }

    private void assertRoomReservedFromAvailableRooms(BookingResult bookingResult, Set<Room> availableRooms) {
        Room reservedRoom = bookingResult.getReservation().orElseThrow().getRoom();

        assertThat(availableRooms)
                .contains(reservedRoom);
    }

    private Reservation getReservationFromRepository(BookingResult bookingResult) {
        return reservationRepository.findById(bookingResult.getReservation().orElseThrow().getId()).orElseThrow();
    }

    private void assertReservationSaved(BookingResult bookingResult, Reservation savedReservation) {
        assertEquals(bookingResult.getReservation().orElseThrow(), savedReservation);
    }

    private void assertReservationForSpecificRoom(BookingResult bookingResult, String roomName) {
        assertEquals(roomName, bookingResult.getReservation().orElseThrow().getRoom().getName());
    }
}