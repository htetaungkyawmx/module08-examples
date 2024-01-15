package com.spring.professional.exam.tutorial.module08.question01.repository;

import com.spring.professional.exam.tutorial.module08.question01.ds.Guest;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GuestRepository extends CrudRepository<Guest, UUID> {
}
