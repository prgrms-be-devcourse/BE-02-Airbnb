package com.prgrms.airbnb.domain.review.service;

import com.prgrms.airbnb.domain.common.exception.BadRequestException;
import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.common.exception.UnAuthorizedAccessException;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class GuestReviewService {

  private final ReviewRepository reviewRepository;
  private final ReservationRepository reservationRepository;
  private final RoomRepository roomRepository;
  private final UploadService uploadService;

  public GuestReviewService(ReviewRepository reviewRepository,
      ReservationRepository reservationRepository, RoomRepository roomRepository,
      UploadService uploadService) {
    this.reviewRepository = reviewRepository;
    this.reservationRepository = reservationRepository;
    this.roomRepository = roomRepository;
    this.uploadService = uploadService;
  }

  @Transactional
  public ReviewResponse save(Long userId, String reservationId,
      CreateReviewRequest createReviewRequest, List<MultipartFile> images) {
    Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> {
      throw new NotFoundException(this.getClass().getName());
    });
    validateAuthority(userId, reservation.getUserId());
    if (!reservation.canReviewed()) {
      throw new BadRequestException(this.getClass().getName());
    }
    Review review = ReviewConverter.toReview(reservationId, createReviewRequest);
    uploadNewImages(images, review);
    Review savedReview = reviewRepository.save(review);
    reservation.changeStatus(ReservationStatus.COMPLETE);
    Room room = roomRepository.findById(reservation.getRoomId()).orElseThrow(() -> {
      throw new NotFoundException(this.getClass().getName());
    });
    room.getReviewInfo().updateReviewInfo(createReviewRequest.getRating());
    return ReviewConverter.of(savedReview);
  }

  @Transactional
  public ReviewResponse modify(Long userId, Long reviewId, UpdateReviewRequest updateReviewRequest,
      List<MultipartFile> images) {
    Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
      throw new NotFoundException(this.getClass().getName());
    });
    Reservation reservation = reservationRepository.findById(review.getReservationId())
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    validateAuthority(userId, reservation.getUserId());
    review.changeComment(updateReviewRequest.getComment());
    review.changeRating(updateReviewRequest.getRating());
    review.changeVisible(updateReviewRequest.getVisible());
    deleteImages(updateReviewRequest, review);
    uploadNewImages(images, review);
    Room room = roomRepository.findById(reservation.getRoomId()).orElseThrow(() -> {
      throw new NotFoundException(this.getClass().getName());
    });
    room.getReviewInfo().changeReviewInfo(review.getRating(), updateReviewRequest.getRating());
    return ReviewConverter.of(review);
  }

  public Slice<ReviewResponse> findAllByRoomId(Long userId, Long roomId, Pageable pageable) {
    Slice<Review> reviewList = reviewRepository.findAllByRoomIdForGuest(roomId, userId, pageable);
    return reviewList.map(ReviewConverter::of);
  }

  @Transactional
  public void remove(Long userId, Long reviewId) {
    Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
      throw new NotFoundException(this.getClass().getName());
    });
    Reservation reservation = reservationRepository.findById(review.getReservationId())
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    validateAuthority(userId, reservation.getUserId());
    review.getImages().forEach(reviewImage -> uploadService.delete(reviewImage.getPath()));
    reviewRepository.delete(review);
  }

  private void validateAuthority(Long authenticationUserId, Long userId) {
    if (!authenticationUserId.equals(userId)) {
      throw new UnAuthorizedAccessException(this.getClass().getName());
    }
  }

  private void uploadNewImages(List<MultipartFile> images, Review review) {
    Optional.ofNullable(images).orElseGet(Collections::emptyList).stream()
        .map(image -> uploadService.uploadImg(image)).map(path -> new ReviewImage(path))
        .forEach(review::addImage);
  }

  private void deleteImages(UpdateReviewRequest updateReviewRequest, Review review) {
    Optional.ofNullable(review.getImages()).orElseGet(Collections::emptyList).stream().filter(
            Predicate.not(reviewImage -> Optional.ofNullable(updateReviewRequest.getImages())
                .orElseGet(Collections::emptyList).stream().map(ReviewImage::getId)
                .anyMatch(imageId -> imageId.equals(reviewImage.getId())))).filter(Objects::nonNull)
        .collect(Collectors.toList()).forEach(reviewImage -> {
          uploadService.delete(reviewImage.getPath());
          reviewImage.deleteReview();
        });
  }
}
