package com.prgrms.airbnb.domain.user.dto;


import javax.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateRequest {
  private String name;
  @Email
  private String email;
  private String phone;
}
