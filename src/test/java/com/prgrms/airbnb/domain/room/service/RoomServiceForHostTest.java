package com.prgrms.airbnb.domain.room.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
  UserRepository userRepository;

  @Autowired
  GroupRepository groupRepository;

  @Autowired
  RoomImageRepository roomImageRepository;

  @Autowired
  RoomServiceForHost roomServiceForHost;

  Room defaultRoom;
  Long hostId;
  Long roomId;

  Address defaultAddress;
  Integer defaultCharge;
  String defaultRoomName;
  String defaultRoomDescription;
  RoomInfo defaultRoomInfo;
  List<RoomImage> defaultImages;
  RoomImage defaultRoomImage1;
  RoomImage defaultRoomImage2;
  RoomImage defaultRoomImage3;
  RoomImage defaultRoomImage4;

  @BeforeEach
  void setup() {
    Group group = groupRepository.findByName("USER_GROUP")
        .orElseThrow(() -> new IllegalStateException("Could not found group for USER_GROUP"));

    User user = userRepository.save(new User("user", "provider", "providerId",
        "profileImage", group, new Email("aaa@gmail.com")));

    hostId = user.getId();

    defaultAddress = new Address("default address1", "default address2");
    defaultCharge = 20000;
    defaultRoomName = "default roomName";
    defaultRoomDescription = "default roomDescription";
    defaultRoomInfo = new RoomInfo(1, 1, 1, 1);
    defaultImages = new ArrayList<>();
    defaultRoomImage1 = new RoomImage("default roomImage Path 1");
    defaultRoomImage2 = new RoomImage("default roomImage Path 2");
    defaultRoomImage3 = new RoomImage("default roomImage Path 3");
    defaultRoomImage4 = new RoomImage("default roomImage Path 4");
    defaultImages.add(defaultRoomImage1);
    defaultImages.add(defaultRoomImage2);
    defaultImages.add(defaultRoomImage3);
    defaultImages.add(defaultRoomImage4);

    Room room = new Room(defaultAddress, defaultCharge, defaultRoomName, defaultRoomDescription,
        defaultRoomInfo, RoomType.APARTMENT, defaultImages, hostId);

    defaultRoom = roomRepository.save(room);
    roomId = defaultRoom.getId();
  }

  @AfterEach
  void cleanUp() {
    roomImageRepository.deleteAll();
    userRepository.deleteAll();
    roomRepository.deleteAll();
  }

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

  @BeforeEach
  void setupForSave() {

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

  }

  @Nested
  class 저장_save {

    @Test
    @DisplayName("성공: room 저장에 성공합니다. roomImage도 관련 repo에 저장됩니다.")
    public void success() throws Exception {

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
      assertThrows(RuntimeException.class,
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

  Integer changedCharge;
  String changedRoomName;
  String changedDescription;
  Integer changedMaxGuest;
  Integer changedBedCount;
  List<RoomImage> changedRoomImages;
  RoomImage changedRoomImage1;

  @BeforeEach
  void setupForModify() {
    changedCharge = 5000;
    changedRoomName = "영업 안해요";
    changedDescription = "영업 안합니다";
    changedMaxGuest = 2;
    changedBedCount = 2;
    changedRoomImages = new ArrayList<>();
    changedRoomImage1 = new RoomImage("roomImage path");
    changedRoomImages.add(changedRoomImage1);

  }
  @Nested
  class 수정_modify {

    @Test
    @DisplayName("성공: Room 전체 수정에 성공합니다.")
    public void success() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(changedCharge)
          .name(changedRoomName)
          .description(changedDescription)
          .maxGuest(changedMaxGuest)
          .bedCount(changedBedCount)
          .images(changedRoomImages)
          .build();

      //when
      RoomDetailResponse modify = roomServiceForHost.modify(updateRoomRequest, hostId);

      //then
      Room room = roomRepository.findById(roomId).orElseThrow(RuntimeException::new);

      assertThat(room.getId()).isEqualTo(roomId);
      assertThat(room.getCharge()).isEqualTo(changedCharge);
      assertThat(room.getName()).isEqualTo(changedRoomName);
      assertThat(room.getDescription()).isEqualTo(changedDescription);
      assertThat(room.getRoomInfo().getMaxGuest()).isEqualTo(changedMaxGuest);
      assertThat(room.getRoomInfo().getBedCount()).isEqualTo(changedBedCount);
      assertThat(room.getUserId()).isEqualTo(hostId);
      assertThat(room.getImages().size()).isEqualTo(changedRoomImages.size());
    }

    @Test
    @DisplayName("성공: Room 가격 수정에 성공합니다.")
    public void successChangeCharge() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(changedCharge)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //when
      RoomDetailResponse modify = roomServiceForHost.modify(updateRoomRequest, hostId);

      //then
      Room room = roomRepository.findById(roomId).orElseThrow(RuntimeException::new);

      assertThat(room.getCharge()).isEqualTo(changedCharge);
    }

    @Test
    @DisplayName("성공: Room 가격을 0으로 수정에 성공합니다.")
    public void successChangeChargeToZero() throws Exception {

      Integer zero = 0;
      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(zero)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //when
      RoomDetailResponse modify = roomServiceForHost.modify(updateRoomRequest, hostId);

      //then
      Room room = roomRepository.findById(roomId).orElseThrow(RuntimeException::new);

      assertThat(room.getCharge()).isEqualTo(zero);
    }

    @Test
    @DisplayName("실패: Room 가격을 null 수정에 실패합니다.")
    public void failChangeChargeToNull() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(null)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //then
      assertThrows(RuntimeException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, hostId));
    }

    @Test
    @DisplayName("실패: Room 가격을 음수로 수정에 실패합니다.")
    public void failChangeChargeToNegative() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(-1)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //then
      assertThrows(RuntimeException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, hostId));
    }

    @Test
    @DisplayName("성공: Room 이름 수정에 성공합니다.")
    public void successChangeName() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(changedRoomName)
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //when
      RoomDetailResponse modify = roomServiceForHost.modify(updateRoomRequest, hostId);

      //then
      Room room = roomRepository.findById(roomId).orElseThrow(RuntimeException::new);

      assertThat(room.getName()).isEqualTo(changedRoomName);
    }

    @Test
    @DisplayName("실패: Room 이름을 null 수정에 실패합니다.")
    public void failChangeNameToNull() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(null)
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //then
      assertThrows(RuntimeException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, hostId));
    }

    @Test
    @DisplayName("실패: Room 이름을 blank 수정에 실패합니다.")
    public void failChangeNameToBlank() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(" ")
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //then
      assertThrows(RuntimeException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, hostId));
    }

    @Test
    @DisplayName("실패: Room 이름을 empty 수정에 실패합니다.")
    public void failChangeNameToEmpty() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name("")
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //then
      assertThrows(RuntimeException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, hostId));
    }

    @Test
    @DisplayName("성공: Room 상세정보 수정에 성공합니다.")
    public void successChangeDescription() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(defaultRoomName)
          .description(changedDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //when
      RoomDetailResponse modify = roomServiceForHost.modify(updateRoomRequest, hostId);

      //then
      Room room = roomRepository.findById(roomId).orElseThrow(RuntimeException::new);

      assertThat(room.getDescription()).isEqualTo(changedDescription);
    }

    @Test
    @DisplayName("성공: Room 상세정보 null 수정에 성공합니다.")
    public void successChangeDescriptionToNull() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(defaultRoomName)
          .description(null)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //when
      RoomDetailResponse modify = roomServiceForHost.modify(updateRoomRequest, hostId);

      //then
      Room room = roomRepository.findById(roomId).orElseThrow(RuntimeException::new);

      assertThat(room.getDescription()).isNull();
    }

    @Test
    @DisplayName("성공: Room 최대수용인원 수정에 성공합니다.")
    public void successChangeMaxGuest() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(changedMaxGuest)
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //when
      RoomDetailResponse modify = roomServiceForHost.modify(updateRoomRequest, hostId);

      //then
      Room room = roomRepository.findById(roomId).orElseThrow(RuntimeException::new);

      assertThat(room.getRoomInfo().getMaxGuest()).isEqualTo(changedMaxGuest);
    }

    @Test
    @DisplayName("실패: Room 최대수용인원을 0으로 수정에 실패합니다.")
    public void failChangeMaxGuestToZero() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(0)
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //then
      assertThrows(RuntimeException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, hostId));
    }


    @Test
    @DisplayName("실패: Room 최대수용인원을 음수로 수정에 실패합니다.")
    public void failChangeMaxGuestNegative() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(-1)
          .bedCount(defaultRoomInfo.getBedCount())
          .images(defaultImages)
          .build();

      //then
      assertThrows(RuntimeException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, hostId));
    }

    @Test
    @DisplayName("성공: Room 침대 개수 수정에 성공합니다.")
    public void successChangeBedCount() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(changedBedCount)
          .images(defaultImages)
          .build();

      //when
      RoomDetailResponse modify = roomServiceForHost.modify(updateRoomRequest, hostId);

      //then
      Room room = roomRepository.findById(roomId).orElseThrow(RuntimeException::new);

      assertThat(room.getRoomInfo().getBedCount()).isEqualTo(changedBedCount);
    }

    @Test
    @DisplayName("성공: Room 침대 개수 0으로 수정에 성공합니다.")
    public void successChangeBedCountToZero() throws Exception {

      //given
      Integer zero = 0;

      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(zero)
          .images(defaultImages)
          .build();

      //when
      RoomDetailResponse modify = roomServiceForHost.modify(updateRoomRequest, hostId);

      //then
      Room room = roomRepository.findById(roomId).orElseThrow(RuntimeException::new);

      assertThat(room.getRoomInfo().getBedCount()).isEqualTo(zero);
    }

    @Test
    @DisplayName("실패: Room 침대 개수 음수로 수정에 실패합니다.")
    public void failChangeBedCountToNegative() throws Exception {

      //given
      Integer negative = -1;

      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(negative)
          .images(defaultImages)
          .build();

      //then
      assertThrows(RuntimeException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, hostId));
    }

    @Test
    @DisplayName("성공: Room 이미지 수정에 성공합니다.")
    public void successChangeImages() throws Exception {

      //given
      RoomImage roomImage1 = new RoomImage("new path1");
      RoomImage roomImage2 = new RoomImage("new path2");
      RoomImage roomImage3 = new RoomImage("new path3");
      changedRoomImages.add(roomImage1);
      changedRoomImages.add(roomImage2);
      changedRoomImages.add(roomImage3);
      changedRoomImages.add(defaultRoomImage2);
      changedRoomImages.add(defaultRoomImage3);

      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .images(changedRoomImages)
          .build();

      //when
      RoomDetailResponse modify = roomServiceForHost.modify(updateRoomRequest, hostId);

      //then
      Room room = roomRepository.findById(roomId).orElseThrow(RuntimeException::new);

      assertThat(room.getImages().size()).isEqualTo(changedRoomImages.size());
    }

    @Test
    @DisplayName("실패: 수정할 Room 이미지 목록은 null이 아니어야합니다")
    public void failChangeImagesToNull() throws Exception {

      //given
      changedRoomImages = null;

      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .images(changedRoomImages)
          .build();

      //then
      assertThrows(RuntimeException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, hostId));
    }
  }


}