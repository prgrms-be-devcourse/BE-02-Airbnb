package com.prgrms.airbnb.domain.reservation.service;

import com.prgrms.airbnb.domain.common.exception.BadRequestException;
import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.reservation.dto.CreateReservationRequest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForGuest;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReservationServiceForGuest {

  private final ReservationRepository reservationRepository;
  private final UserRepository userRepository;
  private final RoomRepository roomRepository;

  public ReservationServiceForGuest(
      ReservationRepository reservationRepository,
      UserRepository userRepository,
      RoomRepository roomRepository) {
    this.reservationRepository = reservationRepository;
    this.userRepository = userRepository;
    this.roomRepository = roomRepository;
  }

  public ReservationDetailResponseForGuest findDetailById(String reservationId, Long userId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });

    if(!reservation.validateUserId(userId)){
      throw new BadRequestException(this.getClass().getName());
    }

    Room room = roomRepository.findById(reservation.getRoomId())
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    User host = userRepository.findById(room.getUserId())
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    return ReservationConverter.ofDetailForGuest(reservation, host, room);
  }

  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public ReservationDetailResponseForGuest save(CreateReservationRequest createReservationRequest) {
    Room room = roomRepository.findById(createReservationRequest.getRoomId())
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    User host = userRepository.findById(room.getUserId())
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    if (reservationRepository.existReservation(room.getId(),
        createReservationRequest.getStartDate(),
        createReservationRequest.getEndDate())) {
      throw new BadRequestException(this.getClass().getName());
    }
    String reservationId = reservationRepository.createReservationId();
    Reservation reservation = ReservationConverter.toReservation(reservationId,
        createReservationRequest);
    Reservation savedReservation = reservationRepository.save(reservation);
    return ReservationConverter.ofDetailForGuest(savedReservation, host, room);
  }

  @Transactional
  public void cancel(Long userId, String reservationId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    if (!reservation.canCancelled(userId)) {
      throw new BadRequestException(this.getClass().getName());
    }
    reservation.changeStatus(ReservationStatus.GUEST_CANCELLED);
  }

  public Slice<ReservationSummaryResponse> findByUserId(Long userId, Pageable pageable) {
    Slice<Reservation> reservationList = reservationRepository.findByUserIdOrderByCreatedAtDesc(
        userId, pageable);
    return reservationList.map(ReservationConverter::ofSummary);
  }
}
