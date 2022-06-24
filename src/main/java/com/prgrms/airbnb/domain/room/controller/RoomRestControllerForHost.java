package com.prgrms.airbnb.domain.room.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.room.dto.*;
import com.prgrms.airbnb.domain.room.entity.SortTypeForHost;
import com.prgrms.airbnb.domain.room.service.RoomServiceForHost;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/host/room")
@RestController
public class RoomRestControllerForHost {

  private final RoomServiceForHost roomService;

  public RoomRestControllerForHost(RoomServiceForHost roomService) {
    this.roomService = roomService;
  }

  @PostMapping
  public ResponseEntity<RoomDetailResponse> registerRoom(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @RequestBody CreateRoomRequest createRoomRequest) {
    RoomDetailResponse response = roomService.save(createRoomRequest, authentication.userId);
    return ResponseEntity.created(URI.create("/api/v1/room/" + response.getId()))
        .body(response);
  }

  @PutMapping
  public ResponseEntity<RoomDetailResponse> modifyRoom(UpdateRoomRequest updateRoomRequest,
      @AuthenticationPrincipal JwtAuthentication authentication) {

    Long userId = authentication.userId;
    RoomDetailResponse modifiedRoom = roomService.modify(updateRoomRequest, userId);

    return ResponseEntity.ok(modifiedRoom);
  }

  @GetMapping
  public ResponseEntity<Slice<RoomSummaryResponse>> getByHostId(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @RequestParam("sortType") SortTypeForHost sortType,
      Pageable pageable) {

    Long hostId = authentication.userId;
    Slice<RoomSummaryResponse> pagesHostId = roomService.findByHostId(hostId, sortType, pageable);

    return ResponseEntity.ok(pagesHostId);
  }

  @GetMapping("/{roomId}")
  public ResponseEntity<RoomDetailResponse> getDetail(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @PathVariable("roomId") Long roomId
  ) {
    RoomDetailResponse roomDetailInfo = roomService.findDetailById(roomId, authentication.userId);

    return ResponseEntity.ok(roomDetailInfo);
  }

  @DeleteMapping("/{roomId}")
  public ResponseEntity<Object> delete(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @PathVariable("roomId") Long roomId
  ) {

    roomService.remove(roomId, authentication.userId);
    return ResponseEntity.noContent().build();
  }
}
