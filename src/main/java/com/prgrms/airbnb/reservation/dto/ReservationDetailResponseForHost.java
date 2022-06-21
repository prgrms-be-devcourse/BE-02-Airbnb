package com.prgrms.airbnb.reservation.dto;

import com.prgrms.airbnb.common.model.Money;
import com.prgrms.airbnb.reservation.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailResponseForHost {
    private String id;
    private ReservationStatus reservationStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer period;
    private Money totalPrice;
    private UserResponseForReservation guest;
    private RoomResponseForReservation roomResponseForReservation;
}
