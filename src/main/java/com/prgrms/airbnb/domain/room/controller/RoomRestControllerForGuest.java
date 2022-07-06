package com.prgrms.airbnb.domain.room.controller;

import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.dto.SearchRoomRequest;
import com.prgrms.airbnb.domain.room.service.RoomServiceForGuest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
  @ApiOperation(value = "Room 검색", notes = "조건에 맞는 Room 목록들을 조회할 수 있다.")
  public ResponseEntity<Slice<RoomSummaryResponse>> getAllRoom(
      SearchRoomRequest searchRoomRequest,
      Pageable pageable) {

    Slice<RoomSummaryResponse> searchRoomSummarySlice
        = roomService.findAll(searchRoomRequest, pageable);

    return ResponseEntity.ok(searchRoomSummarySlice);
  }

  @GetMapping("/{roomId}")
  @ApiImplicitParam(name = "roomId", value = "Room 아이디")
  @ApiOperation(value = "Room 상세정보 조회", notes = "Room 1개의 상세정보를 조회할 수 있다.")
  public ResponseEntity<RoomDetailResponse> getRoomDetail(@PathVariable Long roomId) {
    RoomDetailResponse roomDetailInfo = roomService.findDetailById(roomId);

    return ResponseEntity.ok(roomDetailInfo);
  }
}
