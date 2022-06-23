package com.prgrms.airbnb.domain.room.util;

import com.prgrms.airbnb.domain.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.user.entity.User;

public class RoomConverter {

  public static Room toRoom(CreateRoomRequest createRoomRequest, User user) {
    return new Room(
        createRoomRequest.getAddress(),
        createRoomRequest.getCharge(),
        createRoomRequest.getName(),
        createRoomRequest.getDescription(),
        createRoomRequest.getRoomInfo(),
        createRoomRequest.getRoomType(),
        null,
        user.getId()
    );
  }

  public static Room toRoom(CreateRoomRequest createRoomRequest) {
    return new Room(
        createRoomRequest.getAddress(),
        createRoomRequest.getCharge(),
        createRoomRequest.getName(),
        createRoomRequest.getDescription(),
        createRoomRequest.getRoomInfo(),
        createRoomRequest.getRoomType(),
        null,
        createRoomRequest.getUserId()
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
        .build();
  }

  public static RoomSummaryResponse ofSummary(Room room) {
    return RoomSummaryResponse.builder()
        .id(room.getId())
        .address(room.getAddress())
        .charge(room.getCharge())
        .name(room.getName())
        .roomType(room.getRoomType())
        .roomImage(room.getImages().get(0))
        .build();
  }
}
