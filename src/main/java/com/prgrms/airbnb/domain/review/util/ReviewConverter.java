package com.prgrms.airbnb.domain.review.util;

import com.prgrms.airbnb.domain.review.dto.CreateReviewRequest;
import com.prgrms.airbnb.domain.review.entity.Review;

public class ReviewConverter {

  public static Review toReview(String reservationId,CreateReviewRequest createReviewRequest){
    return new Review(
        createReviewRequest.getComment(),
        createReviewRequest.getRating(),
        reservationId,
        createReviewRequest.getVisible(),
        createReviewRequest.getImages()
    );
  }


}
