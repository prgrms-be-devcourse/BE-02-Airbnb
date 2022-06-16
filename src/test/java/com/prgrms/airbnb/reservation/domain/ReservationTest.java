package com.prgrms.airbnb.reservation.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ReservationTest {

    @Test
    void reservation_created() {
        Reservation reservation = Reservation.builder()
                .reservationStatus(ReservationStatus.ACCEPTED)
                .checkIn(LocalDate.of(2022, 6, 16))
                .checkOut(LocalDate.of(2022, 6, 22))
                .period(6)
                .userId(1L)
                .roomId(1L)
                .build();
        Assertions.assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.ACCEPTED);
    }
}