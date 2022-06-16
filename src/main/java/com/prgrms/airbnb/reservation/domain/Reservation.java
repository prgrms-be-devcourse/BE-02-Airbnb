package com.prgrms.airbnb.reservation.domain;

import com.prgrms.airbnb.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Access(AccessType.FIELD)
@Table(name = "reservations")
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private ReservationStatus reservationStatus;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private Integer period;

    private Long userId;

    private Long roomId;

    @Builder
    public Reservation(ReservationStatus reservationStatus, LocalDate checkIn, LocalDate checkOut, Integer period, Long userId, Long roomId) {
        this.reservationStatus = reservationStatus;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.period = period;
        this.userId = userId;
        this.roomId = roomId;
    }
}
