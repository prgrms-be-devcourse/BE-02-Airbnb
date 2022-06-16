package com.prgrms.airbnb.reservation.domain;

import com.prgrms.airbnb.common.jpa.MoneyConverter;
import com.prgrms.airbnb.common.model.BaseEntity;
import com.prgrms.airbnb.common.model.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Access(AccessType.FIELD)
@Table(name = "reservation")
public class Reservation extends BaseEntity {

    @Id
    private String id;

    @Enumerated(value = EnumType.STRING)
    private ReservationStatus reservationStatus;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private Integer period;

    @Convert(converter = MoneyConverter.class)
    private Money totalPrice;

    @NotNull(message = "userId는 null 일 수 없습니다")
    private Long userId;

    @NotNull(message = "roomId는 null 일 수 없습니다")
    private Long roomId;

    @Builder
    public Reservation(String id, ReservationStatus reservationStatus, LocalDate checkIn, LocalDate checkOut, Integer period, Money charge, Long userId, Long roomId) {
        setId(id);
        setReservationStatus(reservationStatus);
        setCheckIn(checkIn);
        setCheckOut(checkOut);
        setPeriod(period);
        calculatePrice(charge);
        setUserId(userId);
        setRoomId(roomId);
    }

    private void setId(String id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    private void setReservationStatus(ReservationStatus reservationStatus) {
        if (ObjectUtils.isEmpty(reservationStatus)) {
            throw new IllegalArgumentException();
        }
        this.reservationStatus = reservationStatus;
    }

    private void setCheckIn(LocalDate checkIn) {
        if (ObjectUtils.isEmpty(checkIn)) {
            throw new IllegalArgumentException();
        }
        this.checkIn = checkIn;
    }

    private void setCheckOut(LocalDate checkOut) {
        if (ObjectUtils.isEmpty(checkOut)) {
            throw new IllegalArgumentException();
        }
        this.checkOut = checkOut;
    }

    private void setPeriod(Integer period) {
        if (ObjectUtils.isEmpty(period)) {
            throw new IllegalArgumentException();
        }
        this.period = period;
    }

    private void setUserId(Long userId) {
        if (ObjectUtils.isEmpty(userId)) {
            throw new IllegalArgumentException();
        }
        this.userId = userId;
    }

    private void setRoomId(Long roomId) {
        if (ObjectUtils.isEmpty(roomId)) {
            throw new IllegalArgumentException();
        }
        this.roomId = roomId;
    }

    private void calculatePrice(Money charge) {
        totalPrice = charge.multiply(period);
    }
}
