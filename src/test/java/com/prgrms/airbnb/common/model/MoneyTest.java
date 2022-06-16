package com.prgrms.airbnb.common.model;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoneyTest {

    Money money;

    @BeforeEach
    void setUp() {
        money = new Money(200);
    }

    @Test
    @DisplayName("Money 객체를 생성한다.")
    void money_created() {
        int value = 100;
        Money testMoney = new Money(value);
        Assertions.assertThat(testMoney.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("0보다 작거나 같은 Money 객체를 생성한다.")
    void wrong_money_created() {
        int value = 0;
        Assertions.assertThatThrownBy(() -> new Money(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("getValue를 사용해 Money 객체를 가지고 온다.")
    void getMoney() {
        Assertions.assertThat(money.getValue()).isEqualTo(200);
    }
}