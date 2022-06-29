package com.prgrms.airbnb.domain.reservation.service;

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
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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

  public ReservationDetailResponseForHost findDetailById(String reservationId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(IllegalArgumentException::new);
    User guest = userRepository.findById(reservation.getUserId())
        .orElseThrow(IllegalArgumentException::new);
    Room room = roomRepository.findById(reservation.getRoomId())
        .orElseThrow(IllegalArgumentException::new);
    return ReservationConverter.ofDetailForHost(reservation, guest, room);
  }

  public Slice<ReservationSummaryResponse> findByHostId(Long userId, Pageable pageable) {
    //TODO: 호스트의 ID 입력시 예약 list 가져오기 수정 필요함
    Slice<Reservation> reservationList = reservationRepository.findByUserIdOrderByCreatedAtDesc(
        userId, pageable);
    return new SliceImpl<>(
        reservationList.getContent().stream()
            .map(
                ReservationConverter::ofSummary).collect(Collectors.toList()),
        pageable,
        reservationList.hasNext());
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
    reservation.changeStatus(reservationStatus);
  }

}
