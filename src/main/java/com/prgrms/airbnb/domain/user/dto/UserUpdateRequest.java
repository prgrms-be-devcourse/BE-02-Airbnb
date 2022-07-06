package com.prgrms.airbnb.domain.user.dto;


import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateRequest {
  @ApiModelProperty(value = "변경하고자 하는 회원 이름", example = "abcd")
  private String name;

  @Email
  @ApiModelProperty(value = "변경하고자 하는 회원 이메일", example = "gef@naver.com")
  private String email;

  @ApiModelProperty(value = "변경하고자 하는 회원 전화번호", example = "010-1234-5678")
  private String phone;
}
