package com.prgrms.airbnb.domain.review.event;

import lombok.Getter;

@Getter
public class ChangeReviewInfoEvent {

  private final Long roomId;
  private final Integer oldRating;
  private final Integer newRating;

  public ChangeReviewInfoEvent(Long roomId, Integer oldRating, Integer newRating) {
    this.roomId = roomId;
    this.oldRating = oldRating;
    this.newRating = newRating;
  }
}
