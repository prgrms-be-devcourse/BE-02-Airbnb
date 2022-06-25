package com.prgrms.airbnb.domain.review.repository;

import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.domain.review.entity.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;


    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("review 객체 생성: 빈 이미지인 경우")
    void empty_image_save() {
        Review review = new Review(
                "여기 좋아용",
                5,
                "245325",
                true,
                null
        );
        reviewRepository.save(review);
        Assertions.assertThat(review.getId()).isNotNull();
    }
}