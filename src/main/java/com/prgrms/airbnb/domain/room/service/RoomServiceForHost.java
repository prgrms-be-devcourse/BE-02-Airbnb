package com.prgrms.airbnb.domain.room.service;

import com.prgrms.airbnb.domain.common.service.UploadService;
import com.prgrms.airbnb.domain.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.dto.UpdateRoomRequest;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.SortTypeForHost;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import com.prgrms.airbnb.domain.room.util.RoomConverter;
import com.prgrms.airbnb.domain.user.entity.User;
import com.prgrms.airbnb.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional(readOnly = true)
public class RoomServiceForHost {

  private final RoomRepository roomRepository;
  private final UserRepository userRepository;
  private final UploadService uploadService;

  public RoomServiceForHost(RoomRepository roomRepository,
      UserRepository userRepository,
      UploadService uploadService) {
    this.roomRepository = roomRepository;
    this.userRepository = userRepository;
    this.uploadService = uploadService;
  }

  @Transactional
  public RoomDetailResponse save(
      CreateRoomRequest createRoomRequest,
      List<MultipartFile> multipartFiles,
      Long hostId) {

    if (roomRepository.existsByAddress(createRoomRequest.getAddress())) {
      throw new RuntimeException("기존에 등록된 주소가 있습니다.");
    }

    List<RoomImage> roomImages = new ArrayList<>();
    if (multipartFiles != null && multipartFiles.size() > 0) {
      roomImages = multipartFiles.stream().map(
          m -> new RoomImage(uploadService.uploadImg(m))).collect(Collectors.toList());
    }

    User user = userRepository.findById(hostId)
        .orElseThrow(RuntimeException::new);
    Room room = RoomConverter.toRoom(createRoomRequest, roomImages, user);
    Room savedRoom = roomRepository.save(room);

    return RoomConverter.ofDetail(savedRoom);
  }

  @Transactional
  public RoomDetailResponse modify(
      UpdateRoomRequest updateRoomRequest,
      List<MultipartFile> multipartFiles,
      Long hostId) {

    Room room = roomRepository.findById(updateRoomRequest.getId())
        .orElseThrow(IllegalArgumentException::new);
    validateHost(room, hostId);

    room.setName(updateRoomRequest.getName());
    room.setCharge(updateRoomRequest.getCharge());
    room.setDescription(updateRoomRequest.getDescription());

    if (multipartFiles != null) {
      List<RoomImage> newRoomImages = multipartFiles.stream().map(
          m -> new RoomImage(uploadService.uploadImg(m))).collect(Collectors.toList());

      room.getRoomImages()
          .removeIf(roomImage -> !newRoomImages.contains(roomImage));
      newRoomImages.forEach(room::setImage);
    }

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

  public Slice<RoomSummaryResponse> findByHostId(
      Long hostId,
      SortTypeForHost sortType,
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
