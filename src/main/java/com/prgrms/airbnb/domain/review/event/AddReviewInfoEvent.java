package com.prgrms.airbnb.domain.review.event;

import lombok.Getter;

@Getter
public class AddReviewInfoEvent {

  private final Long roomId;
  private final Integer rating;

  public AddReviewInfoEvent(Long roomId, Integer rating) {
    this.roomId = roomId;
    this.rating = rating;
  }
}
