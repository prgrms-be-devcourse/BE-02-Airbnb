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
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    private String id;

    @Enumerated(value = EnumType.STRING)
    private ReservationStatus reservationStatus = ReservationStatus.WAITED_OK;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer period;

    @Convert(converter = MoneyConverter.class)
    private Money totalPrice;

    @NotNull(message = "userId는 null 일 수 없습니다")
    private Long userId;

    @NotNull(message = "roomId는 null 일 수 없습니다")
    private Long roomId;

    @Builder
    public Reservation(String id, ReservationStatus reservationStatus, LocalDate startDate, LocalDate endDate, Integer period, Money oneDayCharge, Long userId, Long roomId) {
        setId(id);
        setReservationStatus(reservationStatus);
        setStartDate(startDate);
        setEndDate(endDate);
        setPeriod(period);
        calculatePrice(oneDayCharge);
        setUserId(userId);
        setRoomId(roomId);
    }

    public void cancelReservation(ReservationStatus reservationStatus) {
        if (canCancelled()) {
            setReservationStatus(reservationStatus);
        }
    }

    private boolean canCancelled() {
        if (startDate.isAfter(LocalDate.now()) || startDate.isEqual(LocalDate.now())) {
            //TODO: 환불 정책 필요, 에러 추가 필요
            throw new IllegalArgumentException();
        }
        return this.reservationStatus.equals(ReservationStatus.WAITED_OK) || this.reservationStatus.equals(ReservationStatus.ACCEPTED);
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
    }

    private void setStartDate(LocalDate checkIn) {
        if (ObjectUtils.isEmpty(checkIn)) {
            throw new IllegalArgumentException();
        }
        this.startDate = checkIn;
    }

    private void setEndDate(LocalDate checkOut) {
        if (ObjectUtils.isEmpty(checkOut)) {
            throw new IllegalArgumentException();
        }
        this.endDate = checkOut;
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
