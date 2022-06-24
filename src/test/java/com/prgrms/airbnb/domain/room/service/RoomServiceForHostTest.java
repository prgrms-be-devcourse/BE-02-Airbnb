package com.prgrms.airbnb.domain.room.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomInfo;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import com.prgrms.airbnb.domain.room.repository.RoomImageRepository;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import com.prgrms.airbnb.domain.room.util.RoomConverter;
import com.prgrms.airbnb.domain.user.entity.Group;
import com.prgrms.airbnb.domain.user.entity.User;
import com.prgrms.airbnb.domain.user.repository.GroupRepository;
import com.prgrms.airbnb.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
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
        "pr",
        "prId",
        "proim",
        group,
        new Email("aaa@gmail.com")));
    id = user.getId();

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
    Long hostId = 1000L;

    Room room = new Room(address, charge, roomName, roomDescription, roomInfo, RoomType.APARTMENT,
        images, hostId);

    defaultRoom = roomRepository.save(room);
  }

  Room defaultRoom;
  Long id;

  @Nested
  class 저장 {

    @Test
    @DisplayName("성공: room 저장에 성공합니다.")
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
      Long hostId = id;

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
    }

  }

}