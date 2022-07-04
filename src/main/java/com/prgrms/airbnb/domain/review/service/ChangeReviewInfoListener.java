package com.prgrms.airbnb.domain.review.service;

import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChangeReviewInfoListener {

  private final RoomRepository roomRepository;

  public ChangeReviewInfoListener(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  @Async
  @EventListener(ChangeReviewInfoEvent.class)
  public void handleContextStart(ChangeReviewInfoEvent event) {
    Room room = roomRepository.findById(event.getRoomId()).orElseThrow(() -> {
      throw new NotFoundException(this.getClass().getName());
    });
    room.getReviewInfo().changeReviewInfo(event.getOldRating(), event.getNewRating());
  }
}