package com.prgrms.airbnb.domain.room.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.room.dto.*;
import com.prgrms.airbnb.domain.room.service.RoomService;
import com.prgrms.airbnb.domain.user.entity.User;
import com.prgrms.airbnb.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/api/v1/room")
@RestController
@RequiredArgsConstructor
public class RoomRestControllerForHost {
    private final RoomService roomService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<RoomDetailResponse> registerRoom(
        @AuthenticationPrincipal JwtAuthentication authentication,
        @RequestBody CreateRoomRequest createRoomRequest) {
        User user = userService.findByUsername(authentication.username)
            .orElseThrow(() -> new IllegalArgumentException("Could not found user for " + authentication.username));
        RoomDetailResponse response = roomService.save(createRoomRequest, user);
        return ResponseEntity.created(URI.create("/api/v1/room/" + response.getId()))
            .body(response);
    }

//    @PutMapping
//    // TODO: 2022/06/21 updateDto <= hostId 필요? servletRequest에 담겨져있고 아직 검증 로직이 없어서 만들어야하나
//    public ResponseEntity<RoomDetailResponse> modifyRoom(UpdateRoomRequest updateRoomRequest,
//                                                         HttpServletRequest request) {
//        Long userId = (Long) request.getSession().getAttribute("userId");
//        updateRoomRequest.setUserId(userId);
//        RoomDetailResponse modifiedRoom = roomService.modify(updateRoomRequest);
//
//        return ResponseEntity.ok(modifiedRoom);
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<RoomSummaryResponse>> findByHostId(HttpServletRequest request,
//                                                                  @RequestParam("sortType") SortTypeForHost sortType,
//                                                                  Pageable pageable) {
//        Long hostId = (Long) request.getSession().getAttribute("userId");
//        Page<RoomSummaryResponse> pagesHostId = roomService.findByHostId(hostId, sortType, pageable);
//
//        return ResponseEntity.ok(pagesHostId);
//    }

}
