package com.prgrms.airbnb.domain.user.util;

import com.prgrms.airbnb.domain.user.dto.UserDetailResponse;
import com.prgrms.airbnb.domain.user.entity.User;

public class UserConverter {

  public static UserDetailResponse from(User user) {
    return UserDetailResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail().getEmail())
        .phone(user.getPhone().getNumber())
        .group(user.getGroup())
        .build();
  }

}
