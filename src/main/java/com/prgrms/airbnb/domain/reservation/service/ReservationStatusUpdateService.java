package com.prgrms.airbnb.domain.reservation.service;

import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationStatusUpdateService {

  private final ReservationRepository reservationRepository;

  public ReservationStatusUpdateService(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
  }

  @Scheduled(cron = "0 0 5 * * *")
  public void updateReservationStatus() {
    reservationRepository.updateReservationStatus();
  }
}
