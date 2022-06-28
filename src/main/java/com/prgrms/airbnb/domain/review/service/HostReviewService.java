package com.prgrms.airbnb.domain.review.service;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.domain.review.dto.ReviewResponse;
import com.prgrms.airbnb.domain.review.entity.Review;
import com.prgrms.airbnb.domain.review.repository.ReviewRepository;
import com.prgrms.airbnb.domain.review.util.ReviewConverter;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import com.prgrms.airbnb.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HostReviewService {

  private final ReviewRepository reviewRepository;
  private final ReservationRepository reservationRepository;
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;

  public HostReviewService(ReviewRepository reviewRepository,
      ReservationRepository reservationRepository, RoomRepository roomRepository,
      UserRepository userRepository) {
    this.reviewRepository = reviewRepository;
    this.reservationRepository = reservationRepository;
    this.roomRepository = roomRepository;
    this.userRepository = userRepository;
  }

  public List<ReviewResponse> findAllByRoomId(
      @AuthenticationPrincipal JwtAuthentication authentication, Long roomId) {
    Room room = roomRepository.findById(roomId).orElseThrow(IllegalArgumentException::new);
    validateAuthority(authentication, room.getUserId());
    List<Review> reviewList = reviewRepository.findAllByRoomId(roomId);
    return reviewList.stream().map(ReviewConverter::of).collect(Collectors.toList());
  }

  private void validateAuthority(@AuthenticationPrincipal JwtAuthentication authentication,
      Long userId) {
    if (!authentication.userId.equals(userId)) {
      throw new IllegalArgumentException();
    }
  }
}
