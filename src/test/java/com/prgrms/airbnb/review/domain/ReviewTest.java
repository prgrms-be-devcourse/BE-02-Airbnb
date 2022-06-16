package com.prgrms.airbnb.review.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReviewTest {
    @Test
    void review_created() {
        Review review = Review.builder()
                .comment("시설이 좋아요")
                .rating(5)
                .reservationId(3L)
                .build();
        Assertions.assertThat(review.getRating()).isEqualTo(5);
    }

}