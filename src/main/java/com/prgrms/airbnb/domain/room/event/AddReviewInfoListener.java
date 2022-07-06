package com.prgrms.airbnb.domain.room.event;

import com.prgrms.airbnb.domain.review.event.AddReviewInfoEvent;
import com.prgrms.airbnb.domain.room.service.ReviewInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class AddReviewInfoListener {

  private final ReviewInfoService reviewInfoService;

  public AddReviewInfoListener(ReviewInfoService reviewInfoService) {
    this.reviewInfoService = reviewInfoService;
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = AddReviewInfoEvent.class)
  public void handleContextStart(AddReviewInfoEvent event) {
    reviewInfoService.addReviewInfo(event);
  }
}