package com.prgrms.airbnb.domain.room.event;

import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.review.event.UpdateReviewInfoEvent;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class UpdateReviewInfoListener {

  private final RoomRepository roomRepository;

  public UpdateReviewInfoListener(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = UpdateReviewInfoEvent.class)
  public void handleContextStart(UpdateReviewInfoEvent event) {
    Room room = roomRepository.findById(event.getRoomId())
        .orElseThrow(() -> new NotFoundException(this.getClass().getName()));
    room.getReviewInfo().updateReviewInfo(event.getRating());
  }
}