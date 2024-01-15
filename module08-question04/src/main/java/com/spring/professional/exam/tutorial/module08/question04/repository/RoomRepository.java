package com.spring.professional.exam.tutorial.module08.question04.repository;

import com.spring.professional.exam.tutorial.module08.question04.ds.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;
import java.util.UUID;

public interface RoomRepository extends CrudRepository<Room, UUID> {

    Set<Room> findAll();
}
