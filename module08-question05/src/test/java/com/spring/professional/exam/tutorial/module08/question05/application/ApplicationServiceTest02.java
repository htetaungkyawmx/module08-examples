package com.spring.professional.exam.tutorial.module08.question05.application;

import com.spring.professional.exam.tutorial.module08.question05.ds.BookingResult;
import com.spring.professional.exam.tutorial.module08.question05.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question05.ds.Reservation;
import com.spring.professional.exam.tutorial.module08.question05.ds.Room;
import com.spring.professional.exam.tutorial.module08.question05.service.BookingService;
import com.spring.professional.exam.tutorial.module08.question05.service.GuestRegistrationService;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.Optional;

import static com.spring.professional.exam.tutorial.module08.question05.configuration.TestDataConfiguration.YELLOW_ROOM;
import static com.spring.professional.exam.tutorial.module08.question05.ds.BookingResult.BookingState.NO_ROOM_AVAILABLE;
import static com.spring.professional.exam.tutorial.module08.question05.ds.BookingResult.BookingState.ROOM_BOOKED;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(EasyMockRunner.class)
public class ApplicationServiceTest02 {

    private static final String JOHN = "John";
    private static final String DOE = "Doe";
    private static final Guest GUEST_TO_REGISTER = new Guest(JOHN, DOE);
    private static final LocalDate DATE_2020_JULY_20 = LocalDate.of(2020, 7, 20);

    @TestSubject
    private ApplicationService applicationService;
    @Mock
    private GuestRegistrationService guestRegistrationService;
    @Mock
    private BookingService bookingService;

    @Mock
    private Room room;
    @Mock
    private Guest registeredGuest;
    @Mock
    private Reservation reservation;

    @Test
    public void shouldBookRoomAfterRegisteringUserAndConfirmingRoomAvailability() {
        mockAvailableRoomAtDate(DATE_2020_JULY_20, room);
        mockGuestRegistration(GUEST_TO_REGISTER, registeredGuest);
        mockRoomBookingProcess(room, registeredGuest, DATE_2020_JULY_20);

        replay(guestRegistrationService, bookingService);

        BookingResult bookingResult = applicationService.bookAnyRoomForNewGuest(JOHN, DOE, DATE_2020_JULY_20);

        verify(guestRegistrationService, bookingService);

        assertEquals(ROOM_BOOKED, bookingResult.getBookingState());
        assertEquals(Optional.of(reservation), bookingResult.getReservation());
    }

    @Test
    public void shouldRejectBookingRequestWhenNoRoomAvailable() {
        mockNoRoomAvailableAtDate(DATE_2020_JULY_20);

        replay(guestRegistrationService, bookingService);

        BookingResult bookingResult = applicationService.bookAnyRoomForNewGuest(JOHN, DOE, DATE_2020_JULY_20);

        verify(guestRegistrationService, bookingService);

        assertEquals(NO_ROOM_AVAILABLE, bookingResult.getBookingState());
        assertEquals(Optional.empty(), bookingResult.getReservation());
    }

    @Test
    public void shouldRegisterGuest() {
        Guest guestToRegister = new Guest(JOHN, DOE);
        mockGuestRegistration(guestToRegister, registeredGuest);

        replay(guestRegistrationService, bookingService);

        Guest newlyRegisteredGuest = applicationService.registerGuest(JOHN, DOE);

        verify(guestRegistrationService, bookingService);

        assertSame(registeredGuest, newlyRegisteredGuest);
    }

    @Test
    public void shouldBookAnyRoomForRegisteredUser() {
        mockAvailableRoomAtDate(DATE_2020_JULY_20, room);
        mockRoomBookingProcess(room, registeredGuest, DATE_2020_JULY_20);

        replay(guestRegistrationService, bookingService);

        BookingResult bookingResult = applicationService.bookAnyRoomForRegisteredGuest(registeredGuest, DATE_2020_JULY_20);

        verify(guestRegistrationService, bookingService);

        assertEquals(ROOM_BOOKED, bookingResult.getBookingState());
        assertEquals(Optional.of(reservation), bookingResult.getReservation());
    }

    @Test
    public void shouldRejectBookingRequestForRegisteredUserWhenNoRoomAvailable() {
        mockNoRoomAvailableAtDate(DATE_2020_JULY_20);

        replay(guestRegistrationService, bookingService);

        BookingResult bookingResult = applicationService.bookAnyRoomForRegisteredGuest(registeredGuest, DATE_2020_JULY_20);

        verify(guestRegistrationService, bookingService);

        assertEquals(NO_ROOM_AVAILABLE, bookingResult.getBookingState());
        assertEquals(Optional.empty(), bookingResult.getReservation());
    }

    @Test
    public void shouldBookSpecificRoomForRegisteredGuest() {
        mockSpecificRoomAvailableAtDate(YELLOW_ROOM, registeredGuest, DATE_2020_JULY_20);

        replay(guestRegistrationService, bookingService);

        BookingResult bookingResult = applicationService.bookSpecificRoomForRegisteredGuest(registeredGuest, YELLOW_ROOM, DATE_2020_JULY_20);

        verify(guestRegistrationService, bookingService);

        assertEquals(ROOM_BOOKED, bookingResult.getBookingState());
        assertEquals(Optional.of(reservation), bookingResult.getReservation());
    }

    @Test
    public void shouldRejectBookSpecificRoomForRegisteredGuest() {
        mockSpecificRoomNotAvailableAtDate(YELLOW_ROOM, registeredGuest, DATE_2020_JULY_20);

        replay(guestRegistrationService, bookingService);

        BookingResult bookingResult = applicationService.bookSpecificRoomForRegisteredGuest(registeredGuest, YELLOW_ROOM, DATE_2020_JULY_20);

        verify(guestRegistrationService, bookingService);

        assertEquals(NO_ROOM_AVAILABLE, bookingResult.getBookingState());
        assertEquals(Optional.empty(), bookingResult.getReservation());
    }

    private void mockAvailableRoomAtDate(LocalDate date, Room availableRoom) {
        expect(bookingService.findAvailableRoom(date)).andReturn(Optional.of(availableRoom));
    }

    private void mockNoRoomAvailableAtDate(LocalDate date) {
        expect(bookingService.findAvailableRoom(date)).andReturn(Optional.empty());
    }

    private void mockSpecificRoomAvailableAtDate(String roomName, Guest guest, LocalDate date) {
        expect(bookingService.bookRoom(roomName, guest, date)).andReturn(Optional.of(reservation));
    }

    private void mockSpecificRoomNotAvailableAtDate(String roomName, Guest guest, LocalDate date) {
        expect(bookingService.bookRoom(roomName, guest, date)).andReturn(Optional.empty());
    }

    private void mockGuestRegistration(Guest guestToRegister, Guest registeredGuest) {
        expect(guestRegistrationService.registerGuest(guestToRegister)).andReturn(registeredGuest);
    }

    private void mockRoomBookingProcess(Room room, Guest registeredGuest, LocalDate bookingAtDate) {
        expect(bookingService.bookRoom(room, registeredGuest, bookingAtDate)).andReturn(Optional.of(reservation));
    }
}