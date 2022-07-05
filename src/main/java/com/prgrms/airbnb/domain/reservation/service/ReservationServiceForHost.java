package com.prgrms.airbnb.domain.reservation.service;

import com.prgrms.airbnb.domain.common.exception.BadRequestException;
import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForHost;
import com.prgrms.airbnb.domain.reservation.dto.ReservationSummaryResponse;
import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import com.prgrms.airbnb.domain.reservation.entity.ReservationStatus;
import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.domain.reservation.util.ReservationConverter;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import com.prgrms.airbnb.domain.user.entity.User;
import com.prgrms.airbnb.domain.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReservationServiceForHost {

  private final ReservationRepository reservationRepository;
  private final UserRepository userRepository;
  private final RoomRepository roomRepository;

  public ReservationServiceForHost(
      ReservationRepository reservationRepository,
      UserRepository userRepository,
      RoomRepository roomRepository) {
    this.reservationRepository = reservationRepository;
    this.userRepository = userRepository;
    this.roomRepository = roomRepository;
  }

  public ReservationDetailResponseForHost findDetailById(String reservationId, Long hostId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    User guest = userRepository.findById(reservation.getUserId())
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    Room room = roomRepository.findById(reservation.getRoomId())
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });

    if(!room.getUserId().equals(hostId)){
      throw new BadRequestException(this.getClass().getName());
    }

    return ReservationConverter.ofDetailForHost(reservation, guest, room);
  }

  public Slice<ReservationSummaryResponse> findByHostId(Long hostId, Pageable pageable) {
    Slice<Reservation> allReservationByHostId = reservationRepository
        .findAllReservationByHostId(hostId, pageable);

    return allReservationByHostId.map(ReservationConverter::ofSummary);
  }

  @Transactional
  public ReservationDetailResponseForHost approval(String reservationId, Long userId,
      ReservationStatus reservationStatus) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    Room room = roomRepository.findById(reservation.getRoomId())
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    User guest = userRepository.findById(reservation.getUserId())
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    if (!userId.equals(room.getUserId())) {
      //TODO 권한 없음 에러 처리 필요
      throw new BadRequestException(this.getClass().getName());
    }
    reservation.changeStatus(reservationStatus);
    return ReservationConverter.ofDetailForHost(reservation, guest, room);
  }

}
