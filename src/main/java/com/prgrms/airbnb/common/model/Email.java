package com.prgrms.airbnb.common.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {
    private String email;

    public Email(String email) {
        this.email = email;
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
