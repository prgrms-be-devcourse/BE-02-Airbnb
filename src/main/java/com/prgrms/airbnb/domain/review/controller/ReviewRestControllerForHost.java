package com.prgrms.airbnb.domain.review.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.review.dto.ReviewResponse;
import com.prgrms.airbnb.domain.review.service.HostReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
  @ApiOperation(value = "룸 아이디로 리뷰 리스트 조회")
  public ResponseEntity<Slice<ReviewResponse>> getByRoomId(
      @ApiParam(value = "token", required = true) @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "룸 아이디", required = true) @PathVariable Long roomId, Pageable pageable) {
    Long userId = authentication.userId;
    Slice<ReviewResponse> reviewResponses = hostReviewService.findAllByRoomId(userId, roomId,
        pageable);
    return ResponseEntity.ok(reviewResponses);
  }
}
