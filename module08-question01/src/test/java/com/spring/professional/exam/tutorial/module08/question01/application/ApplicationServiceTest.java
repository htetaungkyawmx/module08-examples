package com.spring.professional.exam.tutorial.module08.question01.application;

import com.spring.professional.exam.tutorial.module08.question01.ds.BookingResult;
import com.spring.professional.exam.tutorial.module08.question01.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question01.ds.Reservation;
import com.spring.professional.exam.tutorial.module08.question01.ds.Room;
import com.spring.professional.exam.tutorial.module08.question01.service.BookingService;
import com.spring.professional.exam.tutorial.module08.question01.service.GuestRegistrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static com.spring.professional.exam.tutorial.module08.question01.configuration.DefaultDataConfiguration.YELLOW_ROOM;
import static com.spring.professional.exam.tutorial.module08.question01.ds.BookingResult.BookingState.NO_ROOM_AVAILABLE;
import static com.spring.professional.exam.tutorial.module08.question01.ds.BookingResult.BookingState.ROOM_BOOKED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationServiceTest {

    private static final String JOHN = "John";
    private static final String DOE = "Doe";
    private static final Guest GUEST_TO_REGISTER = new Guest(JOHN, DOE);
    private static final LocalDate DATE_2020_JULY_20 = LocalDate.of(2020, 7, 20);

    @InjectMocks
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

        BookingResult bookingResult = applicationService.bookAnyRoomForNewGuest(JOHN, DOE, DATE_2020_JULY_20);

        verifyGuestRegistrationPerformed(GUEST_TO_REGISTER);
        verifyRoomBookingProcessPerformed(room, registeredGuest, DATE_2020_JULY_20);

        assertEquals(ROOM_BOOKED, bookingResult.getBookingState());
        assertEquals(Optional.of(reservation), bookingResult.getReservation());
    }

    @Test
    public void shouldRejectBookingRequestWhenNoRoomAvailable() {
        mockNoRoomAvailableAtDate(DATE_2020_JULY_20);

        BookingResult bookingResult = applicationService.bookAnyRoomForNewGuest(JOHN, DOE, DATE_2020_JULY_20);

        verifyGuestRegistrationWasNotPerformed();
        verifyRoomBookingProcessWasNotPerformed();

        assertEquals(NO_ROOM_AVAILABLE, bookingResult.getBookingState());
        assertEquals(Optional.empty(), bookingResult.getReservation());
    }

    @Test
    public void shouldRegisterGuest() {
        Guest guestToRegister = new Guest(JOHN, DOE);
        mockGuestRegistration(guestToRegister, registeredGuest);

        Guest newlyRegisteredGuest = applicationService.registerGuest(JOHN, DOE);

        verifyGuestRegistrationPerformed(guestToRegister);

        assertSame(registeredGuest, newlyRegisteredGuest);
    }

    @Test
    public void shouldBookAnyRoomForRegisteredUser() {
        mockAvailableRoomAtDate(DATE_2020_JULY_20, room);
        mockRoomBookingProcess(room, registeredGuest, DATE_2020_JULY_20);

        BookingResult bookingResult = applicationService.bookAnyRoomForRegisteredGuest(registeredGuest, DATE_2020_JULY_20);

        verifyRoomBookingProcessPerformed(room, registeredGuest, DATE_2020_JULY_20);

        assertEquals(ROOM_BOOKED, bookingResult.getBookingState());
        assertEquals(Optional.of(reservation), bookingResult.getReservation());
    }

    @Test
    public void shouldRejectBookingRequestForRegisteredUserWhenNoRoomAvailable() {
        mockNoRoomAvailableAtDate(DATE_2020_JULY_20);

        BookingResult bookingResult = applicationService.bookAnyRoomForRegisteredGuest(registeredGuest, DATE_2020_JULY_20);

        verifyRoomBookingProcessWasNotPerformed();

        assertEquals(NO_ROOM_AVAILABLE, bookingResult.getBookingState());
        assertEquals(Optional.empty(), bookingResult.getReservation());
    }

    @Test
    public void shouldBookSpecificRoomForRegisteredGuest() {
        mockSpecificRoomAvailableAtDate(YELLOW_ROOM, registeredGuest, DATE_2020_JULY_20);

        BookingResult bookingResult = applicationService.bookSpecificRoomForRegisteredGuest(registeredGuest, YELLOW_ROOM, DATE_2020_JULY_20);

        assertEquals(ROOM_BOOKED, bookingResult.getBookingState());
        assertEquals(Optional.of(reservation), bookingResult.getReservation());
    }

    @Test
    public void shouldRejectBookSpecificRoomForRegisteredGuest() {
        mockSpecificRoomNotAvailableAtDate(YELLOW_ROOM, registeredGuest, DATE_2020_JULY_20);

        BookingResult bookingResult = applicationService.bookSpecificRoomForRegisteredGuest(registeredGuest, YELLOW_ROOM, DATE_2020_JULY_20);

        assertEquals(NO_ROOM_AVAILABLE, bookingResult.getBookingState());
        assertEquals(Optional.empty(), bookingResult.getReservation());
    }

    private void mockAvailableRoomAtDate(LocalDate date, Room availableRoom) {
        when(bookingService.findAvailableRoom(date)).thenReturn(Optional.of(availableRoom));
    }

    private void mockNoRoomAvailableAtDate(LocalDate date) {
        when(bookingService.findAvailableRoom(date)).thenReturn(Optional.empty());
    }

    private void mockSpecificRoomAvailableAtDate(String roomName, Guest guest, LocalDate date) {
        when(bookingService.bookRoom(roomName, guest, date)).thenReturn(Optional.of(reservation));
    }

    private void mockSpecificRoomNotAvailableAtDate(String roomName, Guest guest, LocalDate date) {
        when(bookingService.bookRoom(roomName, guest, date)).thenReturn(Optional.empty());
    }

    private void mockGuestRegistration(Guest guestToRegister, Guest registeredGuest) {
        when(guestRegistrationService.registerGuest(guestToRegister)).thenReturn(registeredGuest);
    }

    private void mockRoomBookingProcess(Room room, Guest registeredGuest, LocalDate bookingAtDate) {
        when(bookingService.bookRoom(room, registeredGuest, bookingAtDate)).thenReturn(Optional.of(reservation));
    }

    private void verifyGuestRegistrationPerformed(Guest guestToRegister) {
        verify(guestRegistrationService).registerGuest(guestToRegister);
    }

    private void verifyGuestRegistrationWasNotPerformed() {
        verify(guestRegistrationService, never()).registerGuest(any());
    }

    private void verifyRoomBookingProcessPerformed(Room room, Guest registeredGuest, LocalDate bookingAtDate) {
        verify(bookingService).bookRoom(room, registeredGuest, bookingAtDate);
    }

    private void verifyRoomBookingProcessWasNotPerformed() {
        verify(bookingService, never()).bookRoom(any(Room.class), any(), any());
    }
}