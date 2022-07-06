package com.prgrms.airbnb.domain.room.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.common.exception.InvalidParamException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RoomTest {

  Address address;
  Integer charge;
  String roomName;
  String roomDescription;
  RoomInfo roomInfo;
  List<RoomImage> images;
  RoomImage roomImage1;
  RoomImage roomImage2;
  RoomImage roomImage3;
  RoomImage roomImage4;
  Long hostId;

  @BeforeEach
  void setup() {
    address = new Address("address1", "address2");
    charge = 10000;
    roomName = "roomName";
    roomDescription = "roomDescription";
    roomInfo = new RoomInfo(1, 1, 1, 1);
    images = new ArrayList<>();
    roomImage1 = new RoomImage("roomImage Path 1");
    roomImage2 = new RoomImage("roomImage Path 2");
    roomImage3 = new RoomImage("roomImage Path 3");
    roomImage4 = new RoomImage("roomImage Path 4");
    images.add(roomImage1);
    images.add(roomImage2);
    images.add(roomImage3);
    images.add(roomImage4);
    hostId = 1000L;
  }

  @Nested
  class 생성 {

    @Test
    @DisplayName("성공: 모든 조건이 들어갈 때.")
    public void success1() throws Exception {

      //when
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //then
      assertThat(room.getAddress()).isEqualTo(address);
      assertThat(room.getCharge()).isEqualTo(charge);
      assertThat(room.getName()).isEqualTo(roomName);
      assertThat(room.getDescription()).isEqualTo(roomDescription);
      assertThat(room.getRoomInfo()).isEqualTo(roomInfo);
      assertThat(room.getRoomImages()).contains(roomImage1);
      assertThat(room.getRoomImages()).contains(roomImage2);
      assertThat(room.getRoomImages()).contains(roomImage3);
      assertThat(room.getRoomImages()).contains(roomImage4);
      assertThat(room.getUserId()).isEqualTo(hostId);
    }

    @Test
    @DisplayName("성공: description = null")
    public void success2() throws Exception {

      //when
      String testDescription = null;
      Room room = new Room(address, charge, roomName, testDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //then
      assertThat(room.getDescription()).isEqualTo(testDescription);
    }

    @Test
    @DisplayName("성공: images가 빈 List일 경우")
    public void success3() throws Exception {
      //given
      List<RoomImage> testImages = new ArrayList<>();

      //when
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          testImages, hostId);

      //then
      assertThat(room.getRoomImages().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("성공: charge = 0")
    public void success5() throws Exception {

      //given
      Integer testCharge = 0;

      //when
      Room room = new Room(address, testCharge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //then
      assertThat(room.getCharge()).isEqualTo(testCharge);
    }

    @Test
    @DisplayName("실패: name = null -> 예외 발생")
    public void failNameIsNull() throws Exception {

      //when
      String testRoomName = null;

      //then
      assertThrows(InvalidParamException.class,
          () -> new Room(address, charge, testRoomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

    @Test
    @DisplayName("실패: name = empty -> 예외 발생")
    public void failNameIsEmpty() throws Exception {

      //when
      String testRoomName = "";

      //then
      assertThrows(InvalidParamException.class,
          () -> new Room(address, charge, testRoomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

    @Test
    @DisplayName("실패: name = blank -> 예외 발생")
    public void failNameIsBlank() throws Exception {

      //when
      String testRoomName = " ";

      //then
      assertThrows(InvalidParamException.class,
          () -> new Room(address, charge, testRoomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

    @Test
    @DisplayName("실패: charge = null -> 예외 발생")
    public void failChargeIsNull() throws Exception {

      //when
      Integer testCharge = null;

      //then
      assertThrows(InvalidParamException.class,
          () -> new Room(address, testCharge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

    @Test
    @DisplayName("실패: charge < 0 -> 예외 발생")
    public void failChargeIsNegative() throws Exception {

      //when
      Integer testCharge = -10;

      //then
      assertThrows(InvalidParamException.class,
          () -> new Room(address, testCharge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

    @Test
    @DisplayName("실패: hostId = null 예외 발생")
    public void failHostIdIsNull() throws Exception {

      //when
      Long testHostId = null;

      //then
      assertThrows(InvalidParamException.class,
          () -> new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, testHostId));
    }

    @Test
    @DisplayName("실패: address = null -> 예외 발생")
    public void failAddressIsNull() throws Exception {

      //when
      Address testAddress = null;

      //then
      assertThrows(InvalidParamException.class,
          () -> new Room(testAddress, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
              images, hostId));
    }

    @Test
    @DisplayName("실패: images를 null로 입력 -> 양방향 연관관계를 stream.forEach때문에 반드시 빈 리스트라도 들어가야 함")
    public void failListIsNull() throws Exception {

      //when
      List<RoomImage> testImages = null;

      //then
      assertThrows(NullPointerException.class,
          () -> new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
              testImages, hostId));
    }
  }

  @Nested
  class 수정{

    @Test
    @DisplayName("성공: Room 이름을 변경할 수 있음")
    public void successChangeName() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      String newRoomName = "new roomName";
      room.changeName(newRoomName);

      //then
      assertThat(room.getName()).isEqualTo(newRoomName);
    }

    @Test
    @DisplayName("실패: Room 이름을 null 로 변경할 수 없음")
    public void failChangeNameToNull() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      String newRoomName = null;

      //then
      assertThrows(InvalidParamException.class, () -> room.changeName(newRoomName));
    }

    @Test
    @DisplayName("실패: Room 이름을 empty 로 변경할 수 없음")
    public void failChangeNameToEmpty() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      String newRoomName = "";

      //then
      assertThrows(InvalidParamException.class, () -> room.changeName(newRoomName));
    }

    @Test
    @DisplayName("실패: Room 이름을 blank 로 변경할 수 없음")
    public void failChangeNameToBlank() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      String newRoomName = " ";

      //then
      assertThrows(InvalidParamException.class, () -> room.changeName(newRoomName));
    }

    @Test
    @DisplayName("성공: Room 가격을 양수 값으로 변경할 수 있음")
    public void successChangeChargeToPositive() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      Integer newCharge = 10;
      room.changeCharge(newCharge);

      //then
      assertThat(room.getCharge()).isEqualTo(newCharge);
    }

    @Test
    @DisplayName("성공: Room 가격을 0으로 변경할 수 있음")
    public void successChangeChargeToZero() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      Integer newCharge = 0;
      room.changeCharge(newCharge);

      //then
      assertThat(room.getCharge()).isEqualTo(newCharge);
    }

    @Test
    @DisplayName("실패: Room 가격을 음수로 변경할 수 없음")
    public void failChangeChargeToNegative() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      Integer newCharge = -1;

      //then
      assertThrows(InvalidParamException.class, () -> room.changeCharge(newCharge));
    }

    @Test
    @DisplayName("성공: 새로운 이미지를 추가할 수 있음")
    public void successAddNewImage() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      RoomImage newRoomImage = new RoomImage("new Room Image Path");
      room.setImage(newRoomImage);

      //then
      assertThat(room.getRoomImages()).contains(newRoomImage);
    }

    @Test
    @DisplayName("실패: null인 image를 추가할 수 없음")
    public void failChangeImagesToNull() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      RoomImage newRoomImage = null;

      //then
      assertThrows(InvalidParamException.class, () -> room.setImage(newRoomImage));
    }

    @Test
    @DisplayName("성공: 기존 이미지를 삭제할 수 있음")
    public void successDeleteImage() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      room.deleteImage(roomImage4);

      //then
      assertThat(room.getRoomImages()).doesNotContain(roomImage4);
    }

    @Test
    @DisplayName("실패: 가지고 있지 않은 image를 삭제할 수 없음")
    public void failImageDeleteImageNotContained() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      RoomImage roomImage5 = new RoomImage("roomImage Path 5");

      //then
      assertThrows(InvalidParamException.class, () -> room.deleteImage(roomImage5));
    }

    @Test
    @DisplayName("성공: Description 변경할 수 있음")
    public void successChangeDescription() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      String newDescription = "new Description";
      room.changeDescription(newDescription);

      //then
      assertThat(room.getDescription()).isEqualTo(newDescription);
    }

    @Test
    @DisplayName("성공: Description을 Null로 변경할 수 있음")
    public void successChangeDescriptionToNull() throws Exception {

      //given
      Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
          images, hostId);

      //when
      String newDescription = null;
      room.changeDescription(newDescription);

      //then
      assertThat(room.getDescription()).isEqualTo(newDescription);
    }
  }
}