package com.prgrms.airbnb.domain.reservation.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForHost;
import com.prgrms.airbnb.domain.reservation.dto.ReservationSummaryResponse;
import com.prgrms.airbnb.domain.reservation.entity.ReservationStatus;
import com.prgrms.airbnb.domain.reservation.service.ReservationServiceForHost;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/host/reservations")
@RestController
@Api(tags = {"host에 관한 예약 정보를 제공하는 Controller"})
public class ReservationRestControllerForHost {

  private final ReservationServiceForHost reservationServiceForHost;

  public ReservationRestControllerForHost(
      ReservationServiceForHost reservationServiceForHost) {
    this.reservationServiceForHost = reservationServiceForHost;
  }

  @PutMapping("/{reservationId}")
  @ApiOperation(value = "예약 상태 변경")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "reservationId", value = "예약 번호(id)", required = true),
      @ApiImplicitParam(name = "status", value = "설정하고자 하는 예약 상태", required = true),
  })
  public ResponseEntity<ReservationDetailResponseForHost> approve(
      @ApiParam(value = "token", required = true)
      @AuthenticationPrincipal JwtAuthentication authentication,
      @PathVariable String reservationId,
      @RequestParam String status) {
    Long hostId = authentication.userId;
    ReservationStatus reservationStatus = ReservationStatus.valueOf(status);
    ReservationDetailResponseForHost approvalReservation = reservationServiceForHost.approval(reservationId,
        hostId, reservationStatus);

    return ResponseEntity.ok(approvalReservation);
  }

  @GetMapping("")
  @ApiOperation(value = "호스트의 모든 예약 정보 조회")
  public ResponseEntity<Slice<ReservationSummaryResponse>> getReservationList(
      @ApiParam(value = "token", required = true)
      @AuthenticationPrincipal JwtAuthentication authentication,
      Pageable pageable) {
    Long hostId = authentication.userId;
    Slice<ReservationSummaryResponse> reservationList = reservationServiceForHost.findByHostId(hostId,
        pageable);

    return ResponseEntity.ok(reservationList);
  }

  @GetMapping("/{reservationId}")
  @ApiOperation(value = "호스트의 예약 상세정보 조회")
  public ResponseEntity<ReservationDetailResponseForHost> getDetail(
      @ApiParam(value = "token", required = true)
      @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "상세 조회할 예약번호(id)", required = true)
      @PathVariable String reservationId) {
    Long hostId = authentication.userId;
    ReservationDetailResponseForHost reservationDetail = reservationServiceForHost.findDetailById(
        reservationId, hostId);
    return ResponseEntity.ok(reservationDetail);
  }

}
