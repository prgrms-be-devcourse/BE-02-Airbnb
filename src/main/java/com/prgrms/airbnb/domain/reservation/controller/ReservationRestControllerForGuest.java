package com.prgrms.airbnb.domain.reservation.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.reservation.dto.CreateReservationRequest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForGuest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationSummaryResponse;
import com.prgrms.airbnb.domain.reservation.service.ReservationServiceForGuest;
import com.prgrms.airbnb.domain.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/guest/reservations")
@RestController
@Api(tags = {"guest에 관한 예약 정보를 제공하는 Controller"})
public class ReservationRestControllerForGuest {

  private final ReservationServiceForGuest reservationServiceForGuest;
  private final UserService userService;

  public ReservationRestControllerForGuest(
      ReservationServiceForGuest reservationServiceForGuest,
      UserService userService) {
    this.reservationServiceForGuest = reservationServiceForGuest;
    this.userService = userService;
  }

  @PostMapping("")
  @ApiOperation(value = "사용자가 방을 예약")
  public ResponseEntity<ReservationDetailResponseForGuest> register(
      @ApiParam(value = "token", required = true)
      @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "생성할 예약 정보")
      @RequestBody CreateReservationRequest createReservationRequest) {
    Long userId = authentication.userId;
    userService.findById(userId)
        .orElseThrow(() -> {
          throw new NotFoundException(this.getClass().getName());
        });
    ReservationDetailResponseForGuest save = reservationServiceForGuest.save(
        createReservationRequest);
    return ResponseEntity.created(URI.create("/api/v1/guest/reservations/" + save.getId())).body(save);
  }

  @GetMapping("")
  @ApiOperation(value = "게스트가 예약한 모든 방 정보 조회")
  public ResponseEntity<Slice<ReservationSummaryResponse>> getReservationList(
      @ApiParam(value = "token", required = true)
      @AuthenticationPrincipal JwtAuthentication authentication,
      Pageable pageable) {
    Long userId = authentication.userId;
    Slice<ReservationSummaryResponse> reservationList = reservationServiceForGuest.findByUserId(userId,
        pageable);

    return ResponseEntity.ok(reservationList);
  }

  @GetMapping("/{reservationId}")
  @ApiOperation(value = "게스트가 예약한 특정 방 정보 조회")
  public ResponseEntity<ReservationDetailResponseForGuest> getDetail(
      @ApiParam(value = "token", required = true)
      @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "조회할 예약번호(id)", required = true)
      @PathVariable String reservationId) {
    Long userId = authentication.userId;
    ReservationDetailResponseForGuest reservationDetail = reservationServiceForGuest.findDetailById(
        reservationId, userId);
    return ResponseEntity.ok(reservationDetail);
  }

  @DeleteMapping("/{reservationId}")
  @ApiOperation(value = "게스트의 특정 예약 취소")
  public ResponseEntity<Object> cancel(
      @ApiParam(value = "token", required = true)
      @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "삭제할 예약번호(id)", required = true)
      @PathVariable String reservationId) {
    Long userId = authentication.userId;
    reservationServiceForGuest.cancel(userId, reservationId);
    return ResponseEntity.noContent().build();
  }

}
