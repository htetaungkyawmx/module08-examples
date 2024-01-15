package com.spring.professional.exam.tutorial.module08.question01.ds;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
// Please see comments on GuestTest class regarding testing approach.
public class ReservationTest {

    private static final LocalDate RESERVATION_DATE = LocalDate.of(2020, 7, 21);

    private static final UUID RESERVATION_ID = UUID.randomUUID();

    @Mock
    private Room room;
    @Mock
    private Guest guest;

    @Test
    public void shouldCreateReservation() {
        Reservation reservation = new Reservation(room, guest, RESERVATION_DATE);

        assertSame(room, reservation.getRoom());
        assertSame(guest, reservation.getGuest());
        assertSame(RESERVATION_DATE, reservation.getReservationDate());
    }

    @Test
    // Please see comment on GuestTest class regarding usage of ReflectionTestUtils.
    // This test uses different approach, field was changed to package-private, to break encapsulation,
    // which allows for field modification from test level, keeping compile-time-feedback.
    public void shouldSetReservationId() {
        Reservation reservation = new Reservation();

        reservation.id = RESERVATION_ID;

        assertSame(RESERVATION_ID, reservation.getId());
    }
}