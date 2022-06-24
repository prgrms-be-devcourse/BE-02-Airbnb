package com.prgrms.airbnb.domain.review.repository;

import com.prgrms.airbnb.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
