package com.prgrms.airbnb.domain.room.service;

import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.review.event.AddReviewInfoEvent;
import com.prgrms.airbnb.domain.review.event.ChangeReviewInfoEvent;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReviewInfoService {

  private final RoomRepository roomRepository;

  public ReviewInfoService(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  @Transactional
  public void addReviewInfo(AddReviewInfoEvent event) {
    Room room = roomRepository.findById(event.getRoomId())
        .orElseThrow(() -> new NotFoundException(this.getClass().getName()));
    room.getReviewInfo().addReviewInfo(event.getRating());
  }

  @Transactional
  public void changeReviewInfo(ChangeReviewInfoEvent event) {
    Room room = roomRepository.findById(event.getRoomId())
        .orElseThrow(() -> new NotFoundException(this.getClass().getName()));
    room.getReviewInfo().changeReviewInfo(event.getOldRating(), event.getNewRating());
  }
}
