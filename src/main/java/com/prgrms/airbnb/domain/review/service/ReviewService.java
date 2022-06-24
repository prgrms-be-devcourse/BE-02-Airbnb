package com.prgrms.airbnb.domain.review.service;

import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.domain.review.dto.CreateReviewRequest;
import com.prgrms.airbnb.domain.review.dto.ReviewResponse;
import com.prgrms.airbnb.domain.review.dto.UpdateReviewRequest;
import com.prgrms.airbnb.domain.review.entity.Review;
import com.prgrms.airbnb.domain.review.repository.ReviewRepository;
import com.prgrms.airbnb.domain.review.util.ReviewConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         ReservationRepository reservationRepository) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReviewResponse save(String reservationId, CreateReviewRequest createReviewRequest) {
        //TODO: 리턴 타입 지정해야함
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(IllegalArgumentException::new);
        if (!reservation.canReviewed()) {
            throw new IllegalArgumentException();
        }
        Review review = ReviewConverter.toReview(reservationId, createReviewRequest);
        Review savedReview = reviewRepository.save(review);
        return ReviewConverter.of(savedReview);
    }

    @Transactional
    public ReviewResponse modify(Long reviewId, UpdateReviewRequest updateReviewRequest) {
        //TODO: 리턴 타입 지정해야함
        Review review = reviewRepository.findById(reviewId).orElseThrow(IllegalArgumentException::new);
        review.changeComment(updateReviewRequest.getComment());
        review.changeRating(updateReviewRequest.getRating());
        review.changeVisible(updateReviewRequest.getVisible());
        review.changeImage(updateReviewRequest.getImages());
        return ReviewConverter.of(review);
    }

    @Transactional
    public void remove(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }


}
