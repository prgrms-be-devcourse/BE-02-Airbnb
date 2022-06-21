package com.prgrms.airbnb.reservation.repository;

import com.prgrms.airbnb.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    default String createReservationNo() {
        int randomNo = ThreadLocalRandom.current().nextInt(900000) + 100000;
        String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNo);
        return number;
    }
}
