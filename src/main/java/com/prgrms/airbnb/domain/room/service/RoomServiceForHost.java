package com.prgrms.airbnb.domain.room.service;

import com.prgrms.airbnb.domain.common.exception.BadRequestException;
import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.common.exception.UnAuthorizedAccessException;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
      throw new BadRequestException("[" + this.getClass().getName() + "] 기존에 등록된 주소가 있습니다.");
    }

    User user = userRepository.findById(hostId)
        .orElseThrow(() -> new NotFoundException("[" + this.getClass().getName() + "] 해당 id의 user를 찾을 수 없습니다."));
    Room room = RoomConverter.toRoom(createRoomRequest, user);
    Room savedRoom = roomRepository.save(room);

    List<RoomImage> roomImages = uploadImages(multipartFiles);
    room.enrollRoomImages(roomImages);

    return RoomConverter.ofDetail(savedRoom);
  }

  @Transactional
  public RoomDetailResponse modify(
      UpdateRoomRequest updateRoomRequest,
      List<MultipartFile> multipartFiles,
      Long hostId) {

    Room room = roomRepository.findById(updateRoomRequest.getId())
        .orElseThrow(() -> new NotFoundException("[" + this.getClass().getName() + "] 해당 id의 room을 찾을 수 없습니다."));
    validateHost(room, hostId);

    room.changeName(updateRoomRequest.getName());
    room.changeCharge(updateRoomRequest.getCharge());
    room.changeDescription(updateRoomRequest.getDescription());
    room.getRoomInfo().changeMaxGuest(updateRoomRequest.getMaxGuest());
    room.getRoomInfo().changeBedCount(updateRoomRequest.getBedCount());

    List<RoomImage> deleteRoomImageList =
        getDeleteRoomImageList(updateRoomRequest, room);
    deleteRoomImageList.forEach(roomImage -> uploadService.delete(roomImage.getPath()));
    room.deleteRoomImages(deleteRoomImageList);

    List<RoomImage> updateRoomImageList = uploadImages(multipartFiles);
    room.enrollRoomImages(updateRoomImageList);

    return RoomConverter.ofDetail(room);
  }

  public RoomDetailResponse findDetailById(Long id, Long hostId) {

    Room room = roomRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("[" + this.getClass().getName() + "] 해당 id를 가진 room이 존재하지 않습니다."));
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

  @Transactional
  public void remove(Long roomId, Long hostId) {
    Room room = roomRepository.findById(roomId)
        .orElseThrow(() -> new NotFoundException("[" + this.getClass().getName() + "] 해당 id를 가진 room이 존재하지 않습니다."));
    validateHost(room, hostId);
    roomRepository.deleteById(roomId);
  }

  private void validateHost(Room room, Long hostId) {
    if (!room.getUserId().equals(hostId)) {
      throw new UnAuthorizedAccessException("[" + this.getClass().getName() + "] 해당 room에 접근할 권한이 없는 사용자입니다.");
    }
  }

  private List<RoomImage> uploadImages(List<MultipartFile> multipartFiles) {
    return Optional.ofNullable(multipartFiles).orElseGet(Collections::emptyList)
        .stream().map(m -> new RoomImage(uploadService.uploadImg(m))).collect(Collectors.toList());
  }

  private List<RoomImage> getDeleteRoomImageList(UpdateRoomRequest updateRoomRequest, Room room) {
    return room.getRoomImages().stream()
        .filter(roomImage -> !updateRoomRequest.getRoomImages().contains(roomImage))
        .collect(Collectors.toList());
  }
}
