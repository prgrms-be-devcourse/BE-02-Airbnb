package com.prgrms.airbnb.domain.reservation.entity;//package com.prgrms.airbnb.reservation.domain;

public enum ReservationStatus {
    WAITED_OK,
    ACCEPTED,
    GUEST_CANCELLED,
    ACCEPTED_BEFORE_CANCELLED,
    ACCEPTED_AFTER_CANCELLED,
    WAIT_REVIEW,
    COMPLETE
}
