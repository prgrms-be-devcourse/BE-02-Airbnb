package com.prgrms.airbnb.common.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PhoneTest {

    @Test
    @DisplayName("Phone 생성 성공")
    void phone_created() {
        String phoneNumber1 = "010-1234-1234";
        String phoneNumber2 = "019-123-1234";
        Phone phone1 = new Phone(phoneNumber1);
        Phone phone2 = new Phone(phoneNumber2);
        Assertions.assertThat(phone1.getNumber()).isEqualTo(phoneNumber1);
        Assertions.assertThat(phone2.getNumber()).isEqualTo(phoneNumber2);
    }

    @Test
    @DisplayName("Phone 생성 실패")
    void wrong_phone_created() {
        String phoneNumber = "010-12345-12345";
        Assertions.assertThatThrownBy(() -> new Phone(phoneNumber))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 전화번호 저장시 Phone 생성 실패")
    void null_wrong_phone_created() {
        String phoneNumber = " ";
        Assertions.assertThatThrownBy(() -> new Phone(phoneNumber))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("정적 메소드 of 사용해 Phone을 생성")
    void phone_created_with_of() {
        String phoneNumber = "010-1234-1234";
        Phone phone = Phone.of(phoneNumber);
        Assertions.assertThat(phone.getNumber()).isEqualTo(phoneNumber);
    }

    @Test
    @DisplayName("getPhone() 사용하여 값을 가져올 수 있음")
    void getPhone() {
        String phoneNumber1 = "010-1234-1234";
        Phone phone1 = new Phone(phoneNumber1);
        Assertions.assertThat(phone1.getNumber()).isEqualTo(phoneNumber1);
    }
}