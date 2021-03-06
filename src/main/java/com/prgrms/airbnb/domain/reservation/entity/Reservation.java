package com.prgrms.airbnb.domain.reservation.entity;

import com.prgrms.airbnb.domain.common.entity.BaseEntity;
import com.prgrms.airbnb.domain.common.exception.BadRequestException;
import com.prgrms.airbnb.domain.common.exception.InvalidParamException;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

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

  public Reservation(String id, ReservationStatus reservationStatus, LocalDate startDate,
      LocalDate endDate, Integer term, Integer oneDayCharge, Long userId, Long roomId) {
    setId(id);
    setReservationStatus(reservationStatus);
    setStartDate(startDate);
    setEndDate(endDate);
    checkDate(startDate, endDate);
    setTerm(term);
    calculatePrice(oneDayCharge);
    setUserId(userId);
    setRoomId(roomId);
  }

  public void changeStatus(ReservationStatus newReservationStatus) {
    reservationStatus = reservationStatus.changeStatus(newReservationStatus);
  }

  public boolean canReviewed() {
    if (validateDateForReview()) {
      return false;
    }
    return reservationStatus.equals(ReservationStatus.WAIT_REVIEW);
  }

  public boolean canCancelled(Long userId) {
    if (!validateDateForCancel() || !validateUserId(userId)) {
      //TODO: 환불 정책 필요
      throw new BadRequestException(this.getClass().getName());
    }
    return this.reservationStatus.equals(ReservationStatus.WAITED_OK)
        || this.reservationStatus.equals(ReservationStatus.ACCEPTED);
  }

  public boolean validateUserId(Long userId){
    return this.userId.equals(userId);
  }

  private void setId(String id) {
    if (ObjectUtils.isEmpty(id)) {
      throw new InvalidParamException("잘못된 reservationId 입력: " + id);
    }
    this.id = id;
  }

  private void setReservationStatus(ReservationStatus reservationStatus) {
    if (ObjectUtils.isEmpty(reservationStatus) || !(reservationStatus.equals(
        ReservationStatus.WAITED_OK))) {
      throw new InvalidParamException("잘못된 reservationStatus 입력: " + reservationStatus);
    }
  }

  private void setStartDate(LocalDate checkIn) {
    if (ObjectUtils.isEmpty(checkIn)) {
      throw new InvalidParamException("잘못된 startDate 입력: " + checkIn);
    }
    this.startDate = checkIn;
  }

  private void setEndDate(LocalDate checkOut) {
    if (ObjectUtils.isEmpty(checkOut)) {
      throw new InvalidParamException("잘못된 endDate 입력: " + checkOut);
    }
    this.endDate = checkOut;
  }

  private void setTerm(Integer period) {
    if (ObjectUtils.isEmpty(period) || period <= 0) {
      throw new InvalidParamException("잘못된 period 입력: " + period);
    }
    this.term = period;
  }

  private void setUserId(Long userId) {
    if (ObjectUtils.isEmpty(userId)) {
      throw new InvalidParamException("잘못된 userId 입력: " + userId);
    }
    this.userId = userId;
  }

  private void setRoomId(Long roomId) {
    if (ObjectUtils.isEmpty(roomId)) {
      throw new InvalidParamException("잘못된 roomId 입력: " + roomId);
    }
    this.roomId = roomId;
  }

  private void calculatePrice(Integer charge) {
    if (ObjectUtils.isEmpty(charge) || charge < 0) {
      throw new InvalidParamException("잘못된 charge 입력: " + charge);
    }
    totalPrice = charge * term;
  }

  private void checkDate(LocalDate startDate, LocalDate endDate) {
    if (LocalDate.now().isAfter(startDate) || startDate.isAfter(endDate)) {
      throw new InvalidParamException("잘못된 startDate, endDate 입력: " + startDate + " " + endDate);
    }
  }

  private boolean validateDateForCancel() {
    return startDate.isAfter(LocalDate.now()) || startDate.isEqual(LocalDate.now());
  }

  private boolean validateDateForReview() {
    return endDate.plusDays(14).isBefore(LocalDate.now());
  }
}
