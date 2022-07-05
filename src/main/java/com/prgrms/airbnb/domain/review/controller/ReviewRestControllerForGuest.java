package com.prgrms.airbnb.domain.review.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.review.dto.CreateReviewRequest;
import com.prgrms.airbnb.domain.review.dto.ReviewResponse;
import com.prgrms.airbnb.domain.review.dto.UpdateReviewRequest;
import com.prgrms.airbnb.domain.review.service.GuestReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/guest/reviews")
@RestController
public class ReviewRestControllerForGuest {

  private final GuestReviewService guestReviewService;

  public ReviewRestControllerForGuest(GuestReviewService guestReviewService) {
    this.guestReviewService = guestReviewService;
  }

  @PostMapping("/{reservationId}")
  @ApiOperation(value = "예약 아이디로 리뷰 등록")
  public ResponseEntity<ReviewResponse> register(
      @ApiParam(value = "token", required = true) @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "예약 아이디", required = true) @PathVariable String reservationId,
      @ApiParam(value = "리뷰 생성 DTO", required = true) @RequestPart(value = "review") CreateReviewRequest createReviewRequest,
      @ApiParam(value = "리뷰 생성 사진", required = false) @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
    Long userId = authentication.userId;
    ReviewResponse reviewResponse = guestReviewService.save(userId, reservationId,
        createReviewRequest, multipartFiles);
    return ResponseEntity.created(URI.create("/api/v1/guest/reviews/" + reviewResponse.getId()))
        .body(reviewResponse);
  }

  @PutMapping("/{reviewId}")
  @ApiOperation(value = "리뷰 아이디로 리뷰 수정")
  public ResponseEntity<ReviewResponse> modify(
      @ApiParam(value = "token", required = true) @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "리뷰 아이디", required = true) @PathVariable Long reviewId,
      @ApiParam(value = "리뷰 수정 DTO", required = true) @RequestPart(value = "review") UpdateReviewRequest updateReviewRequest,
      @ApiParam(value = "리뷰 수정 추가 사진", required = false) @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
    Long userId = authentication.userId;
    ReviewResponse reviewResponse = guestReviewService.modify(userId, reviewId, updateReviewRequest,
        multipartFiles);
    return ResponseEntity.ok(reviewResponse);
  }

  @GetMapping("/{roomId}")
  @ApiOperation(value = "룸 아이디로 리뷰 리스트 조회")
  public ResponseEntity<Slice<ReviewResponse>> getByRoomId(
      @ApiParam(value = "token", required = true) @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "룸 아이디", required = true) @PathVariable Long roomId, Pageable pageable) {
    Long userId = authentication.userId;
    Slice<ReviewResponse> reviewResponses = guestReviewService.findAllByRoomId(userId, roomId,
        pageable);
    return ResponseEntity.ok(reviewResponses);
  }

  @DeleteMapping("/{reviewId}")
  @ApiOperation(value = "리뷰 아이디로 리뷰 삭제")
  public ResponseEntity<Void> delete(
      @ApiParam(value = "token", required = true) @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "리뷰 아이디", required = true) @PathVariable Long reviewId) {
    Long userId = authentication.userId;
    guestReviewService.remove(userId, reviewId);
    return ResponseEntity.noContent().build();
  }
}
