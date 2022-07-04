package com.prgrms.airbnb.domain.reservation.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForHost;
import com.prgrms.airbnb.domain.reservation.dto.ReservationSummaryResponse;
import com.prgrms.airbnb.domain.reservation.entity.ReservationStatus;
import com.prgrms.airbnb.domain.reservation.service.ReservationServiceForHost;
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
public class ReservationRestControllerForHost {

  private final ReservationServiceForHost reservationServiceForHost;

  public ReservationRestControllerForHost(
      ReservationServiceForHost reservationServiceForHost) {
    this.reservationServiceForHost = reservationServiceForHost;
  }

  @PutMapping("/{reservationId}")
  public ResponseEntity<ReservationDetailResponseForHost> approve(
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
  public ResponseEntity<Slice<ReservationSummaryResponse>> getReservationList(
      @AuthenticationPrincipal JwtAuthentication authentication,
      Pageable pageable) {
    Long hostId = authentication.userId;
    Slice<ReservationSummaryResponse> reservationList = reservationServiceForHost.findByHostId(hostId,
        pageable);

    return ResponseEntity.ok(reservationList);
  }

  @GetMapping("/{reservationId}")
  public ResponseEntity<ReservationDetailResponseForHost> getDetail(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @PathVariable String reservationId) {
    Long hostId = authentication.userId;
    ReservationDetailResponseForHost reservationDetail = reservationServiceForHost.findDetailById(
        reservationId, hostId);
    return ResponseEntity.ok(reservationDetail);
  }

}
