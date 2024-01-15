package com.spring.professional.exam.tutorial.module08.question05.repository;

import com.spring.professional.exam.tutorial.module08.question05.ds.Guest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface GuestRepository extends CrudRepository<Guest, UUID> {

    List<Guest> findAll();
}
