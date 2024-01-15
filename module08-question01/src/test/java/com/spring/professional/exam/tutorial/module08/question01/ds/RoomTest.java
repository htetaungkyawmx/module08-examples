package com.spring.professional.exam.tutorial.module08.question01.ds;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

// Please see comments on GuestTest class regarding testing approach.
public class RoomTest {

    private static final String ROOM_NAME = "Some Room";
    private static final String SECTION_NAME = "C05";
    private static final UUID ROOM_ID = UUID.randomUUID();

    @Test
    public void shouldCreateRoom() {
        Room room = new Room(ROOM_NAME, SECTION_NAME);

        assertSame(ROOM_NAME, room.getName());
        assertSame(SECTION_NAME, room.getSection());
    }

    // Please see comment on GuestTest class regarding usage of ReflectionTestUtils.
    @Test
    public void shouldSetRoomId() {
        Room room = new Room();

        ReflectionTestUtils.setField(room, "id", ROOM_ID, UUID.class);

        assertEquals(ROOM_ID, room.getId());
    }
}