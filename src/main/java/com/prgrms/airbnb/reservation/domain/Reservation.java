package com.prgrms.airbnb.reservation.domain;

import com.prgrms.airbnb.common.model.BaseEntity;
import com.prgrms.airbnb.room.domain.Room;
import com.prgrms.airbnb.user.domain.User;
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

    private ReservationStatus reservationStatus;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private Integer period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rooms_id")
    private Room room;

    @Builder
    public Reservation(ReservationStatus reservationStatus, LocalDate checkIn, LocalDate checkOut, Integer period, User user, Room room) {
        this.reservationStatus = reservationStatus;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.period = period;
        this.user = user;
        this.room = room;
    }
}
