//package com.prgrms.airbnb.common.model;
//
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//class EmailTest {
//
//    Email email;
//    String address;
//
//    @BeforeEach
//    void setUp() {
//        address = "dhkstnaos@gmail.com";
//        email = new Email(address);
//    }
//
//    @Test
//    @DisplayName("Email을 생성한다.")
//    void email_created() {
//        String address = "dhkstnaos@gmail.com";
//        Email testEmail = new Email(address);
//        Assertions.assertThat(testEmail.getEmail()).isEqualTo(address);
//    }
//
//    @Test
//    @DisplayName("RFC5322에 어긋나는 Email을 저장한다.")
//    void wrong_email_created() {
//        String address = "dhkstnaos@gmail.##";
//        Assertions.assertThatThrownBy(() -> new Email(address))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    @DisplayName("빈 string Email을 저장한다.")
//    void null_email_created() {
//        String address = " ";
//        Assertions.assertThatThrownBy(() -> new Email(address))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    @DisplayName("정적 메소드 of를 사용해 Email을 생성한다.")
//    void email_created_with_of() {
//        String address = "dhkstnaos@gmail.com";
//        Email testEmail = Email.of(address);
//        Assertions.assertThat(testEmail.getEmail()).isEqualTo(address);
//    }
//
//    @Test
//    @DisplayName("getEmail을 사용해 Email 객체를 가지고 온다.")
//    void getEmail() {
//        Assertions.assertThat(email.getEmail()).isEqualTo(address);
//    }
//
//}