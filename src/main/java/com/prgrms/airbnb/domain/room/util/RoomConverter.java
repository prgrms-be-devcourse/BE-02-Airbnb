package com.prgrms.airbnb.domain.room.util;

import com.prgrms.airbnb.domain.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.user.entity.User;
import java.util.List;

public class RoomConverter {

  public static Room toRoom(CreateRoomRequest createRoomRequest, List<RoomImage> roomImages, User user) {
    return new Room(
        createRoomRequest.getAddress(),
        createRoomRequest.getCharge(),
        createRoomRequest.getName(),
        createRoomRequest.getDescription(),
        createRoomRequest.getRoomInfo(),
        createRoomRequest.getRoomType(),
        roomImages,
        user.getId()
    );
  }

  public static RoomDetailResponse ofDetail(Room room) {
    return RoomDetailResponse.builder()
        .id(room.getId())
        .address(room.getAddress())
        .charge(room.getCharge())
        .name(room.getName())
        .description(room.getDescription())
        .roomInfo(room.getRoomInfo())
        .roomType(room.getRoomType())
        .userId(room.getUserId())
        .roomImages(room.getRoomImages())
        .build();
  }

  public static RoomSummaryResponse ofSummary(Room room) {
    RoomSummaryResponse roomSummaryResponse = RoomSummaryResponse.builder()
        .id(room.getId())
        .address(room.getAddress())
        .charge(room.getCharge())
        .name(room.getName())
        .roomType(room.getRoomType())
        .build();
    if (room.getRoomImages().size() > 0) {
      roomSummaryResponse.setRoomImage(room.getRoomImages().get(0));
    }
    return roomSummaryResponse;
  }
}
