package com.prgrms.airbnb.domain.room.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.dto.UpdateRoomRequest;
import com.prgrms.airbnb.domain.room.entity.SortTypeForHost;
import com.prgrms.airbnb.domain.room.service.RoomServiceForHost;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/v1/host/room")
@RestController
public class RoomRestControllerForHost {

  private final RoomServiceForHost roomService;

  public RoomRestControllerForHost(RoomServiceForHost roomService) {
    this.roomService = roomService;
  }

  @PostMapping
  @ApiOperation(value = "Room 1개 등록", notes = "호스트는 룸 1개를 사진들과 함께 등록할 수 있다.")
  public ResponseEntity<RoomDetailResponse> registerRoom(
      @ApiParam(value = "token", required = true) @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "room 생성 요청 DTO", required = true) @RequestPart(value = "room") CreateRoomRequest createRoomRequest,
      @ApiParam(value = "room 이미지 목록", required = true) @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {

    RoomDetailResponse response
        = roomService.save(createRoomRequest, multipartFiles, authentication.userId);

    return ResponseEntity.created(URI.create("/api/v1/room/" + response.getId()))
        .body(response);
  }

  @PutMapping
  @ApiOperation(value = "Room의 정보 수정", notes = "호스트는 본인의 룸 정보를 수정할 수 있다.")
  public ResponseEntity<RoomDetailResponse> modifyRoom(
      @ApiParam(value = "token", required = true) @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "room 수정 요청 DTO", required = true) @RequestPart(value = "room") UpdateRoomRequest updateRoomRequest,
      @ApiParam(value = "room 이미지 목록", required = true) @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {

    Long userId = authentication.userId;
    RoomDetailResponse modifiedRoom
        = roomService.modify(updateRoomRequest, multipartFiles, userId);

    return ResponseEntity.ok(modifiedRoom);
  }

  @GetMapping
  @ApiOperation(value = "Host의 전체 Room 목록 조회", notes = "호스트는 본인의 Room 목록을 조회할 수 있다.")
  public ResponseEntity<Slice<RoomSummaryResponse>> getByHostId(
      @ApiParam(value = "token", required = true) @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "정렬 기준", required = true) @RequestParam("sortType") SortTypeForHost sortType,
      Pageable pageable) {

    Long hostId = authentication.userId;
    Slice<RoomSummaryResponse> pagesHostId
        = roomService.findByHostId(hostId, sortType, pageable);

    return ResponseEntity.ok(pagesHostId);
  }

  @GetMapping("/{roomId}")
  @ApiOperation(value = "Room 1개 상세정보 조회", notes = "호스트는 본인의 특정 Room 1개의 상세 정보를 조회할 수 있다.")
  public ResponseEntity<RoomDetailResponse> getDetail(
      @ApiParam(value = "token", required = true) @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "room 아이디", required = true) @PathVariable("roomId") Long roomId) {

    RoomDetailResponse roomDetailInfo
        = roomService.findDetailById(roomId, authentication.userId);
    return ResponseEntity.ok(roomDetailInfo);
  }

  @DeleteMapping("/{roomId}")
  @ApiOperation(value = "Room 삭제", notes = "호스트는 본인의 Room을 삭제할 수 있다.")
  public ResponseEntity<Object> delete(
      @ApiParam(value = "token", required = true) @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "room 아이디", required = true) @PathVariable("roomId") Long roomId) {

    roomService.remove(roomId, authentication.userId);
    return ResponseEntity.noContent().build();
  }
}
