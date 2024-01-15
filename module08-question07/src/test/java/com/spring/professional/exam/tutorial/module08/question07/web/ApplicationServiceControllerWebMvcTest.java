package com.spring.professional.exam.tutorial.module08.question07.web;

import com.spring.professional.exam.tutorial.module08.question07.application.ApplicationService;
import com.spring.professional.exam.tutorial.module08.question07.ds.BookingRequest;
import com.spring.professional.exam.tutorial.module08.question07.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question07.ds.Reservation;
import com.spring.professional.exam.tutorial.module08.question07.ds.Room;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ApplicationServiceController.class)
@AutoConfigureJsonTesters
public class ApplicationServiceControllerWebMvcTest {

    private static final String API_GUESTS = "/api/guests";
    private static final String API_BOOKINGS = "/api/bookings";

    private static final Guest GUEST = new Guest("John", "Doe");
    private static final Guest REGISTERED_GUEST = new Guest(UUID.randomUUID(), "John", "Doe");
    private static final Guest SECOND_GUEST = new Guest("Virgie", "Webster");
    private static final Guest THIRD_GUEST = new Guest("Daphne", "Archer");
    private static final List<Guest> GUESTS = Arrays.asList(GUEST, SECOND_GUEST, THIRD_GUEST);
    private static final LocalDate DATE_2020_JULY_20 = LocalDate.of(2020, 7, 20);
    private static final BookingRequest BOOKING_REQUEST = new BookingRequest(GUEST, DATE_2020_JULY_20);
    private static final Reservation RESERVATION_1 = new Reservation(new Room("Room1", "A"), GUEST, DATE_2020_JULY_20);
    private static final Reservation RESERVATION_2 = new Reservation(new Room("Room2", "B"), SECOND_GUEST, DATE_2020_JULY_20);
    private static final List<Reservation> RESERVATIONS = Arrays.asList(RESERVATION_1, RESERVATION_2);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @Autowired
    private JacksonTester<Guest> guestJson;
    @Autowired
    private JacksonTester<List<Guest>> guestsJson;
    @Autowired
    private JacksonTester<BookingRequest> bookingRequestJson;
    @Autowired
    private JacksonTester<List<Reservation>> reservationsJson;

    @Test
    public void shouldRegisterGuest() throws Exception {
        when(applicationService.registerGuest(GUEST)).thenReturn(REGISTERED_GUEST);

        MvcResult mvcResult = mockMvc.perform(put(API_GUESTS).contentType(APPLICATION_JSON).content(guestJson.write(GUEST).getJson()))
                .andExpect(status().isOk())
                .andReturn();

        Guest registeredGuest = guestJson.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(REGISTERED_GUEST, registeredGuest);
    }

    @Test
    public void shouldListGuests() throws Exception {
        when(applicationService.listGuests()).thenReturn(GUESTS);

        List<Guest> listedGuests = guestsJson.parseObject(mockMvc.perform(get(API_GUESTS))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertEquals(GUESTS, listedGuests);
    }

    @Test
    public void shouldBookAnyRoomForRegisteredGuest() throws Exception {
        mockMvc.perform(put(API_BOOKINGS).contentType(APPLICATION_JSON).content(bookingRequestJson.write(BOOKING_REQUEST).getJson()))
                .andExpect(status().isOk());

        verify(applicationService).bookAnyRoomForRegisteredGuest(GUEST, DATE_2020_JULY_20);
    }

    @Test
    public void shouldListReservations() throws Exception {
        when(applicationService.listReservations()).thenReturn(RESERVATIONS);

        List<Reservation> listedReservations = reservationsJson.parseObject(mockMvc.perform(get(API_BOOKINGS))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertEquals(RESERVATIONS, listedReservations);
    }
}