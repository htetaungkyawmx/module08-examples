package com.spring.professional.exam.tutorial.module08.question08.web;

import com.spring.professional.exam.tutorial.module08.question08.ds.BookingRequest;
import com.spring.professional.exam.tutorial.module08.question08.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question08.ds.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ApplicationServiceControllerIntegrationTest {

    private static final String API_GUESTS = "/api/guests";
    private static final String API_BOOKINGS = "/api/bookings";

    private static final String GUEST_FIRST_NAME = "John";
    private static final String GUEST_LAST_NAME = "Doe";
    private static final Guest GUEST = new Guest(GUEST_FIRST_NAME, GUEST_LAST_NAME);
    private static final LocalDate DATE_2020_JULY_20 = LocalDate.of(2020, 7, 20);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<Guest> guestJson;
    @Autowired
    private JacksonTester<List<Guest>> guestsJson;
    @Autowired
    private JacksonTester<BookingRequest> bookingRequestJson;
    @Autowired
    private JacksonTester<List<Reservation>> reservationsJson;

    @Test
    @DirtiesContext
    public void shouldRegisterGuest() throws Exception {
        mockMvc.perform(put(API_GUESTS).contentType(APPLICATION_JSON).content(guestJson.write(GUEST).getJson()))
                .andExpect(status().isOk());

        List<Guest> guests = guestsJson.parseObject(mockMvc.perform(get(API_GUESTS))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertThat(guests)
                .usingElementComparator(guestComparatorWithoutId())
                .containsOnly(GUEST);
    }

    @Test
    @DirtiesContext
    public void shouldBookRoomForRegisteredGuest() throws Exception {
        Guest registeredGuest = guestJson.parseObject(mockMvc.perform(put(API_GUESTS).contentType(APPLICATION_JSON).content(guestJson.write(GUEST).getJson()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        mockMvc.perform(put(API_BOOKINGS).contentType(APPLICATION_JSON).content(bookingRequestJson.write(new BookingRequest(registeredGuest, DATE_2020_JULY_20)).getJson()))
                .andExpect(status().isOk());

        List<Reservation> reservations = reservationsJson.parseObject(mockMvc.perform(get(API_BOOKINGS))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertEquals(1, reservations.size());
        Reservation reservation = reservations.get(0);
        assertEquals(GUEST_FIRST_NAME, reservation.getGuest().getFirstName());
        assertEquals(GUEST_LAST_NAME, reservation.getGuest().getLastName());
        assertEquals(DATE_2020_JULY_20, reservation.getReservationDate());
    }

    private Comparator<Guest> guestComparatorWithoutId() {
        return Comparator
                .comparing(Guest::getFirstName)
                .thenComparing(Guest::getLastName);
    }
}