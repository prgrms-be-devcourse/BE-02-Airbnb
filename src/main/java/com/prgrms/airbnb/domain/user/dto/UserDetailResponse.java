package com.prgrms.airbnb.domain.user.dto;

import com.prgrms.airbnb.domain.user.entity.Group;
import lombok.Builder;

@Builder
public class UserDetailResponse {
  private Long id;
  private String name;
  private String profileImage;
  private String email;
  private String phone;
  private Group group;
}
