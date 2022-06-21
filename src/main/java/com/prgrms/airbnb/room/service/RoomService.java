package com.prgrms.airbnb.room.service;


import com.prgrms.airbnb.room.RoomConverter;
import com.prgrms.airbnb.room.domain.Room;
import com.prgrms.airbnb.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.room.dto.SortTypeForHost;
import com.prgrms.airbnb.room.dto.UpdateRoomRequest;
import com.prgrms.airbnb.room.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoomService {

  private final RoomRepository roomRepository;

  public RoomService(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  @Transactional
  public RoomDetailResponse save(CreateRoomRequest createRoomRequest) {
    Room room = RoomConverter.toRoom(createRoomRequest);
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

  public Page<RoomSummaryResponse> findByHostId(Long hostId, SortTypeForHost sortType, Pageable pageable) {
    return sortType.findByHost(hostId, pageable, roomRepository)
        .map(RoomConverter::ofSummary);
  }

  public void remove(Long id) {
    roomRepository.deleteById(id);
  }
}
