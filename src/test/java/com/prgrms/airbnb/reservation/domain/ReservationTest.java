package com.prgrms.airbnb.reservation.domain;

import com.prgrms.airbnb.common.model.Address;
import com.prgrms.airbnb.common.model.Email;
import com.prgrms.airbnb.common.model.Money;
import com.prgrms.airbnb.common.model.Phone;
import com.prgrms.airbnb.room.domain.Room;
import com.prgrms.airbnb.room.domain.RoomImage;
import com.prgrms.airbnb.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

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