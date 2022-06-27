package com.prgrms.airbnb.domain.room.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.dto.UpdateRoomRequest;
import com.prgrms.airbnb.domain.room.entity.SortTypeForHost;
import com.prgrms.airbnb.domain.room.service.RoomServiceForHost;
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
    public ResponseEntity<RoomDetailResponse> registerRoom(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @RequestPart(value = "room") CreateRoomRequest createRoomRequest,
            @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
        RoomDetailResponse response = roomService.save(createRoomRequest, authentication.userId);
        return ResponseEntity.created(URI.create("/api/v1/room/" + response.getId()))
                .body(response);
    }

    @PutMapping
    public ResponseEntity<RoomDetailResponse> modifyRoom(
            UpdateRoomRequest updateRoomRequest,
            @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles,
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
