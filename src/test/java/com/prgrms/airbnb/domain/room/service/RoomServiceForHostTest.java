package com.prgrms.airbnb.domain.room.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.dto.UpdateRoomRequest;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomInfo;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import com.prgrms.airbnb.domain.room.repository.RoomImageRepository;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import com.prgrms.airbnb.domain.user.entity.Group;
import com.prgrms.airbnb.domain.user.entity.User;
import com.prgrms.airbnb.domain.user.repository.GroupRepository;
import com.prgrms.airbnb.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RoomServiceForHostTest {

  @Autowired
  RoomRepository roomRepository;

  @Autowired
  RoomServiceForHost roomServiceForHost;

  @Autowired
  UserRepository userRepository;

  @Autowired
  GroupRepository groupRepository;

  @Autowired
  RoomImageRepository roomImageRepository;

  @BeforeEach
  void setup() {
    Group group = groupRepository.findByName("USER_GROUP")
        .orElseThrow(() -> new IllegalStateException("Could not found group for USER_GROUP"));

    User user = userRepository.save(new User("user",
        "provider",
        "providerId",
        "profileImage",
        group,
        new Email("aaa@gmail.com")));
    hostId = user.getId();

    Address address = new Address("default address1", "default address2");
    Integer charge = 10000;
    String roomName = "default roomName";
    String roomDescription = "default roomDescription";
    RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
    List<RoomImage> images = new ArrayList<>();
    RoomImage roomImage1 = new RoomImage("default roomImage Path 1");
    images.add(roomImage1);
    RoomImage roomImage2 = new RoomImage("default roomImage Path 2");
    images.add(roomImage2);
    RoomImage roomImage3 = new RoomImage("default roomImage Path 3");
    images.add(roomImage3);
    RoomImage roomImage4 = new RoomImage("default roomImage Path 4");
    images.add(roomImage4);

    Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
        images, hostId);

    defaultRoom = roomRepository.save(room);
    roomId = defaultRoom.getId();
  }

  @AfterEach
  void cleanUp() {
    roomImageRepository.deleteAll();
    userRepository.deleteAll();
    roomRepository.deleteAll();
  }

  Room defaultRoom;
  Long hostId;
  Long roomId;

  @Nested
  class 저장_save {

    @Test
    @DisplayName("성공: room 저장에 성공합니다. roomImage도 관련 repo에 저장됩니다.")
    public void success() throws Exception {

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

      //when
      CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
          .address(address)
          .charge(charge)
          .name(roomName)
          .description(roomDescription)
          .roomInfo(roomInfo)
          .roomType(RoomType.APARTMENT)
          .roomImages(images)
          .build();

      RoomDetailResponse createRoomResponse = roomServiceForHost.save(createRoomRequest, hostId);

      //then
      assertThat(createRoomResponse.getId()).isNotNull();
      assertThat(createRoomResponse.getAddress()).isEqualTo(address);
      assertThat(createRoomResponse.getCharge()).isEqualTo(charge);
      assertThat(createRoomResponse.getName()).isEqualTo(roomName);
      assertThat(createRoomResponse.getDescription()).isEqualTo(roomDescription);
      assertThat(createRoomResponse.getRoomInfo()).isEqualTo(roomInfo);
      assertThat(createRoomResponse.getImages()).contains(roomImage1);
      assertThat(createRoomResponse.getImages()).contains(roomImage2);
      assertThat(createRoomResponse.getImages()).contains(roomImage3);
      assertThat(createRoomResponse.getImages()).contains(roomImage4);
      assertThat(createRoomResponse.getUserId()).isEqualTo(hostId);

      List<RoomImage> allRoomImages = roomImageRepository.findAll();

      assertThat(allRoomImages).contains(roomImage1);
      assertThat(allRoomImages).contains(roomImage2);
      assertThat(allRoomImages).contains(roomImage3);
      assertThat(allRoomImages).contains(roomImage4);
    }

    @Test
    @DisplayName("성공: room 저장에 성공합니다. roomImage가 없다면 repo역시 저장되지 않습니다.")
    public void successWithoutImage() throws Exception {

      //given
      Address address = new Address("address1", "address2");
      Integer charge = 10000;
      String roomName = "roomName";
      String roomDescription = "roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();

      RoomImage roomImage1 = new RoomImage("roomImage Path 1");
      RoomImage roomImage2 = new RoomImage("roomImage Path 2");
      RoomImage roomImage3 = new RoomImage("roomImage Path 3");
      RoomImage roomImage4 = new RoomImage("roomImage Path 4");

      //when
      CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
          .address(address)
          .charge(charge)
          .name(roomName)
          .description(roomDescription)
          .roomInfo(roomInfo)
          .roomType(RoomType.APARTMENT)
          .build();

      RoomDetailResponse createRoomResponse = roomServiceForHost.save(createRoomRequest, hostId);

      //then
      assertThat(createRoomResponse.getId()).isNotNull();
      assertThat(createRoomResponse.getAddress()).isEqualTo(address);
      assertThat(createRoomResponse.getCharge()).isEqualTo(charge);
      assertThat(createRoomResponse.getName()).isEqualTo(roomName);
      assertThat(createRoomResponse.getDescription()).isEqualTo(roomDescription);
      assertThat(createRoomResponse.getRoomInfo()).isEqualTo(roomInfo);
      assertThat(createRoomResponse.getImages()).doesNotContain(roomImage1);
      assertThat(createRoomResponse.getImages()).doesNotContain(roomImage2);
      assertThat(createRoomResponse.getImages()).doesNotContain(roomImage3);
      assertThat(createRoomResponse.getImages()).doesNotContain(roomImage4);
      assertThat(createRoomResponse.getUserId()).isEqualTo(hostId);

      List<RoomImage> allRoomImages = roomImageRepository.findAll();

      assertThat(allRoomImages).doesNotContain(roomImage1);
      assertThat(allRoomImages).doesNotContain(roomImage2);
      assertThat(allRoomImages).doesNotContain(roomImage3);
      assertThat(allRoomImages).doesNotContain(roomImage4);
    }

    @Test
    @DisplayName("실패: DB에 같은 주소의 Room이 존재한다면 새 Room 을 등록할 수 없습니다.")
    public void failCreateSameAddressRoom() throws Exception {

      //given
      Integer charge = 10000;
      String roomName = "new roomName";
      String roomDescription = "new roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();
      RoomImage roomImage1 = new RoomImage("new roomImage Path 1");
      images.add(roomImage1);
      RoomImage roomImage2 = new RoomImage("new roomImage Path 2");
      images.add(roomImage2);
      RoomImage roomImage3 = new RoomImage("new roomImage Path 3");
      images.add(roomImage3);
      RoomImage roomImage4 = new RoomImage("new roomImage Path 4");
      images.add(roomImage4);

      //when
      Address address = new Address("default address1", "default address2");

      CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
          .address(address)
          .charge(charge)
          .name(roomName)
          .description(roomDescription)
          .roomInfo(roomInfo)
          .roomType(RoomType.APARTMENT)
          .roomImages(images)
          .build();

      //then
      Assertions.assertThrows(RuntimeException.class,
          () -> roomServiceForHost.save(createRoomRequest, hostId));
    }

    @Test
    @DisplayName("성공: 주소를 제외한 어떠한 값도 중복될 수 있습니다.")
    public void success3() throws Exception {

      //given
      Integer charge = 10000;
      String roomName = "default roomName";
      String roomDescription = "default roomDescription";
      RoomInfo roomInfo = new RoomInfo(1, 1, 1, 1);
      List<RoomImage> images = new ArrayList<>();
      RoomImage roomImage1 = new RoomImage("default roomImage Path 1");
      images.add(roomImage1);
      RoomImage roomImage2 = new RoomImage("default roomImage Path 2");
      images.add(roomImage2);
      RoomImage roomImage3 = new RoomImage("default roomImage Path 3");
      images.add(roomImage3);
      RoomImage roomImage4 = new RoomImage("default roomImage Path 4");
      images.add(roomImage4);

      //when
      Address address = new Address("address1", "address2");
      CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
          .address(address)
          .charge(charge)
          .name(roomName)
          .description(roomDescription)
          .roomInfo(roomInfo)
          .roomType(RoomType.APARTMENT)
          .roomImages(images)
          .build();

      RoomDetailResponse createRoomResponse = roomServiceForHost.save(createRoomRequest, hostId);

      //then
      assertThat(createRoomResponse.getId()).isNotNull();
      assertThat(createRoomResponse.getAddress()).isEqualTo(address);
      assertThat(createRoomResponse.getCharge()).isEqualTo(charge);
      assertThat(createRoomResponse.getName()).isEqualTo(roomName);
      assertThat(createRoomResponse.getDescription()).isEqualTo(roomDescription);
      assertThat(createRoomResponse.getRoomInfo()).isEqualTo(roomInfo);
      assertThat(createRoomResponse.getImages()).contains(roomImage1);
      assertThat(createRoomResponse.getImages()).contains(roomImage2);
      assertThat(createRoomResponse.getImages()).contains(roomImage3);
      assertThat(createRoomResponse.getImages()).contains(roomImage4);
      assertThat(createRoomResponse.getUserId()).isEqualTo(hostId);

      List<RoomImage> allRoomImages = roomImageRepository.findAll();

      assertThat(allRoomImages).contains(roomImage1);
      assertThat(allRoomImages).contains(roomImage2);
      assertThat(allRoomImages).contains(roomImage3);
      assertThat(allRoomImages).contains(roomImage4);
    }
  }

  @Nested
  class 수정_modify {

    @Test
    @DisplayName("성공: Room 수정에 성공합니다.")
    public void success() throws Exception {

      List<RoomImage> roomImages = new ArrayList<>();
      RoomImage roomImage1 = new RoomImage("roomImage path");
      roomImages.add(roomImage1);
      //given
      Integer charge = 5000;
      String roomName = "영업 안해요";
      String description = "영업 안합니다";
      Integer maxGuest = 2;
      Integer bedCount = 2;
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(charge)
          .name(roomName)
          .description(description)
          .maxGuest(maxGuest)
          .bedCount(bedCount)
          .images(roomImages)
          .build();

      //when
      RoomDetailResponse modify = roomServiceForHost.modify(updateRoomRequest, hostId);

      //then
      Room room = roomRepository.findById(roomId).orElseThrow(RuntimeException::new);

      assertThat(room.getId()).isEqualTo(roomId);
      assertThat(room.getCharge()).isEqualTo(charge);
      assertThat(room.getName()).isEqualTo(roomName);
      assertThat(room.getDescription()).isEqualTo(description);
      assertThat(room.getRoomInfo().getMaxGuest()).isEqualTo(maxGuest);
      assertThat(room.getRoomInfo().getBedCount()).isEqualTo(bedCount);
      assertThat(room.getUserId()).isEqualTo(hostId);
      assertThat(room.getImages().size()).isEqualTo(roomImages.size());
    }
  }
}