package com.prgrms.airbnb.domain.review.service;

import com.prgrms.airbnb.domain.common.service.UploadService;
import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import com.prgrms.airbnb.domain.reservation.entity.ReservationStatus;
import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.domain.review.dto.CreateReviewRequest;
import com.prgrms.airbnb.domain.review.dto.ReviewResponse;
import com.prgrms.airbnb.domain.review.dto.UpdateReviewRequest;
import com.prgrms.airbnb.domain.review.entity.Review;
import com.prgrms.airbnb.domain.review.entity.ReviewImage;
import com.prgrms.airbnb.domain.review.repository.ReviewRepository;
import com.prgrms.airbnb.domain.review.util.ReviewConverter;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import com.prgrms.airbnb.domain.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final ReservationRepository reservationRepository;

  private final RoomRepository roomRepository;
  private final UploadService uploadService;

  public ReviewService(ReviewRepository reviewRepository,
      ReservationRepository reservationRepository, RoomRepository roomRepository,
      UploadService uploadService) {
    this.reviewRepository = reviewRepository;
    this.reservationRepository = reservationRepository;
    this.roomRepository = roomRepository;
    this.uploadService = uploadService;
  }

  @Transactional
  public ReviewResponse save(String reservationId, CreateReviewRequest createReviewRequest,
      List<MultipartFile> images) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(IllegalArgumentException::new);
    if (!reservation.canReviewed()) {
      //TODO: 리뷰를 남길 수 없는 경우.
      throw new IllegalArgumentException();
    }
    List<ReviewImage> imageList = images.stream()
        .map(image -> uploadService.uploadImg(image))
        .map(path -> new ReviewImage(path))
        .collect(Collectors.toList());
    Review review = ReviewConverter.toReview(reservationId, createReviewRequest, imageList);
    Review savedReview = reviewRepository.save(review);
    reservation.changeStatus(ReservationStatus.COMPLETE);
    Room room = roomRepository.findById(reservation.getRoomId())
        .orElseThrow(IllegalArgumentException::new);
    room.getReviewInfo().updateReviewInfo(createReviewRequest.getRating());
    return ReviewConverter.of(savedReview);
  }

  @Transactional
  public ReviewResponse modify(Long reviewId, UpdateReviewRequest updateReviewRequest,
      List<MultipartFile> images) {
    Review review = reviewRepository.findById(reviewId).orElseThrow(IllegalArgumentException::new);
    Reservation reservation = reservationRepository.findById(review.getReservationId())
        .orElseThrow(IllegalArgumentException::new);
    Room room = roomRepository.findById(reservation.getRoomId())
        .orElseThrow(IllegalArgumentException::new);
    room.getReviewInfo().changeReviewInfo(review.getRating(), updateReviewRequest.getRating());
    review.changeComment(updateReviewRequest.getComment());
    review.changeRating(updateReviewRequest.getRating());
    review.changeVisible(updateReviewRequest.getVisible());
    review.getImages().removeIf(reviewImage -> !images.contains(reviewImage));
    //images.forEach(review::changeImage);
    return ReviewConverter.of(review);
  }

  public List<ReviewResponse> findAllByRoomId(Long roomId, User user) {
    List<Review> reviewList = reviewRepository.findAllByRoomId(roomId);

    //HINT 리뷰 작성자가 아니면서 익명 글일땐 List에 추가하지 않는다.
    return reviewList.stream()
        .filter(review -> {
          Reservation reservation = reservationRepository.findById(review.getReservationId())
              .orElseThrow(IllegalArgumentException::new);
          if (!review.isVisible() && !reservation.getUserId().equals(user.getId())) {
            return false;
          }
          return true;
        })
        .map(ReviewConverter::of).collect(Collectors.toList());
  }

  @Transactional
  public void remove(Long reviewId) {
    Review review = reviewRepository.findById(reviewId).orElseThrow(IllegalArgumentException::new);
    review.getImages().forEach(reviewImage -> uploadService.delete(reviewImage.getPath()));
    reviewRepository.delete(review);
  }


}
