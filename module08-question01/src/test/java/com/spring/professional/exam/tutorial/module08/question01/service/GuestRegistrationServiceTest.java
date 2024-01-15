package com.spring.professional.exam.tutorial.module08.question01.service;

import com.spring.professional.exam.tutorial.module08.question01.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question01.repository.GuestRepository;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.mockito.Mockito.*;

// This test was written with "classic" approach to mock injection just to show
// how ReflectionTestUtils can be used for @Autowired fields.
// However, consider using MockitoJUnitRunner and @Mock instead, like inside BookingServiceTest,
// since usage of MockitoJUnitRunner and @Mock makes tests easier to read
public class GuestRegistrationServiceTest {

    @Test
    public void shouldRegisterGuest() {
        GuestRegistrationService guestRegistrationService = new GuestRegistrationService();
        GuestRepository guestRepository = mock(GuestRepository.class);
        Guest guest = mock(Guest.class);

        ReflectionTestUtils.setField(guestRegistrationService, "guestRepository", guestRepository);

        guestRegistrationService.registerGuest(guest);

        verify(guestRepository).save(guest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenRegisteringAlreadyRegisteredUser() {
        GuestRegistrationService guestRegistrationService = new GuestRegistrationService();
        Guest guest = mock(Guest.class);

        when(guest.getId()).thenReturn(UUID.randomUUID());

        guestRegistrationService.registerGuest(guest);
    }
}