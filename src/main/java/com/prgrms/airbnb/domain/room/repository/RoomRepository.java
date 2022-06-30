package com.prgrms.airbnb.domain.room.repository;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.room.entity.Room;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomSearchRepository {

  Boolean existsByAddress(Address address);

  List<Room> findAllByUserId(Long userId);

  Slice<Room> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

  @Query("select r from Room r where r.userId =:hostId order by r.reviewInfo.reviewRating desc")
  Slice<Room> findByHostIdOrderByRating(@Param("hostId") Long hostId, Pageable pageable);
}
