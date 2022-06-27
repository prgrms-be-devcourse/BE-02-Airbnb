package com.prgrms.airbnb.domain.room.service;

import com.prgrms.airbnb.domain.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.dto.UpdateRoomRequest;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.entity.SortTypeForHost;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import com.prgrms.airbnb.domain.room.util.RoomConverter;
import com.prgrms.airbnb.domain.user.entity.User;
import com.prgrms.airbnb.domain.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoomServiceForHost {

  private final RoomRepository roomRepository;
  private final UserRepository userRepository;

  public RoomServiceForHost(RoomRepository roomRepository, UserRepository userRepository) {
    this.roomRepository = roomRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public RoomDetailResponse save(CreateRoomRequest createRoomRequest, Long hostId) {
    //// TODO: 2022/06/24 같은 주소의 집이 새로 저장되면 안될것 같아 추가했습니다.
    if (roomRepository.existsByAddress(createRoomRequest.getAddress())) {
      throw new RuntimeException("기존에 등록된 주소가 있습니다.");
    }
    User user = userRepository.findById(hostId)
        .orElseThrow(RuntimeException::new);
    Room room = RoomConverter.toRoom(createRoomRequest, user);
    Room savedRoom = roomRepository.save(room);
    return RoomConverter.ofDetail(savedRoom);
  }

  @Transactional
  public RoomDetailResponse modify(UpdateRoomRequest updateRoomRequest, Long hostId) {
    Room room = roomRepository.findById(updateRoomRequest.getId())
        .orElseThrow(IllegalArgumentException::new);
    validateHost(room, hostId);

    room.setName(updateRoomRequest.getName());
    room.setCharge(updateRoomRequest.getCharge());
    room.setDescription(updateRoomRequest.getDescription());

    // TODO: 2022/06/24 새로 추가되는 이미지에 포함되지 않는 이미지의 연관관계 제거
    room.getImages().removeIf(roomImage -> !updateRoomRequest.getImages().contains(roomImage));
    updateRoomRequest.getImages().forEach(room::setImage);

    room.getRoomInfo().setMaxGuest(updateRoomRequest.getMaxGuest());
    room.getRoomInfo().setBedCount(updateRoomRequest.getBedCount());
    return RoomConverter.ofDetail(room);
  }

  public RoomDetailResponse findDetailById(Long id, Long hostId) {
    Room room = roomRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("해당 id를 가진 room이 존재하지 않습니다."));
    validateHost(room, hostId);
    return RoomConverter.ofDetail(room);
  }

  public Slice<RoomSummaryResponse> findByHostId(Long hostId, SortTypeForHost sortType,
      Pageable pageable) {
    return sortType.findByHost(hostId, pageable, roomRepository)
        .map(RoomConverter::ofSummary);
  }

  public void remove(Long roomId, Long hostId) {
    Room room = roomRepository.findById(roomId)
        .orElseThrow(RuntimeException::new);
    validateHost(room, hostId);
    roomRepository.deleteById(roomId);
  }

  private void validateHost(Room room, Long hostId) {
    if (!room.getUserId().equals(hostId)) {
      throw new RuntimeException("잘못된 접근 [room의 host가 아님]");
    }
  }
}
