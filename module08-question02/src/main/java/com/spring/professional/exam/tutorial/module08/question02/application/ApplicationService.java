package com.spring.professional.exam.tutorial.module08.question02.application;

import com.spring.professional.exam.tutorial.module08.question02.ds.BookingResult;
import com.spring.professional.exam.tutorial.module08.question02.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question02.ds.Reservation;
import com.spring.professional.exam.tutorial.module08.question02.ds.Room;
import com.spring.professional.exam.tutorial.module08.question02.service.BookingService;
import com.spring.professional.exam.tutorial.module08.question02.service.GuestRegistrationService;
import com.spring.professional.exam.tutorial.module08.question02.service.GuestSharableDataService;
import com.spring.professional.exam.tutorial.module08.question02.service.HotelManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ApplicationService {

    @Autowired
    private GuestRegistrationService guestRegistrationService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private HotelManagementService hotelManagementService;
    @Autowired
    private GuestSharableDataService guestSharableDataService;

    public BookingResult bookAnyRoomForNewGuest(String firstName, String lastName, LocalDate date) {
        Optional<Room> availableRoom = bookingService.findAvailableRoom(date);

        if (availableRoom.isPresent()) {
            Guest registeredGuest = registerGuest(firstName, lastName);

            return BookingResult.createRoomBookedResult(
                    bookingService.bookRoom(availableRoom.get(), registeredGuest, date).orElseThrow()
            );
        } else
            return BookingResult.createNoRoomAvailableResult();
    }

    public Guest registerGuest(String firstName, String lastName) {
        Guest guestToRegister = new Guest(firstName, lastName);
        return guestRegistrationService.registerGuest(guestToRegister);
    }

    public BookingResult bookAnyRoomForRegisteredGuest(Guest guest, LocalDate date) {
        Optional<Room> availableRoom = bookingService.findAvailableRoom(date);

        if (availableRoom.isPresent()) {
            return BookingResult.createRoomBookedResult(
                    bookingService.bookRoom(availableRoom.get(), guest, date).orElseThrow()
            );
        } else
            return BookingResult.createNoRoomAvailableResult();
    }

    public BookingResult bookSpecificRoomForRegisteredGuest(Guest guest, String roomName, LocalDate date) {
        Optional<Reservation> reservation = bookingService.bookRoom(roomName, guest, date);

        return reservation
                .map(BookingResult::createRoomBookedResult)
                .orElseGet(BookingResult::createNoRoomAvailableResult);
    }

    public Set<Room> listRooms() {
        return hotelManagementService.listRooms();
    }

    public List<Guest> listGuests() {
        return guestRegistrationService.listGuests();
    }

    public String getGuestSharableData() {
        return guestSharableDataService.getGuestSharableData();
    }
}
