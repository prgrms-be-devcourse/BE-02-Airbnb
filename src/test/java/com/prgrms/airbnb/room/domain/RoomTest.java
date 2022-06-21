package com.prgrms.airbnb.room.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.prgrms.airbnb.common.model.Address;
import com.prgrms.airbnb.common.model.Money;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoomTest {

  @Test
  @DisplayName("")
  public void Test() throws Exception {

    Room room = new Room(new Address("ad1", "ad2"), new Money(10000), "name1113", "오지마셈",
        new RoomInfo(1, 1, 1, 1), RoomType.APARTMENT,
        new ArrayList<>(), 100L);

    System.out.println("room = " + room);

  }

}