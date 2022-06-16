package com.prgrms.airbnb.reservation.domain;

public enum ReservationStatus {
    WAITED_OK,
    ACCEPTED,
    REJECTED,
    GUEST_CANCELLED,
    HOST_CANCELLED,
    WAIT_REVIEW,
    COMPLETE
}
