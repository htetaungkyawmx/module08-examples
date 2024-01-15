package com.spring.professional.exam.tutorial.module08.question01;

import com.spring.professional.exam.tutorial.module08.question01.application.ApplicationService;
import com.spring.professional.exam.tutorial.module08.question01.configuration.ApplicationConfiguration;
import com.spring.professional.exam.tutorial.module08.question01.ds.BookingResult;
import com.spring.professional.exam.tutorial.module08.question01.ds.Guest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.Month;

public class Runner {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        context.registerShutdownHook();

        ApplicationService applicationService = context.getBean(ApplicationService.class);

        BookingResult bookingResult1 = applicationService.bookAnyRoomForNewGuest("John", "Doe", LocalDate.of(2020, Month.JULY, 20));
        System.out.println("Booking Result for John Doe at 7/20/2020 is " + bookingResult1);

        BookingResult bookingResult2 = applicationService.bookAnyRoomForNewGuest("Nora", "Knight", LocalDate.of(2020, Month.JULY, 20));
        System.out.println("Booking Result for Nora Knight at 7/20/2020 is " + bookingResult2);

        BookingResult bookingResult3 = applicationService.bookAnyRoomForNewGuest("Ainsley", "Jensen", LocalDate.of(2020, Month.JULY, 20));
        System.out.println("Booking Result for Ainsley Jensen at 7/20/2020 is " + bookingResult3);

        BookingResult bookingResult4 = applicationService.bookAnyRoomForNewGuest("Vance", "Woodward", LocalDate.of(2020, Month.JULY, 20));
        System.out.println("Booking Result for Vance Woodward at 7/20/2020 is " + bookingResult4);

        BookingResult bookingResult5 = applicationService.bookAnyRoomForNewGuest("Vance", "Woodward", LocalDate.of(2020, Month.JULY, 21));
        System.out.println("Booking Result for Vance Woodward at 7/21/2020 is " + bookingResult5);

        Guest guest = applicationService.registerGuest("Milo", "Steele");
        System.out.println("Milo Steele registered as " + guest);
        BookingResult bookingResult6 = applicationService.bookAnyRoomForRegisteredGuest(guest, LocalDate.of(2020, Month.JULY, 21));
        System.out.println("Booking Result for already registered guest Milo Steele at 7/21/2020 is " + bookingResult6);

        BookingResult bookingResult7 = applicationService.bookSpecificRoomForRegisteredGuest(guest, "Yellow Room", LocalDate.of(2020, Month.JULY, 21));
        System.out.println("Booking Result for specific room for already registered guest Milo Steele at 7/21/2020 is " + bookingResult7);
    }
}
