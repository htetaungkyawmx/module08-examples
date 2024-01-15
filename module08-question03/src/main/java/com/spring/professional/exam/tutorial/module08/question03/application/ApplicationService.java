package com.spring.professional.exam.tutorial.module08.question03.application;

import com.spring.professional.exam.tutorial.module08.question03.ds.Guest;
import com.spring.professional.exam.tutorial.module08.question03.ds.Room;
import com.spring.professional.exam.tutorial.module08.question03.service.GuestRegistrationService;
import com.spring.professional.exam.tutorial.module08.question03.service.GuestSharableDataService;
import com.spring.professional.exam.tutorial.module08.question03.service.HotelManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ApplicationService {

    @Autowired
    private GuestRegistrationService guestRegistrationService;
    @Autowired
    private HotelManagementService hotelManagementService;
    @Autowired
    private GuestSharableDataService guestSharableDataService;

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
