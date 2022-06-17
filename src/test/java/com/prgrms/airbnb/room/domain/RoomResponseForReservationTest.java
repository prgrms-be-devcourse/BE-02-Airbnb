package com.prgrms.airbnb.room.domain;

import com.prgrms.airbnb.common.model.Address;
import com.prgrms.airbnb.common.model.Money;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class RoomTest {

    @Test
    void room_created() {
        Room room = Room.builder()
                .address(new Address("서울시", "에프동"))
                .charge(new Money(10))
                .description("범석빌라")
                .maxGuest(4)
                .images(new ArrayList<>())
                .userId(1L)
                .build();

        Assertions.assertThat(room.getDescription()).isEqualTo("범석빌라");
    }
}