package com.spring.professional.exam.tutorial.module08.question04.application;

import com.spring.professional.exam.tutorial.module08.question04.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question04.ds.Room;
import com.spring.professional.exam.tutorial.module08.question04.service.GuestRegistrationService;
import com.spring.professional.exam.tutorial.module08.question04.service.HotelManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class ApplicationService {

    @Autowired
    private GuestRegistrationService guestRegistrationService;
    @Autowired
    private HotelManagementService hotelManagementService;

    public void registerGuests(Guest... guests) {
        Arrays.asList(guests).forEach(guestRegistrationService::registerGuest);
    }

    public void registerRooms(Room... rooms) {
        Arrays.asList(rooms).forEach(hotelManagementService::registerRoom);
    }

    public List<Guest> listGuests() {
        return guestRegistrationService.listGuests();
    }

    public Set<Room> listRooms() {
        return hotelManagementService.listRooms();
    }
}
