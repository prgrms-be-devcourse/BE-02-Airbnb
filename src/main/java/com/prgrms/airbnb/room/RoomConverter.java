package com.prgrms.airbnb.room;

import com.prgrms.airbnb.room.domain.Room;
import com.prgrms.airbnb.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.room.dto.RoomSummaryResponse;

public class RoomConverter {

  public static Room toRoom(CreateRoomRequest createRoomRequest) {
    return new Room(
        createRoomRequest.getAddress(),
        createRoomRequest.getCharge(),
        createRoomRequest.getName(),
        createRoomRequest.getDescription(),
        createRoomRequest.getRoomInfo(),
        createRoomRequest.getRoomType(),
        createRoomRequest.getImages(),
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
