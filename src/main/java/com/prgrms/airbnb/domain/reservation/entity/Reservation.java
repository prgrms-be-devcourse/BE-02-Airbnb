package com.prgrms.airbnb.domain.reservation.entity;

import com.prgrms.airbnb.domain.common.entity.BaseEntity;
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
  @Column(name = "id")
  private String id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "reservation_status")
  private ReservationStatus reservationStatus = ReservationStatus.WAITED_OK;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "term")
  private Integer term;

  @Column(name = "total_price")
  private Integer totalPrice;

  @NotNull(message = "userId는 null 일 수 없습니다")
  @Column(name = "user_id")
  private Long userId;

  @NotNull(message = "roomId는 null 일 수 없습니다")
  @Column(name = "room_id")
  private Long roomId;

  @Builder
  public Reservation(String id, ReservationStatus reservationStatus, LocalDate startDate,
      LocalDate endDate, Integer term, Integer oneDayCharge, Long userId, Long roomId) {
    setId(id);
    setReservationStatus(reservationStatus);
    setStartDate(startDate);
    setEndDate(endDate);
    setTerm(term);
    calculatePrice(oneDayCharge);
    setUserId(userId);
    setRoomId(roomId);
  }

  public void cancelReservation(ReservationStatus reservationStatus) {
    if (canCancelled()) {
      setReservationStatus(reservationStatus);
    }
  }

  public void approval(ReservationStatus newReservationStatus) {
    reservationStatus = reservationStatus.changeStatus(newReservationStatus);
  }

  public boolean canReviewed(){
    return reservationStatus.equals(ReservationStatus.WAIT_REVIEW);
  }

  public boolean canCancelled() {
    if (startDate.isAfter(LocalDate.now()) || startDate.isEqual(LocalDate.now())) {
      //TODO: 환불 정책 필요, 에러 추가 필요
      throw new IllegalArgumentException();
    }
    return this.reservationStatus.equals(ReservationStatus.WAITED_OK)
        || this.reservationStatus.equals(ReservationStatus.ACCEPTED);
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

  private void setTerm(Integer period) {
    if (ObjectUtils.isEmpty(period)) {
      throw new IllegalArgumentException();
    }
    this.term = period;
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
    totalPrice = charge * term;
  }
}
