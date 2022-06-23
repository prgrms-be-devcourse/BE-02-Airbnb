package com.prgrms.airbnb.domain.reservation.entity;//package com.prgrms.airbnb.reservation.domain;

public enum ReservationStatus {
  WAITED_OK("승인대기") {
    public ReservationStatus changeStatus(ReservationStatus newReservationStatus) {
      if (!(newReservationStatus.equals(ReservationStatus.ACCEPTED)
          || newReservationStatus.equals(ReservationStatus.ACCEPTED_BEFORE_CANCELLED))) {
        throw new IllegalArgumentException();
      }
      return newReservationStatus.equals(ReservationStatus.ACCEPTED) ?
          ReservationStatus.ACCEPTED : ReservationStatus.ACCEPTED_BEFORE_CANCELLED;
    }
  },
  ACCEPTED("승인완료") {
    public ReservationStatus changeStatus(ReservationStatus newReservationStatus) {
      //승인후 취소, 리뷰 대기
      if (!(newReservationStatus.equals(ReservationStatus.ACCEPTED_AFTER_CANCELLED)
          || newReservationStatus.equals(ReservationStatus.WAIT_REVIEW))) {
        throw new IllegalArgumentException();
      }
      return newReservationStatus.equals(ReservationStatus.ACCEPTED_AFTER_CANCELLED) ?
          ReservationStatus.ACCEPTED_AFTER_CANCELLED : ReservationStatus.WAIT_REVIEW;
    }
  },
  GUEST_CANCELLED("게스트취소") {
    public ReservationStatus changeStatus(ReservationStatus newReservationStatus) {
      return this;
    }
  },
  ACCEPTED_BEFORE_CANCELLED("승인전취소") {
    public ReservationStatus changeStatus(ReservationStatus newReservationStatus) {
      return this;
    }
  },
  ACCEPTED_AFTER_CANCELLED("승인후취소") {
    public ReservationStatus changeStatus(ReservationStatus newReservationStatus) {
      return this;
    }
  },
  WAIT_REVIEW("리뷰대기") {
    public ReservationStatus changeStatus(ReservationStatus newReservationStatus) {
      if (!(newReservationStatus.equals(ReservationStatus.COMPLETE))) {
        throw new IllegalArgumentException();
      }
      return ReservationStatus.COMPLETE;
    }
  },
  COMPLETE("완료") {
    public ReservationStatus changeStatus(ReservationStatus newReservationStatus) {
      return this;
    }
  };

  private String title;

  ReservationStatus(String title) {
    this.title = title;
  }

  public abstract ReservationStatus changeStatus(ReservationStatus newReservationStatus);
}
