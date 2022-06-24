package com.prgrms.airbnb.domain.room.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.prgrms.airbnb.domain.common.entity.Address;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RoomTest {

  @Nested
  class 생성 {

    @Test
    @DisplayName("성공: 모든 조건이 들어갈 때.")
    public void success1() throws Exception {
      //given
      Address address = new Address("address1", "address2");
      Integer charge = 10000;
      String roomName = "roomName";
      String roomDescription = "roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();
      RoomImage roomImage1 = new RoomImage("roomImage Path 1");
      images.add(roomImage1);
      RoomImage roomImage2 = new RoomImage("roomImage Path 2");
      images.add(roomImage2);
      RoomImage roomImage3 = new RoomImage("roomImage Path 3");
      images.add(roomImage3);
      RoomImage roomImage4 = new RoomImage("roomImage Path 4");
      images.add(roomImage4);
      Long hostId = 1000L;

      //when
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //then
      assertThat(room.getAddress()).isEqualTo(address);
      assertThat(room.getCharge()).isEqualTo(charge);
      assertThat(room.getName()).isEqualTo(roomName);
      assertThat(room.getDescription()).isEqualTo(roomDescription);
      assertThat(room.getRoomInfo()).isEqualTo(roomInfo);
      assertThat(room.getImages()).contains(roomImage1);
      assertThat(room.getImages()).contains(roomImage2);
      assertThat(room.getImages()).contains(roomImage3);
      assertThat(room.getImages()).contains(roomImage4);
      assertThat(room.getUserId()).isEqualTo(hostId);
    }

    @Test
    @DisplayName("성공: description = null")
    public void success2() throws Exception {
      //given
      Address address = new Address("address1", "address2");
      Integer charge = 10000;
      String roomName = "roomName";
      String roomDescription = null;
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();
      RoomImage roomImage1 = new RoomImage("roomImage Path 1");
      images.add(roomImage1);
      RoomImage roomImage2 = new RoomImage("roomImage Path 2");
      images.add(roomImage2);
      RoomImage roomImage3 = new RoomImage("roomImage Path 3");
      images.add(roomImage3);
      RoomImage roomImage4 = new RoomImage("roomImage Path 4");
      images.add(roomImage4);
      Long hostId = 1000L;

      //when
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //then
      assertThat(room.getAddress()).isEqualTo(address);
      assertThat(room.getCharge()).isEqualTo(charge);
      assertThat(room.getName()).isEqualTo(roomName);
      assertThat(room.getDescription()).isEqualTo(roomDescription);
      assertThat(room.getRoomInfo()).isEqualTo(roomInfo);
      assertThat(room.getImages()).contains(roomImage1);
      assertThat(room.getImages()).contains(roomImage2);
      assertThat(room.getImages()).contains(roomImage3);
      assertThat(room.getImages()).contains(roomImage4);
      assertThat(room.getUserId()).isEqualTo(hostId);
    }

    @Test
    @DisplayName("성공: images가 빈 List일 경우")
    public void success3() throws Exception {
      //given
      Address address = new Address("address1", "address2");
      Integer charge = 10000;
      String roomName = "roomName";
      String roomDescription = "roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();
      Long hostId = 1000L;

      //when
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //then
      assertThat(room.getAddress()).isEqualTo(address);
      assertThat(room.getCharge()).isEqualTo(charge);
      assertThat(room.getName()).isEqualTo(roomName);
      assertThat(room.getDescription()).isEqualTo(roomDescription);
      assertThat(room.getRoomInfo()).isEqualTo(roomInfo);
      assertThat(room.getUserId()).isEqualTo(hostId);
    }

    @Test
    @DisplayName("성공: charge = 0")
    public void success5() throws Exception {
      //given
      Address address = new Address("address1", "address2");
      Integer charge = 0;
      String roomName = "roomName";
      String roomDescription = "roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();
      RoomImage roomImage1 = new RoomImage("roomImage Path 1");
      images.add(roomImage1);
      RoomImage roomImage2 = new RoomImage("roomImage Path 2");
      images.add(roomImage2);
      RoomImage roomImage3 = new RoomImage("roomImage Path 3");
      images.add(roomImage3);
      RoomImage roomImage4 = new RoomImage("roomImage Path 4");
      images.add(roomImage4);
      Long hostId = 1000L;

      //when
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //then
      assertThat(room.getAddress()).isEqualTo(address);
      assertThat(room.getCharge()).isEqualTo(charge);
      assertThat(room.getName()).isEqualTo(roomName);
      assertThat(room.getDescription()).isEqualTo(roomDescription);
      assertThat(room.getRoomInfo()).isEqualTo(roomInfo);
      assertThat(room.getImages()).contains(roomImage1);
      assertThat(room.getImages()).contains(roomImage2);
      assertThat(room.getImages()).contains(roomImage3);
      assertThat(room.getImages()).contains(roomImage4);
      assertThat(room.getUserId()).isEqualTo(hostId);
    }

    @Test
    @DisplayName("실패: name = null -> 예외 발생")
    public void failNameIsNull() throws Exception {
      //given
      Address address = new Address("address1", "address2");
      Integer charge = 10000;
      String roomDescription = "roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();
      RoomImage roomImage1 = new RoomImage("roomImage Path 1");
      images.add(roomImage1);
      RoomImage roomImage2 = new RoomImage("roomImage Path 2");
      images.add(roomImage2);
      RoomImage roomImage3 = new RoomImage("roomImage Path 3");
      images.add(roomImage3);
      RoomImage roomImage4 = new RoomImage("roomImage Path 4");
      images.add(roomImage4);
      Long hostId = 1000L;

      //when
      String roomName = null;

      //then
      assertThrows(RuntimeException.class,
          () -> new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

    @Test
    @DisplayName("실패: name = empty -> 예외 발생")
    public void failNameIsEmpty() throws Exception {
      //given
      Address address = new Address("address1", "address2");
      Integer charge = 10000;
      String roomDescription = "roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();
      RoomImage roomImage1 = new RoomImage("roomImage Path 1");
      images.add(roomImage1);
      RoomImage roomImage2 = new RoomImage("roomImage Path 2");
      images.add(roomImage2);
      RoomImage roomImage3 = new RoomImage("roomImage Path 3");
      images.add(roomImage3);
      RoomImage roomImage4 = new RoomImage("roomImage Path 4");
      images.add(roomImage4);
      Long hostId = 1000L;

      //when
      String roomName = "";

      //then
      assertThrows(RuntimeException.class,
          () -> new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

    @Test
    @DisplayName("실패: name = blank -> 예외 발생")
    public void failNameIsBlank() throws Exception {
      //given
      Address address = new Address("address1", "address2");
      Integer charge = 10000;
      String roomDescription = "roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();
      RoomImage roomImage1 = new RoomImage("roomImage Path 1");
      images.add(roomImage1);
      RoomImage roomImage2 = new RoomImage("roomImage Path 2");
      images.add(roomImage2);
      RoomImage roomImage3 = new RoomImage("roomImage Path 3");
      images.add(roomImage3);
      RoomImage roomImage4 = new RoomImage("roomImage Path 4");
      images.add(roomImage4);
      Long hostId = 1000L;

      //when
      String roomName = " ";

      //then
      assertThrows(RuntimeException.class,
          () -> new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

    @Test
    @DisplayName("실패: charge = null -> 예외 발생")
    public void failChargeIsNull() throws Exception {
      //given
      Address address = new Address("address1", "address2");
      String roomName = "roomName";
      String roomDescription = "roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();
      RoomImage roomImage1 = new RoomImage("roomImage Path 1");
      images.add(roomImage1);
      RoomImage roomImage2 = new RoomImage("roomImage Path 2");
      images.add(roomImage2);
      RoomImage roomImage3 = new RoomImage("roomImage Path 3");
      images.add(roomImage3);
      RoomImage roomImage4 = new RoomImage("roomImage Path 4");
      images.add(roomImage4);
      Long hostId = 1000L;

      //when
      Integer charge = null;

      //then
      assertThrows(RuntimeException.class,
          () -> new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

    @Test
    @DisplayName("실패: charge < 0 -> 예외 발생")
    public void failChargeIsNegative() throws Exception {
      //given
      Address address = new Address("address1", "address2");
      String roomName = "roomName";
      String roomDescription = "roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();
      RoomImage roomImage1 = new RoomImage("roomImage Path 1");
      images.add(roomImage1);
      RoomImage roomImage2 = new RoomImage("roomImage Path 2");
      images.add(roomImage2);
      RoomImage roomImage3 = new RoomImage("roomImage Path 3");
      images.add(roomImage3);
      RoomImage roomImage4 = new RoomImage("roomImage Path 4");
      images.add(roomImage4);
      Long hostId = 1000L;

      //when
      Integer charge = -10;

      //then
      assertThrows(RuntimeException.class,
          () -> new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

    @Test
    @DisplayName("실패: address = null -> 예외 발생")
    public void failAddressIsNull() throws Exception {
      //given
      Integer charge = 10000;
      String roomName = "roomName";
      String roomDescription = "roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();
      RoomImage roomImage1 = new RoomImage("roomImage Path 1");
      images.add(roomImage1);
      RoomImage roomImage2 = new RoomImage("roomImage Path 2");
      images.add(roomImage2);
      RoomImage roomImage3 = new RoomImage("roomImage Path 3");
      images.add(roomImage3);
      RoomImage roomImage4 = new RoomImage("roomImage Path 4");
      images.add(roomImage4);
      Long hostId = 1000L;

      //when
      Address address = null;

      //then
      assertThrows(RuntimeException.class,
          () -> new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

    @Test
    @DisplayName("실패: images를 null로 입력 -> 양방향 연관관계를 stream.forEach때문에 반드시 빈 리스트라도 들어가야 함")
    public void failListIsNull() throws Exception {
      //given
      Address address = new Address("address1", "address2");
      Integer charge = 10000;
      String roomName = "roomName";
      String roomDescription = "roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      Long hostId = 1000L;

      //when
      List<RoomImage> images = null;

      //then
      assertThrows(NullPointerException.class,
          () -> new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

  }

}