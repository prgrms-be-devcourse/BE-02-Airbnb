package com.prgrms.airbnb.domain.common.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

  private final String EMAIL_REGX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
  private String email;

  public Email(String email) {
    validationEmail(email);
    this.email = email;
  }

  private void validationEmail(String email) {
    if (StringUtils.isBlank(email) || !email.matches(EMAIL_REGX)) {
      throw new IllegalArgumentException();
    }
  }

  public static Email of(String email) {
    return new Email(email);
  }

  public String getEmail() {
    return email;
  }

}
