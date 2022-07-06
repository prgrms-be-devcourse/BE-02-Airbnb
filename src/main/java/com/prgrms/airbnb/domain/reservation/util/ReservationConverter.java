package com.prgrms.airbnb.domain.reservation.util;

import com.prgrms.airbnb.domain.reservation.dto.CreateReservationRequest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForGuest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForHost;
import com.prgrms.airbnb.domain.reservation.dto.ReservationSummaryResponse;
import com.prgrms.airbnb.domain.reservation.dto.RoomResponseForReservation;
import com.prgrms.airbnb.domain.reservation.dto.UserResponseForReservation;
import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.user.entity.User;

public class ReservationConverter {

  public static Reservation toReservation(String reservationNo,
      CreateReservationRequest createReservationRequest) {
    return new Reservation(reservationNo,
        createReservationRequest.getReservationStatus(),
        createReservationRequest.getStartDate(),
        createReservationRequest.getEndDate(),
        createReservationRequest.getPeriod(),
        createReservationRequest.getOneDayCharge(),
        createReservationRequest.getUserId(),
        createReservationRequest.getRoomId()
        );
  }

  public static ReservationDetailResponseForHost ofDetailForHost(Reservation reservation,
      User guest, Room room) {
    return ReservationDetailResponseForHost.builder()
        .id(reservation.getId())
        .reservationStatus(reservation.getReservationStatus())
        .startDate(reservation.getStartDate())
        .endDate(reservation.getEndDate())
        .period(reservation.getTerm())
        .totalPrice(reservation.getTotalPrice())
        .guest(new UserResponseForReservation(guest.getId(), guest.getName(), guest.getEmail(),
            guest.getPhone()))
        .roomResponseForReservation(
            new RoomResponseForReservation(room.getId(), room.getName(), room.getAddress()))
        .build();
  }

  public static ReservationDetailResponseForGuest ofDetailForGuest(Reservation reservation,
      User host, Room room) {
    return ReservationDetailResponseForGuest.builder()
        .id(reservation.getId())
        .reservationStatus(reservation.getReservationStatus())
        .startDate(reservation.getStartDate())
        .endDate(reservation.getEndDate())
        .period(reservation.getTerm())
        .totalPrice(reservation.getTotalPrice())
        .host(new UserResponseForReservation(host.getId(), host.getName(), host.getEmail(),
            host.getPhone()))
        .roomResponseForReservation(
            new RoomResponseForReservation(room.getId(), room.getName(), room.getAddress()))
        .build();
  }

  public static ReservationSummaryResponse ofSummary(Reservation reservation) {
    return ReservationSummaryResponse.builder()
        .id(reservation.getId())
        .reservationStatus(reservation.getReservationStatus())
        .startDate(reservation.getStartDate())
        .endDate(reservation.getEndDate())
        .period(reservation.getTerm())
        .totalPrice(reservation.getTotalPrice())
        .build();
  }
}
