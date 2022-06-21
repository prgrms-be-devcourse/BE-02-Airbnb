package com.prgrms.airbnb.room.controller;

import com.prgrms.airbnb.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.room.dto.SortTypeForHost;
import com.prgrms.airbnb.room.dto.UpdateRoomRequest;
import com.prgrms.airbnb.room.service.RoomService;
import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/room/v1")
@RestController
public class RoomRestControllerForHost {

  private final RoomService roomService;

  public RoomRestControllerForHost(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping("create")
  public ResponseEntity<RoomDetailResponse> enrollRoom(CreateRoomRequest createRoomRequest, HttpServletRequest request) {
    Long userId = (Long) request.getSession().getAttribute("userId");
    createRoomRequest.setUserId(userId);
    RoomDetailResponse savedRoom = roomService.save(createRoomRequest);
    return ResponseEntity.created(URI.create("/api/room/v1/" + savedRoom.getId()))
        .body(savedRoom);
  }

  @PutMapping
  // TODO: 2022/06/21 updateDto <= hostId 필요? servletRequest에 담겨져있고 아직 검증 로직이 없어서 만들어야하나
  public ResponseEntity<RoomDetailResponse> modifyRoom(UpdateRoomRequest updateRoomRequest,
      HttpServletRequest request) {
    Long userId = (Long) request.getSession().getAttribute("userId");
    updateRoomRequest.setUserId(userId);
    RoomDetailResponse modifiedRoom = roomService.modify(updateRoomRequest);

    return ResponseEntity.ok(modifiedRoom);
  }

  @GetMapping
  public ResponseEntity<Page<RoomSummaryResponse>> findByHostId(HttpServletRequest request,
      @RequestParam("sortType") SortTypeForHost sortType, Pageable pageable) {
    Long hostId = (Long) request.getSession().getAttribute("userId");
    Page<RoomSummaryResponse> pagesHostId = roomService.findByHostId(hostId, sortType, pageable);

    return ResponseEntity.ok(pagesHostId);
  }

}
