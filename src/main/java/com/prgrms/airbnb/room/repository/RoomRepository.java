package com.prgrms.airbnb.room.repository;

import com.prgrms.airbnb.room.domain.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomCustomRepository {

  Page<Room> findByUserIdOrderByCreatedAt(Long userId, Pageable pageable);

  @Query("select r from Room r where r.userId =:hostId order by r.reviewInfo.reviewRating")
  Page<Room> findByHostIdOrderByRating(@Param("hostId") Long hostId, Pageable pageable);
}
