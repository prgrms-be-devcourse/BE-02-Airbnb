package com.prgrms.airbnb.domain.user.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.user.dto.UserDetailResponse;
import com.prgrms.airbnb.domain.user.dto.UserUpdateRequest;
import com.prgrms.airbnb.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

  private final UserService userService;

  public UserRestController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<UserDetailResponse> getInfo(
      @AuthenticationPrincipal JwtAuthentication authentication) {
    UserDetailResponse response = userService.findById(authentication.userId).orElseThrow();
    return ResponseEntity.ok(response);
  }

  @PutMapping
  public ResponseEntity<UserDetailResponse> modifyInfo(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @RequestBody UserUpdateRequest request) {
    UserDetailResponse response = userService.modify(authentication.userId, request);
    return ResponseEntity.ok(response);
  }
}