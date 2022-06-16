package com.prgrms.airbnb.common.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Phone {
    private final String regx = "^\\d{3}-\\d{3,4}-\\d{4}$";
    private String number;

    public Phone(String number) {
        validationPhone(number);
        this.number = number;
    }

    public static Phone of(String number) {
        return new Phone(number);
    }

    public String getNumber() {
        return number;
    }

    public void validationPhone(String number) {
        if (StringUtils.isBlank(number) || !number.matches(regx)){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(number, phone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
