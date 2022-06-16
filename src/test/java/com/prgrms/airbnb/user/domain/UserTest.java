package com.prgrms.airbnb.user.domain;

import com.prgrms.airbnb.common.model.Email;
import com.prgrms.airbnb.common.model.Phone;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("팀에프")
                .email(new Email("dhkstnaos@gmail.com"))
                .phone(new Phone("010-1234-5678"))
                .build();
    }

    @Test
    void user_created() {
        Assertions.assertThat(user.getName()).isEqualTo("팀에프");
    }

    @Test
    void wrong_user_created() {
        Assertions.assertThatThrownBy(() ->
                User.builder()
                        .email(new Email("dhkstnaos@gmail.com"))
                        .phone(new Phone("010-1234-5678"))
                        .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }


}