package com.prgrms.airbnb.domain.room.controller;

import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.dto.SearchRoomRequest;
import com.prgrms.airbnb.domain.room.service.RoomServiceForGuest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/guest/room")
@RestController
public class RoomRestControllerForGuest {

  private final RoomServiceForGuest roomService;

  public RoomRestControllerForGuest(RoomServiceForGuest roomService) {
    this.roomService = roomService;
  }

  @GetMapping
  public ResponseEntity<Slice<RoomSummaryResponse>> getAllRoom(
      SearchRoomRequest searchRoomRequest,
      Pageable pageable) {

    Slice<RoomSummaryResponse> searchRoomSummarySlice
        = roomService.findAll(searchRoomRequest, pageable);

    return ResponseEntity.ok(searchRoomSummarySlice);
  }

  @GetMapping("/{roomId}")
  public ResponseEntity<RoomDetailResponse> getRoomDetail(@PathVariable Long roomId) {
    RoomDetailResponse roomDetailInfo = roomService.findDetailById(roomId);

    return ResponseEntity.ok(roomDetailInfo);
  }
}
