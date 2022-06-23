package com.prgrms.airbnb.domain.user.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UserDto {

  private final String token;

  private final String username;

  private final String group;

  public UserDto(String token, String username, String group) {
    this.token = token;
    this.username = username;
    this.group = group;
  }

  public String getToken() {
    return token;
  }

  public String getUsername() {
    return username;
  }

  public String getGroup() {
    return group;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("token", token)
        .append("username", username)
        .append("group", group)
        .toString();
  }

}