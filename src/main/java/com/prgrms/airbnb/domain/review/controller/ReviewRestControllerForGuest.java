package com.prgrms.airbnb.domain.review.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.review.dto.CreateReviewRequest;
import com.prgrms.airbnb.domain.review.dto.ReviewResponse;
import com.prgrms.airbnb.domain.review.dto.UpdateReviewRequest;
import com.prgrms.airbnb.domain.review.service.GuestReviewService;
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
  public ResponseEntity<ReviewResponse> register(
      @AuthenticationPrincipal JwtAuthentication authentication, @PathVariable String reservationId,
      @RequestPart(value = "review") CreateReviewRequest createReviewRequest,
      @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
    Long hostId = authentication.userId;
    ReviewResponse reviewResponse = guestReviewService.save(hostId, reservationId,
        createReviewRequest, multipartFiles);
    return ResponseEntity.ok(reviewResponse);
  }

  @PutMapping("/{reviewId}")
  public ResponseEntity<ReviewResponse> modify(
      @AuthenticationPrincipal JwtAuthentication authentication, @PathVariable Long reviewId,
      @RequestPart(value = "review") UpdateReviewRequest updateReviewRequest,
      @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) {
    Long hostId = authentication.userId;
    ReviewResponse reviewResponse = guestReviewService.modify(hostId, reviewId, updateReviewRequest,
        multipartFiles);
    return ResponseEntity.ok(reviewResponse);
  }

  @GetMapping("/{roomId}")
  public ResponseEntity<Slice<ReviewResponse>> getByRoomId(
      @AuthenticationPrincipal JwtAuthentication authentication, @PathVariable Long roomId,
      Pageable pageable) {
    Long hostId = authentication.userId;
    Slice<ReviewResponse> reviewResponses = guestReviewService.findAllByRoomId(hostId, roomId,
        pageable);
    return ResponseEntity.ok(reviewResponses);
  }

  @DeleteMapping("/{reviewId}")
  public ResponseEntity<Void> delete(@AuthenticationPrincipal JwtAuthentication authentication,
      @PathVariable Long reviewId) {
    Long hostId = authentication.userId;
    guestReviewService.remove(hostId, reviewId);
    return ResponseEntity.ok().build();
  }
}
