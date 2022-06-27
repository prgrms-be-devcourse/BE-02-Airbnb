package com.prgrms.airbnb.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateRequest {
  private Long id;
  private String name;
  private String profileImage;
  private String email;
  private String phone;
}
