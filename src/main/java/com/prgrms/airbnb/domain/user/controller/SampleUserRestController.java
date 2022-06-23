package com.prgrms.airbnb.domain.user.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.user.dto.UserDto;
import com.prgrms.airbnb.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class SampleUserRestController {

  private final UserService userService;

  /**
   * 보호받는 엔드포인트 - ROLE_USER 또는 ROLE_ADMIN 권한 필요함
   */
  @GetMapping(path = "/me")
  public UserDto me(@AuthenticationPrincipal JwtAuthentication authentication) {
    return userService.findById(authentication.userId)
        .map(user ->
            new UserDto(authentication.token, authentication.username, user.getGroup().getName())
        )
        .orElseThrow(() -> new IllegalArgumentException(
            "Could not found user for " + authentication.username));
  }
}