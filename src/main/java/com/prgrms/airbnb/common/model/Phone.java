package com.prgrms.airbnb.common.model;

import java.util.Objects;

public class Phone {
    private String number;

    protected Phone() {
    }

    public Phone(String phoneNumber) {
        this.number = number;
    }

    public static Phone of(String number) {
        return new Phone(number);
    }

    public String getNumber() {
        return number;
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
