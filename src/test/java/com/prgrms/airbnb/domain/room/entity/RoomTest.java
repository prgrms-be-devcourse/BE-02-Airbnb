package com.prgrms.airbnb.domain.room.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.prgrms.airbnb.domain.common.entity.Address;
import java.util.ArrayList;
import java.util.List;
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
    @DisplayName("실패: hostId = null 예외 발생")
    public void failHostIdIsNull() throws Exception {
      //given
      Address address = new Address("address1", "address2");
      String roomName = "roomName";
      String roomDescription = "roomDescription";
      Integer charge = 10000;
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

      //when
      Long hostId = null;

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

  @Nested
  class 수정{

    @Test
    @DisplayName("성공: Room 이름을 변경할 수 있음")
    public void successChangeName() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      String newRoomName = "new roomName";
      room.setName(newRoomName);

      //then
      assertThat(room.getName()).isEqualTo(newRoomName);
    }

    @Test
    @DisplayName("실패: Room 이름을 null 로 변경할 수 없음")
    public void failChangeNameToNull() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      String newRoomName = null;

      //then
      assertThrows(RuntimeException.class, () -> room.setName(newRoomName));
    }

    @Test
    @DisplayName("실패: Room 이름을 empty 로 변경할 수 없음")
    public void failChangeNameToEmpty() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      String newRoomName = "";

      //then
      assertThrows(RuntimeException.class, () -> room.setName(newRoomName));
    }

    @Test
    @DisplayName("실패: Room 이름을 blank 로 변경할 수 없음")
    public void failChangeNameToBlank() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      String newRoomName = " ";

      //then
      assertThrows(RuntimeException.class, () -> room.setName(newRoomName));
    }

    @Test
    @DisplayName("성공: Room 가격을 양수 값으로 변경할 수 있음")
    public void successChangeChargeToPositive() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      Integer newCharge = 10;
      room.setCharge(newCharge);

      //then
      assertThat(room.getCharge()).isEqualTo(newCharge);
    }

    @Test
    @DisplayName("성공: Room 가격을 0으로 변경할 수 있음")
    public void successChangeChargeToZero() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      Integer newCharge = 0;
      room.setCharge(newCharge);

      //then
      assertThat(room.getCharge()).isEqualTo(newCharge);
    }

    @Test
    @DisplayName("실패: Room 가격을 음수로 변경할 수 없음")
    public void failChangeChargeToNegative() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      Integer newCharge = -1;

      //then
      assertThrows(RuntimeException.class, () -> room.setCharge(newCharge));
    }

    @Test
    @DisplayName("성공: 새로운 이미지를 추가할 수 있음")
    public void successAddNewImage() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      RoomImage newRoomImage = new RoomImage("new Room Image Path");
      room.setImage(newRoomImage);

      //then
      assertThat(room.getImages()).contains(newRoomImage);
    }

    @Test
    @DisplayName("실패: null인 image를 추가할 수 없음")
    public void failChangeImagesToNull() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      RoomImage newRoomImage = null;

      //then
      assertThrows(RuntimeException.class, () -> room.setImage(newRoomImage));
    }

    @Test
    @DisplayName("성공: 기존 이미지를 삭제할 수 있음")
    public void successDeleteImage() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      room.deleteImage(roomImage4);

      //then
      assertThat(room.getImages()).doesNotContain(roomImage4);
    }

    @Test
    @DisplayName("실패: 가지고 있지 않은 image를 삭제할 수 없음")
    public void failImageDeleteImageNotContained() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      RoomImage roomImage5 = new RoomImage("roomImage Path 5");

      //then
      assertThrows(RuntimeException.class, () -> room.deleteImage(roomImage5));
    }

    @Test
    @DisplayName("성공: Description 변경할 수 있음")
    public void successChangeDescription() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      String newDescription = "new Description";
      room.setDescription(newDescription);

      //then
      assertThat(room.getDescription()).isEqualTo(newDescription);
    }

    @Test
    @DisplayName("성공: Description을 Null로 변경할 수 있음")
    public void successChangeDescriptionToNull() throws Exception {
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

      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      String newDescription = null;
      room.setDescription(newDescription);

      //then
      assertThat(room.getDescription()).isEqualTo(newDescription);
    }
  }

}