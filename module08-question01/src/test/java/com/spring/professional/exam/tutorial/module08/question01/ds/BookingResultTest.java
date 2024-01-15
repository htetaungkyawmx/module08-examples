package com.spring.professional.exam.tutorial.module08.question01.ds;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.spring.professional.exam.tutorial.module08.question01.ds.BookingResult.BookingState.NO_ROOM_AVAILABLE;
import static com.spring.professional.exam.tutorial.module08.question01.ds.BookingResult.BookingState.ROOM_BOOKED;
import static com.spring.professional.exam.tutorial.module08.question01.ds.BookingResult.createNoRoomAvailableResult;
import static com.spring.professional.exam.tutorial.module08.question01.ds.BookingResult.createRoomBookedResult;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class BookingResultTest {

    @Mock
    private Reservation reservation;

    @Test
    public void shouldCreateRoomBookedResult() {
        BookingResult roomBookedResult = createRoomBookedResult(reservation);

        assertSame(ROOM_BOOKED, roomBookedResult.getBookingState());
        assertEquals(Optional.of(reservation), roomBookedResult.getReservation());
    }

    @Test
    public void shouldCreateNoRoomAvailableResult() {
        BookingResult roomBookedResult = createNoRoomAvailableResult();

        assertSame(NO_ROOM_AVAILABLE, roomBookedResult.getBookingState());
        assertEquals(Optional.empty(), roomBookedResult.getReservation());
    }
}