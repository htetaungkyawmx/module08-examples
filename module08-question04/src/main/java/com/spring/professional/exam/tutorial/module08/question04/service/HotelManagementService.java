package com.spring.professional.exam.tutorial.module08.question04.service;

import com.spring.professional.exam.tutorial.module08.question04.ds.Room;
import com.spring.professional.exam.tutorial.module08.question04.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class HotelManagementService {

    @Autowired
    private RoomRepository roomRepository;

    public void registerRoom(Room room) {
        if (room.getId() != null)
            throw new IllegalArgumentException(String.format("Room [%s] already has ID assigned", room));

        roomRepository.save(room);
    }

    public Set<Room> listRooms() {
        return roomRepository.findAll();
    }
}
