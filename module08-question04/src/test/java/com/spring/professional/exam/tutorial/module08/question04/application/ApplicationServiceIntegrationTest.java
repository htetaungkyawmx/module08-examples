package com.spring.professional.exam.tutorial.module08.question04.application;

import com.spring.professional.exam.tutorial.module08.question04.configuration.ApplicationConfiguration;
import com.spring.professional.exam.tutorial.module08.question04.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question04.ds.Identifiable;
import com.spring.professional.exam.tutorial.module08.question04.ds.Room;
import org.fest.assertions.core.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class ApplicationServiceIntegrationTest {

    private static final Guest GUEST_1 = new Guest("Michal ", "Leon");
    private static final Guest GUEST_2 = new Guest("Virgie", "Webster");
    private static final Guest GUEST_3 = new Guest("Daphne", "Archer");

    private static final Room ROOM_1 = new Room("Green Room", "A01");
    private static final Room ROOM_2 = new Room("Yellow Room", "A02");
    private static final Room ROOM_3 = new Room("Blue Room", "B01");

    @Autowired
    private ApplicationService applicationService;

    @Transactional
    @Test
    @Commit
    public void shouldRegisterGuests() {
        applicationService.registerGuests(GUEST_1, GUEST_2, GUEST_3);

        assertThat(applicationService.listGuests())
                .have(elementsWithIdAssigned())
                .usingElementComparator(guestComparatorWithoutId())
                .containsExactly(
                        GUEST_1, GUEST_2, GUEST_3
                );
    }

    @Transactional
    @Test
    public void shouldRegisterRooms() {
        applicationService.registerRooms(ROOM_1, ROOM_2, ROOM_3);

        assertThat(applicationService.listRooms())
                .have(elementsWithIdAssigned())
                .usingElementComparator(roomComparatorWithoutId())
                .containsExactly(
                        ROOM_1, ROOM_2, ROOM_3
                );
    }

    @BeforeTransaction
    private void beforeTransaction() {
        System.out.println("BEFORE TRANSACTION");
    }

    @AfterTransaction
    private void afterTransaction() {
        System.out.println("AFTER TRANSACTION");
    }

    private Comparator<Guest> guestComparatorWithoutId() {
        return Comparator
                .comparing(Guest::getFirstName)
                .thenComparing(Guest::getLastName);
    }

    private Comparator<Room> roomComparatorWithoutId() {
        return Comparator
                .comparing(Room::getName)
                .thenComparing(Room::getSection);
    }

    private Condition<? super Identifiable> elementsWithIdAssigned() {
        return new Condition<>() {
            @Override
            public boolean matches(Identifiable identifiable) {
                return identifiable.getId() > 0;
            }
        };
    }
}