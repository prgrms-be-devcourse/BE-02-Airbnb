package com.prgrms.airbnb.domain.reservation.service;

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
import java.time.LocalDate;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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

    if (LocalDate.now().isAfter(createReservationRequest.getStartDate())) {
      throw new IllegalArgumentException();
    }
    //TODO: 저장하기전에 앞서 예약이 존재하는지 확인해야함
    if (reservationRepository.findAll()
        .stream()
        .anyMatch(v ->
            !v.canReservation(
                createReservationRequest.getStartDate(),
                createReservationRequest.getEndDate())
        )) {
      throw new IllegalArgumentException();
    }
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
    if (!reservation.canCancelled()) {
      //TODO: 취소가 될 수 없는데 취소하려 함
      throw new IllegalArgumentException();
    }
    reservation.changeStatus(ReservationStatus.GUEST_CANCELLED);
  }

  public Slice<ReservationSummaryResponse> findByUserId(Long userId, Pageable pageable) {
    Slice<Reservation> reservationList = reservationRepository.findByUserIdOrderByCreatedAtDesc(
        userId, pageable);
    return new SliceImpl<>(
        reservationList.getContent().stream()
            .map(
                ReservationConverter::ofSummary).collect(Collectors.toList()),
        pageable,
        reservationList.hasNext());
  }
}
