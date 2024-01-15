package com.spring.professional.exam.tutorial.module08.question03.application;

import com.spring.professional.exam.tutorial.module08.question03.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question03.ds.Room;
import com.spring.professional.exam.tutorial.module08.question03.integration.tests.approachC.IntegrationTestConfiguration;
import com.spring.professional.exam.tutorial.module08.question03.service.GuestSharableDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.spring.professional.exam.tutorial.module08.question03.integration.tests.configuration.TestDataConfiguration.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
public class ApplicationServiceIntegrationTest03 {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private GuestSharableDataService guestSharableDataServiceMock;

    @Test
    public void shouldContainRoomsFromTestConfiguration() {
        Set<Room> roomsRegisteredInHotel = applicationService.listRooms();

        assertThat(roomsRegisteredInHotel.stream()
                .map(Room::getName)
                .collect(Collectors.toSet()))
                .containsOnly(GREEN_ROOM, YELLOW_ROOM, BLUE_ROOM);
    }

    @Test
    public void shouldContainGuestsFromTestDataScript() {
        List<Guest> guests = applicationService.listGuests();

        assertThat(guests).containsOnly(
                new Guest(1, "Earl", "Wilkerson"),
                new Guest(2, "Atif", "Melton"),
                new Guest(3, "Salma", "Sheridan")
        );
    }

    @Test
    public void shouldFetchGuestSharableData() {
        when(guestSharableDataServiceMock.getGuestSharableData()).thenReturn(SHARABLE_DATA);

        String guestSharableData = applicationService.getGuestSharableData();

        assertEquals(SHARABLE_DATA, guestSharableData);
    }

    private static final String SHARABLE_DATA = "FirstName1 LastName1, FirstName2 LastName2";
}