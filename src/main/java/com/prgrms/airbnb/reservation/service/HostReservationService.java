package com.prgrms.airbnb.reservation.service;

import com.prgrms.airbnb.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HostReservationService {

    private ReservationRepository reservationRepository;

}
