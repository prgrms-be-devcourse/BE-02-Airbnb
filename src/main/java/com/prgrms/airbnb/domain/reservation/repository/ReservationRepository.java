package com.prgrms.airbnb.domain.reservation.repository;

import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    Slice<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Optional<Reservation> findByUserId(Long userId);

    default String createReservationId() {
        int randomNo = ThreadLocalRandom.current().nextInt(900000) + 100000;
        String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNo);
        return number;
    }
}
