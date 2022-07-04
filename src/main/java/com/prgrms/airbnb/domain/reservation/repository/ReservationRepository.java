package com.prgrms.airbnb.domain.reservation.repository;

import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, String>,
    ReservationCustomRepository {

  Slice<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

  default String createReservationId() {
    int randomNo = ThreadLocalRandom.current().nextInt(900000) + 100000;
    String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNo);
    return number;
  }
}
