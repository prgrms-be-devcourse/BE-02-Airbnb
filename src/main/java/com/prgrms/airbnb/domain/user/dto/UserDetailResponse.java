package com.prgrms.airbnb.domain.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class UserDetailResponse {

  @ApiModelProperty(value = "회원 식별 번호", example = "1")
  private Long id;

  @ApiModelProperty(value = "회원 이름", example = "abcd")
  private String name;

  @ApiModelProperty(value = "회원 프로필 사진", example = "https://image.com")
  private String profileImage;

  @ApiModelProperty(value = "회원 이메일", example = "abcd@gmail.com")
  private String email;

  @ApiModelProperty(value = "회원 전화번호", example = "010-1234-5678")
  private String phone;

  @ApiModelProperty(value = "회원 그룹", example = "USER_GROUP")
  private String group;
}
