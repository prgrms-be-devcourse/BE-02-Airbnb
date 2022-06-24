package com.prgrms.airbnb.domain.room.entity;

import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public enum SortTypeForHost {

  RECENTLY {
    public Slice<Room> findByHost(Long hostId, Pageable pageable, RoomRepository roomRepository) {
      return roomRepository.findByUserIdOrderByCreatedAt(hostId, pageable);
    }
  },
  RATING {
    public Slice<Room> findByHost(Long hostId, Pageable pageable, RoomRepository roomRepository) {
      return roomRepository.findByHostIdOrderByRating(hostId, pageable);
    }
  };

  abstract public Slice<Room> findByHost(Long hostId, Pageable pageable,
      RoomRepository roomRepository);
}
