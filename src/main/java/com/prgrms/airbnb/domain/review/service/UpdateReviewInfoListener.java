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
public class UpdateReviewInfoListener {

  private final RoomRepository roomRepository;

  public UpdateReviewInfoListener(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  @Async
  @EventListener(UpdateReviewInfoEvent.class)
  public void handleContextStart(UpdateReviewInfoEvent event) {
    Room room = roomRepository.findById(event.getRoomId()).orElseThrow(() -> {
      throw new NotFoundException(this.getClass().getName());
    });
    room.getReviewInfo().updateReviewInfo(event.getRating());
  }
}