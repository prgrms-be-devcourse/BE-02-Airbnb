package com.prgrms.airbnb.domain.reservation.service;

import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForHost;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HostReservationService {

  private final ReservationRepository reservationRepository;
  private final UserRepository userRepository;
  private final RoomRepository roomRepository;

  public ReservationDetailResponseForHost findDetailById(String reservationId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(IllegalArgumentException::new);
    User guest = userRepository.findById(reservation.getUserId())
        .orElseThrow(IllegalArgumentException::new);
    Room room = roomRepository.findById(reservation.getRoomId())
        .orElseThrow(IllegalArgumentException::new);
    return ReservationConverter.ofDetailForHost(reservation, guest, room);
  }

  @Transactional
  public void approval(String reservationId, Long userId, ReservationStatus reservationStatus) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(IllegalArgumentException::new);
    Long hostId = roomRepository.findById(reservation.getRoomId())
        .orElseThrow(IllegalArgumentException::new).getUserId();
    if (userId != hostId) {
      //TODO 권한 없음 에러 처리 필요
      throw new IllegalArgumentException();
    }
    reservation.approval(reservationStatus);
  }

}
