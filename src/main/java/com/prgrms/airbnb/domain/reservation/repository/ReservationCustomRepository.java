package com.prgrms.airbnb.domain.reservation.repository;

import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReservationCustomRepository {
    void updateReservationStatus();
    Slice<Reservation> findAllReservationByHostId(Long hostId, Pageable pageable);
    boolean existReservation(Long roomId, LocalDate startDate, LocalDate endDate);
}
