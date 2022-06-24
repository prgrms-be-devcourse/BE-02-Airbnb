package com.prgrms.airbnb.domain.user.util;

import com.prgrms.airbnb.domain.user.dto.UserDetailResponse;
import com.prgrms.airbnb.domain.user.entity.User;
import java.util.Objects;

public class UserConverter {

  public static UserDetailResponse from(User user) {
    return UserDetailResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .email(Objects.isNull(user.getEmail()) ? null : user.getEmail().getEmail())
        .phone(Objects.isNull(user.getPhone()) ? null : user.getPhone().getNumber())
        .group(user.getGroup())
        .build();
  }

}
