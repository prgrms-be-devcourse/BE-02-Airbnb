package com.prgrms.airbnb.domain.reservation.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.reservation.dto.CreateReservationRequest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForGuest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationSummaryResponse;
import com.prgrms.airbnb.domain.reservation.service.ReservationServiceForGuest;
import com.prgrms.airbnb.domain.user.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/guest/reservations")
@RestController
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
  public ResponseEntity<ReservationDetailResponseForGuest> register(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @RequestParam CreateReservationRequest createReservationRequest) {
    Long userId = authentication.userId;
    userService.findById(userId)
        .orElseThrow(IllegalArgumentException::new);
    ReservationDetailResponseForGuest save = reservationServiceForGuest.save(
        createReservationRequest);
    return ResponseEntity.ok(save);
  }

  @PutMapping("/{reservationId}")
  public ResponseEntity<ReservationDetailResponseForGuest> cancel(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @PathVariable String reservationId) {
    Long userId = authentication.userId;
    reservationServiceForGuest.cancel(userId, reservationId);
    ReservationDetailResponseForGuest reservationDetail = reservationServiceForGuest.findDetailById(
        reservationId, userId);

    return ResponseEntity.ok(reservationDetail);
  }

  @GetMapping("/lists")
  public ResponseEntity<Slice<ReservationSummaryResponse>> getReservationList(
      @AuthenticationPrincipal JwtAuthentication authentication,
      Pageable pageable) {
    Long userId = authentication.userId;
    Slice<ReservationSummaryResponse> reservationList = reservationServiceForGuest.findByUserId(userId,
        pageable);

    return ResponseEntity.ok(reservationList);
  }

  @GetMapping("/{reservationId}")
  public ResponseEntity<ReservationDetailResponseForGuest> getDetail(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @PathVariable String reservationId) {
    Long userId = authentication.userId;
    ReservationDetailResponseForGuest reservationDetail = reservationServiceForGuest.findDetailById(
        reservationId, userId);

    return ResponseEntity.ok(reservationDetail);
  }

}
