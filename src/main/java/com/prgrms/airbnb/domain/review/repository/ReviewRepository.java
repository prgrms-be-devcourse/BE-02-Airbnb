package com.prgrms.airbnb.domain.review.repository;

import com.prgrms.airbnb.domain.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  @Query("select rv from Review rv left join Reservation rsv on rv.reservationId = rsv.id where rsv.roomId = :roomId order by rv.createdAt desc")
  List<Review> findAllByRoomId(@Param("roomId") Long roomId);
}
