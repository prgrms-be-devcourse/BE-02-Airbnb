package com.prgrms.airbnb.domain.review.event;

import lombok.Getter;

@Getter
public class UpdateReviewInfoEvent {

  private final Long roomId;
  private final Integer rating;

  public UpdateReviewInfoEvent(Long roomId, Integer rating) {
    this.roomId = roomId;
    this.rating = rating;
  }
}
