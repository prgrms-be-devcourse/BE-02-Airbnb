package com.prgrms.airbnb.domain.reservation.repository;

import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReservationStatusRepository {
    void updateReservationStatus();
    Slice<Reservation> findAllReservationByHostId(Long hostId, Pageable pageable);
}
