package com.prgrms.airbnb.common.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Money {
    private int value;

    public Money(int value) {
        validationMoney(value);
        this.value = value;
    }

    private void validationMoney(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public Money multiply(int multiplier) {
        return new Money(value * multiplier);
    }

    public int getValue() {
        return value;
    }
}
