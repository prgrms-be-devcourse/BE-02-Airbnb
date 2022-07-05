package com.prgrms.airbnb.domain.room.service;

import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.dto.SearchRoomRequest;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import com.prgrms.airbnb.domain.room.util.RoomConverter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoomServiceForGuest {

  private final RoomRepository roomRepository;

  public RoomServiceForGuest(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public RoomDetailResponse findDetailById(Long id) {
    Room room = roomRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("[" + this.getClass().getName() + "] 해당 id의 room을 찾을 수 없습니다."));
    return RoomConverter.ofDetail(room);
  }

  public Slice<RoomSummaryResponse> findAll(SearchRoomRequest searchRoomRequest,
      Pageable pageable) {
    return roomRepository.searchRoom(searchRoomRequest, pageable);
  }
}
