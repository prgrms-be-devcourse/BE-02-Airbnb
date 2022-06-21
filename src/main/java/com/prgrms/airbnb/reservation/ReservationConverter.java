package com.prgrms.airbnb.reservation;

import com.prgrms.airbnb.reservation.domain.Reservation;
import com.prgrms.airbnb.reservation.dto.*;
import com.prgrms.airbnb.room.domain.Room;
import com.prgrms.airbnb.user.domain.User;

public class ReservationConverter {
    public static Reservation toReservation(String reservationNo, CreateReservationRequest createReservationRequest) {
        return Reservation.builder()
                .id(reservationNo)
                .reservationStatus(createReservationRequest.getReservationStatus())
                .startDate(createReservationRequest.getStartDate())
                .endDate(createReservationRequest.getEndDate())
                .period(createReservationRequest.getPeriod())
                .oneDayCharge(createReservationRequest.getOneDayCharge())
                .userId(createReservationRequest.getUserId())
                .roomId(createReservationRequest.getRoomId())
                .build();
    }

    public static ReservationDetailResponseForHost ofDetailForHost(Reservation reservation, User guest, Room room) {
        return ReservationDetailResponseForHost.builder()
                .id(reservation.getId())
                .reservationStatus(reservation.getReservationStatus())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .period(reservation.getPeriod())
                .totalPrice(reservation.getTotalPrice())
                .guest(new UserResponseForReservation(guest.getId(), guest.getName(), guest.getEmail(), guest.getPhone()))
                .roomResponseForReservation(new RoomResponseForReservation(room.getId(), room.getName(), room.getAddress()))
                .build();
    }

    public static ReservationDetailResponseForGuest ofDetailForGuest(Reservation reservation, User host, Room room) {
        return ReservationDetailResponseForGuest.builder()
                .id(reservation.getId())
                .reservationStatus(reservation.getReservationStatus())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .period(reservation.getPeriod())
                .totalPrice(reservation.getTotalPrice())
                .host(new UserResponseForReservation(host.getId(), host.getName(), host.getEmail(), host.getPhone()))
                .roomResponseForReservation(new RoomResponseForReservation(room.getId(), room.getName(), room.getAddress()))
                .build();
    }
}
