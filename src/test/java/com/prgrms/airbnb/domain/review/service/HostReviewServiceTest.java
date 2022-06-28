package com.prgrms.airbnb.domain.review.service;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import com.prgrms.airbnb.domain.reservation.entity.ReservationStatus;
import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.domain.review.repository.ReviewRepository;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomInfo;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

class HostReviewServiceTest {

  Room room;
  Reservation reservation1, reservation2;
  @Autowired
  private HostReviewService hostReviewService;
  @Autowired
  private ReviewRepository reviewRepository;
  @Autowired
  private ReservationRepository reservationRepository;
  @Autowired
  private RoomRepository roomRepository;

  @BeforeEach
  void setUp() {
    room = new Room(new Address("1", "2"), 30000, "담양 떡갈비", "뷰가 좋습니다", new RoomInfo(1, 2, 3, 4),
        RoomType.HOUSE, List.of(new RoomImage("room path1")), 1L);
    roomRepository.save(room);
    reservation1 = new Reservation(reservationRepository.createReservationId(),
        ReservationStatus.WAITED_OK, LocalDate.of(2022, 6, 5), LocalDate.of(2022, 6, 8), 3, 30000,
        1L, room.getId());
    reservation2 = new Reservation(reservationRepository.createReservationId(),
        ReservationStatus.WAITED_OK, LocalDate.of(2022, 6, 10), LocalDate.of(2022, 6, 15), 3, 30000,
        1L, room.getId());
    reservationRepository.save(reservation1);
    reservationRepository.save(reservation2);
  }



}