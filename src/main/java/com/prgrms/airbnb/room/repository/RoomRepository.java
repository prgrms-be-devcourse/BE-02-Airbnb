package com.prgrms.airbnb.room.repository;

import com.prgrms.airbnb.room.domain.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

  Page<Room> findAll(Pageable pageable);
}
