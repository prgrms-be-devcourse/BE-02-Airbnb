package com.prgrms.airbnb.common.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {
    private final String regx = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private String email;

    public Email(String email) {
        validationEmail(email);
        this.email = email;
    }

    private void validationEmail(String email) {
        if (StringUtils.isBlank(email) || !email.matches(regx)) {
            throw new IllegalArgumentException();
        }
    }

    public static Email of(String email) {
        return new Email(email);
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
