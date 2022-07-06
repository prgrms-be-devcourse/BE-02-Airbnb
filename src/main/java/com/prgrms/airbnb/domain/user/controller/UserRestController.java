package com.prgrms.airbnb.domain.user.controller;

import com.prgrms.airbnb.config.jwt.JwtAuthentication;
import com.prgrms.airbnb.domain.common.exception.UnAuthorizedAccessException;
import com.prgrms.airbnb.domain.user.dto.UserDetailResponse;
import com.prgrms.airbnb.domain.user.dto.UserUpdateRequest;
import com.prgrms.airbnb.domain.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@Api(tags = {"회원 정보를 제공하는 Controller"})
public class UserRestController {

  private final UserService userService;

  public UserRestController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @ApiOperation(value = "접속한 회원의 정보를 조회")
  @ApiResponse(code = 200, message = "존재하는 회원 정보를 요청하는 경우")
  public ResponseEntity<UserDetailResponse> getInfo(
      @AuthenticationPrincipal JwtAuthentication authentication) {
    UserDetailResponse response = userService.findById(authentication.userId).orElseThrow(() -> {
      throw new UnAuthorizedAccessException(this.getClass().getName());
    });
    return ResponseEntity.ok(response);
  }

  @PutMapping
  @ApiOperation(value = "접속한 사용자의 정보를 수정")
  @ApiResponse(code = 200, message = "존재하는 회원 정보 수정을 요청하는 경우")
  public ResponseEntity<UserDetailResponse> modifyInfo(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @ApiParam(value = "변경하고자 하는 텍스트 요청", required = true) @RequestPart(name = "request") UserUpdateRequest request,
      @ApiParam(value = "변경하고자 하는 이미지") @RequestPart(name = "image", required = false) MultipartFile multipartFile) {
    UserDetailResponse response = userService.modify(authentication.userId, request, multipartFile);
    return ResponseEntity.ok(response);
  }

  @PatchMapping
  @ApiOperation(value = "접속한 사용자의 권한을 호스트로 변경합니다.")
  @ApiResponse(code = 200, message = "존재하는 회원의 권한을 변경하는 경우")
  public ResponseEntity<UserDetailResponse> changePermissionToHost(
      @AuthenticationPrincipal JwtAuthentication authentication) {
    UserDetailResponse response = userService.changeUserToHost(authentication.userId);
    return ResponseEntity.ok(response);
  }
}