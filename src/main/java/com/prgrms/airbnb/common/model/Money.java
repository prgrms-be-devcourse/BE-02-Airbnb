package com.prgrms.airbnb.common.model;

import com.querydsl.core.annotations.QueryProjection;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Access(value = AccessType.FIELD)
@Embeddable
public class Money {
    private Integer value;

    public Money(Integer value) {
        validationMoney(value);
        this.value = value;
    }

    private void validationMoney(Integer value) {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public Money multiply(Integer multiplier) {
        return new Money(value * multiplier);
    }

    public Integer getValue() {
        return value;
    }
}
