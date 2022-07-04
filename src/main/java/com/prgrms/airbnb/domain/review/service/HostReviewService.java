package com.prgrms.airbnb.domain.review.service;

import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.common.exception.UnAuthorizedAccessException;
import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.domain.review.dto.ReviewResponse;
import com.prgrms.airbnb.domain.review.entity.Review;
import com.prgrms.airbnb.domain.review.repository.ReviewRepository;
import com.prgrms.airbnb.domain.review.util.ReviewConverter;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HostReviewService {

  private final ReviewRepository reviewRepository;
  private final ReservationRepository reservationRepository;
  private final RoomRepository roomRepository;

  public HostReviewService(ReviewRepository reviewRepository,
      ReservationRepository reservationRepository, RoomRepository roomRepository) {
    this.reviewRepository = reviewRepository;
    this.reservationRepository = reservationRepository;
    this.roomRepository = roomRepository;
  }

  public Slice<ReviewResponse> findAllByRoomId(Long userId, Long roomId, Pageable pageable) {
    Room room = roomRepository.findById(roomId).orElseThrow(() -> {
      throw new NotFoundException(this.getClass().getName());
    });
    validateAuthority(userId, room.getUserId());
    Slice<Review> reviewList = reviewRepository.findAllByRoomIdForHost(roomId, pageable);
    return reviewList.map(ReviewConverter::of);
  }

  private void validateAuthority(Long authenticationUserId, Long userId) {
    if (!authenticationUserId.equals(userId)) {
      throw new UnAuthorizedAccessException(this.getClass().getName());
    }
  }
}
