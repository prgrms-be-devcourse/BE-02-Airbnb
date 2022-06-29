package com.prgrms.airbnb.domain.review.repository;

import com.prgrms.airbnb.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  @Query("select rv from Review rv left join Reservation rsv on rv.reservationId = rsv.id where rsv.roomId = :roomId order by rv.createdAt desc")
  Slice<Review> findAllByRoomId(@Param("roomId") Long roomId, Pageable pageable);
}
