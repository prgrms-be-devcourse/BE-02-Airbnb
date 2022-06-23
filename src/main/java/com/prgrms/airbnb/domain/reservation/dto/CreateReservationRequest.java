package com.prgrms.airbnb.domain.reservation.dto;

import com.prgrms.airbnb.domain.reservation.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequest {
    private ReservationStatus reservationStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer period;
    private Integer oneDayCharge;
    private Long userId;
    private Long roomId;
}
