package com.spring.professional.exam.tutorial.module08.question07.ds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class BookingRequest {
    private Guest guest;
    private LocalDate bookingDate;

    @SuppressWarnings("unused")
    BookingRequest() {
    }
}
