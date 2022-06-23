package com.prgrms.airbnb.domain.room.service;

import com.prgrms.airbnb.domain.room.dto.*;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.entity.SortTypeForHost;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import com.prgrms.airbnb.domain.room.util.RoomConverter;
import com.prgrms.airbnb.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

  private final RoomRepository roomRepository;

  @Transactional
  public RoomDetailResponse save(CreateRoomRequest createRoomRequest, User user) {
    Room room = RoomConverter.getRoomFrom(createRoomRequest, user);
    Room savedRoom = roomRepository.save(room);
    return RoomConverter.ofDetail(savedRoom);
  }

  @Transactional
  public RoomDetailResponse modify(UpdateRoomRequest updateRoomRequest) {
    Room room = roomRepository.findById(updateRoomRequest.getId())
        .orElseThrow(IllegalArgumentException::new);
    room.changeName(updateRoomRequest.getName());
    room.changeCharge(updateRoomRequest.getCharge());
    room.getRoomInfo().changeMaxGuest(updateRoomRequest.getRoomInfo().getMaxGuest());
    room.getRoomInfo().changeBedCount(updateRoomRequest.getRoomInfo().getBedCount());
    room.changeImages(updateRoomRequest.getImages());
    room.changeDescription(updateRoomRequest.getDescription());
    return RoomConverter.ofDetail(room);
  }

  public RoomDetailResponse findDetailById(Long id) {
    Room room = roomRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("해당 id를 가진 room이 존재하지 않습니다."));

    return RoomConverter.ofDetail(room);
  }

  public Page<RoomSummaryResponse> findByHostId(Long hostId, SortTypeForHost sortType,
      Pageable pageable) {
    return sortType.findByHost(hostId, pageable, roomRepository)
        .map(RoomConverter::ofSummary);
  }

  public void remove(Long id) {
    roomRepository.deleteById(id);
  }
}
