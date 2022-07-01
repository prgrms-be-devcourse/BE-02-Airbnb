package com.prgrms.airbnb.config.jwt;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

// 인증 완료후 사용자를 표현하기 위한 객체 -> principal에 들어갈 객체
public class JwtAuthentication {

  public final String token;
  public final String username;
  public final String userEmail;
  public final Long userId;

  public JwtAuthentication(String token, String username, String userEmail, Long userId) {
    if (StringUtils.isEmpty(token)) {
      throw new IllegalArgumentException();
    }

    if (StringUtils.isEmpty(username)) {
      throw new IllegalArgumentException();
    }

    if (StringUtils.isEmpty(userEmail)) {
      throw new IllegalArgumentException();
    }

    if (ObjectUtils.isEmpty(userId)) {
      throw new IllegalArgumentException();
    }

    this.token = token;
    this.username = username;
    this.userEmail = userEmail;
    this.userId = userId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("token", token)
        .append("username", username)
        .append("userEmail", userEmail)
        .append("userId", userId)
        .toString();
  }

}