package com.prgrms.airbnb.reservation.domain;

import com.prgrms.airbnb.common.model.BaseEntity;
import javax.persistence.criteria.CriteriaBuilder.In;
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

    private Integer totalPrice;

    @NotNull(message = "userId는 null 일 수 없습니다")
    private Long userId;

    @NotNull(message = "roomId는 null 일 수 없습니다")
    private Long roomId;

    @Builder
    public Reservation(String id, ReservationStatus reservationStatus, LocalDate startDate, LocalDate endDate, Integer period, Integer oneDayCharge, Long userId, Long roomId) {
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

    public void approval(ReservationStatus reservationStatus) {
        //TODO: 호스트만 접근이 가능해야 할 권한
        if (this.reservationStatus.equals(ReservationStatus.WAITED_OK)) {
            if (!(reservationStatus.equals(ReservationStatus.ACCEPTED)
                    || reservationStatus.equals(ReservationStatus.ACCEPTED_BEFORE_CANCELLED))) {
                //TODO: WAIT_OK 인데 적합하지 않은 Status 들어옴
                throw new IllegalArgumentException();
            }
            this.reservationStatus = reservationStatus.equals(ReservationStatus.ACCEPTED) ?
                    ReservationStatus.ACCEPTED : ReservationStatus.ACCEPTED_BEFORE_CANCELLED;
        } else if (reservationStatus.equals(ReservationStatus.ACCEPTED_AFTER_CANCELLED)) {//대기 상태가 아님 ACCEPTED_AFTER_CANCELLED,
            this.reservationStatus = reservationStatus;
        } else {
            //TODO: 승인대기상태가 아닌데 적합하지 않은 Status 들어옴
            throw new IllegalArgumentException();
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

    private void calculatePrice(Integer charge) {
        totalPrice = charge * period;
    }
}
