package com.prgrms.airbnb.domain.review.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.review.dto.ReviewResponse;
import com.prgrms.airbnb.domain.review.service.HostReviewService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/host/reviews")
@RestController
public class ReviewRestControllerForHost {

  private final HostReviewService hostReviewService;

  public ReviewRestControllerForHost(HostReviewService hostReviewService) {
    this.hostReviewService = hostReviewService;
  }

  @GetMapping("{roomId}")
  public ResponseEntity<Slice<ReviewResponse>> getByRoomId(
      @AuthenticationPrincipal JwtAuthentication authentication, @PathVariable Long roomId,
      Pageable pageable) {
    Long hostId = authentication.userId;
    Slice<ReviewResponse> reviewResponses = hostReviewService.findAllByRoomId(hostId, roomId,
        pageable);
    return ResponseEntity.ok(reviewResponses);
  }
}
