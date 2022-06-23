package com.prgrms.airbnb.domain.reservation.service;

import com.prgrms.airbnb.domain.reservation.dto.CreateReservationRequest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForGuest;
import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import com.prgrms.airbnb.domain.reservation.entity.ReservationStatus;
import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.domain.reservation.util.ReservationConverter;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import com.prgrms.airbnb.domain.user.entity.User;
import com.prgrms.airbnb.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GuestReservationService {

  private final ReservationRepository reservationRepository;
  private final UserRepository userRepository;
  private final RoomRepository roomRepository;

  public ReservationDetailResponseForGuest findDetailById(String reservationId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(IllegalArgumentException::new);
    Room room = roomRepository.findById(reservation.getRoomId())
        .orElseThrow(IllegalArgumentException::new);
    User host = userRepository.findById(room.getUserId())
        .orElseThrow(IllegalArgumentException::new);
    return ReservationConverter.ofDetailForGuest(reservation, host, room);
  }

  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public ReservationDetailResponseForGuest save(CreateReservationRequest createReservationRequest) {
    Room room = roomRepository.findById(createReservationRequest.getRoomId())
        .orElseThrow(IllegalArgumentException::new);
    User host = userRepository.findById(room.getUserId())
        .orElseThrow(IllegalArgumentException::new);
    String reservationId = reservationRepository.createReservationId();
    Reservation reservation = ReservationConverter.toReservation(reservationId,
        createReservationRequest);
    Reservation savedReservation = reservationRepository.save(reservation);
    return ReservationConverter.ofDetailForGuest(savedReservation, host, room);
  }

  @Transactional
  public void cancel(String reservationId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(IllegalArgumentException::new);
    reservation.cancelReservation(ReservationStatus.GUEST_CANCELLED);
  }
}
