package com.prgrms.airbnb.domain.room.entity;

import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public enum SortTypeForHost {

  RECENTLY {
    public Page<Room> findByHost(Long hostId, Pageable pageable, RoomRepository roomRepository) {
      return roomRepository.findByUserIdOrderByCreatedAt(hostId, pageable);
    }
  },
  RATING {
    public Page<Room> findByHost(Long hostId, Pageable pageable, RoomRepository roomRepository) {
      return roomRepository.findByHostIdOrderByRating(hostId, pageable);
    }
  };

  abstract public Page<Room> findByHost(Long hostId, Pageable pageable,
      RoomRepository roomRepository);
}
