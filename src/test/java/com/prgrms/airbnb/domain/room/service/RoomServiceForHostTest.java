package com.prgrms.airbnb.domain.room.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.amazonaws.services.s3.AmazonS3;
import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.common.exception.BadRequestException;
import com.prgrms.airbnb.domain.common.exception.InvalidParamException;
import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.common.exception.UnAuthorizedAccessException;
import com.prgrms.airbnb.domain.common.service.UploadService;
import com.prgrms.airbnb.domain.localStack.LocalStackS3Config;
import com.prgrms.airbnb.domain.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.dto.UpdateRoomRequest;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomInfo;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import com.prgrms.airbnb.domain.room.entity.SortTypeForHost;
import com.prgrms.airbnb.domain.room.repository.RoomImageRepository;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import com.prgrms.airbnb.domain.user.entity.Group;
import com.prgrms.airbnb.domain.user.entity.User;
import com.prgrms.airbnb.domain.user.repository.GroupRepository;
import com.prgrms.airbnb.domain.user.repository.UserRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LocalStackS3Config.class)
@Transactional
class RoomServiceForHostTest {

  @Autowired
  AmazonS3 amazonS3;

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

  @Autowired
  UploadService uploadService;

  RoomDetailResponse defaultRoomResponse;
  Long hostId;
  Long roomId;

  Address defaultAddress;
  Integer defaultCharge;
  String defaultRoomName;
  String defaultRoomDescription;
  RoomInfo defaultRoomInfo;
  RoomType defaultRoomType;
  List<MultipartFile> defaultMultipartFiles;

  MockMultipartFile mockMultipartFile1;
  MockMultipartFile mockMultipartFile2;

  Long hostId1;
  Long hostId2;
  Long hostId3;

  @BeforeEach
  void setup() throws IOException {

    amazonS3.createBucket("fbnb");

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
    defaultRoomType = RoomType.APARTMENT;
    defaultMultipartFiles = new ArrayList<>();

    String testCustomerUpload1 = "testCustomerUpload1";
    String uploadPath1 = "src/test/resources/uploadFile/testCustomerUpload1.png";
    String png = "png";
    mockMultipartFile1 = getMockMultipartFile(testCustomerUpload1, png, uploadPath1);

    String testCustomerUpload2 = "testCustomerUpload2";
    String uploadPath2 = "src/test/resources/uploadFile//testCustomerUpload2.png";
    mockMultipartFile2 = getMockMultipartFile(testCustomerUpload2, png, uploadPath2);

    defaultMultipartFiles.add(mockMultipartFile1);
    defaultMultipartFiles.add(mockMultipartFile2);

    CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
        .address(defaultAddress)
        .charge(defaultCharge)
        .name(defaultRoomName)
        .description(defaultRoomDescription)
        .roomInfo(defaultRoomInfo)
        .roomType(defaultRoomType)
        .build();

    defaultRoomResponse = roomServiceForHost.save(createRoomRequest, defaultMultipartFiles, hostId);

    roomId = defaultRoomResponse.getId();

    User user1 = userRepository.save(new User("user1", "provider1", "providerId1",
        "profileImage1", group, new Email("aaa1@gmail.com")));

    User user2 = userRepository.save(new User("user2", "provider2", "providerId2",
        "profileImage2", group, new Email("aaa2@gmail.com")));

    User user3 = userRepository.save(new User("user3", "provider3", "providerId3",
        "profileImage3", group, new Email("aaa3@gmail.com")));

    hostId1 = user1.getId();
    hostId2 = user2.getId();
    hostId3 = user3.getId();

    for (int i = 0; i < 10; i++) {
      Address address = new Address("address1" + i, "address2" + i);
      CreateRoomRequest createRoomRequest1 = CreateRoomRequest.builder()
          .address(address)
          .charge(defaultCharge)
          .name("userId1의 Room")
          .description(defaultRoomDescription)
          .roomInfo(defaultRoomInfo)
          .roomType(defaultRoomType)
          .build();
      roomServiceForHost.save(createRoomRequest1, defaultMultipartFiles, hostId1);
    }
    for (int i = 10; i < 20; i++) {
      Address address = new Address("address1" + i, "address2" + i);
      CreateRoomRequest createRoomRequest2 = CreateRoomRequest.builder()
          .address(address)
          .charge(defaultCharge)
          .name("userId2의 Room")
          .description(defaultRoomDescription)
          .roomInfo(defaultRoomInfo)
          .roomType(defaultRoomType)
          .build();
      roomServiceForHost.save(createRoomRequest2, defaultMultipartFiles, hostId2);
    }
    for (int i = 20; i < 30; i++) {
      Address address = new Address("address1" + i, "address2" + i);
      CreateRoomRequest createRoomRequest3 = CreateRoomRequest.builder()
          .address(address)
          .charge(defaultCharge)
          .name("userId3의 Room")
          .description(defaultRoomDescription)
          .roomInfo(defaultRoomInfo)
          .roomType(defaultRoomType)
          .build();
      roomServiceForHost.save(createRoomRequest3, defaultMultipartFiles, hostId3);
    }
  }

  static public MockMultipartFile getMockMultipartFile(String fileName, String contentType,
      String path) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(new File(path));
    return new MockMultipartFile(
        fileName, fileName + "." + contentType, contentType, fileInputStream);
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

  @BeforeEach
  void setupForSave() {

    address = new Address("address1", "address2");
    charge = 10000;
    roomName = "roomName";
    roomDescription = "roomDescription";
    roomInfo = new RoomInfo(1, 1, 1, 1);
  }

  @Nested
  class 저장_save {

    @Test
    @DisplayName("성공: room 저장에 성공합니다.")
    public void success() throws Exception {

      //when
      CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
          .address(address)
          .charge(charge)
          .name(roomName)
          .description(roomDescription)
          .roomInfo(roomInfo)
          .roomType(defaultRoomType)
          .build();

      RoomDetailResponse createRoomResponse
          = roomServiceForHost.save(createRoomRequest, defaultMultipartFiles, hostId);

      //then
      assertThat(createRoomResponse)
          .usingRecursiveComparison()
          .ignoringFields("id","roomImages", "userId")
          .isEqualTo(createRoomRequest);

      assertThat(createRoomResponse.getId()).isNotNull();
      assertThat(createRoomResponse.getRoomImages().size()).isEqualTo(2);
      assertThat(createRoomResponse.getUserId()).isEqualTo(hostId);
    }

    @Test
    @DisplayName("성공: room 저장에 성공합니다.")
    public void successWithoutImage() throws Exception {

      //when
      CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
          .address(address)
          .charge(charge)
          .name(roomName)
          .description(roomDescription)
          .roomInfo(roomInfo)
          .roomType(defaultRoomType)
          .build();

      RoomDetailResponse createRoomResponse = roomServiceForHost.save(
          createRoomRequest,
          defaultMultipartFiles,
          hostId);

      //then
      assertThat(createRoomResponse)
          .usingRecursiveComparison()
          .ignoringFields("id", "roomImages", "userId")
          .isEqualTo(createRoomRequest);

      assertThat(createRoomResponse.getId()).isNotNull();
      assertThat(createRoomResponse.getUserId()).isEqualTo(hostId);
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
          .roomType(defaultRoomType)
          .build();

      //then
      assertThrows(BadRequestException.class,
          () -> roomServiceForHost.save(createRoomRequest, defaultMultipartFiles, hostId));
    }

    @Test
    @DisplayName("성공: 주소를 제외한 어떠한 값도 중복될 수 있습니다.")
    public void success3() throws Exception {

      //given
      Address diffAddress = new Address("address1", "address2");

      //when
      CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
          .address(diffAddress)
          .charge(defaultCharge)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .roomInfo(defaultRoomInfo)
          .roomType(defaultRoomType)
          .build();

      RoomDetailResponse createRoomResponse
          = roomServiceForHost.save(createRoomRequest, defaultMultipartFiles, hostId);

      //then
      assertThat(createRoomResponse)
          .usingRecursiveComparison()
          .ignoringFields("id", "roomImages", "userId")
          .isEqualTo(createRoomRequest);

      assertThat(createRoomResponse.getId()).isNotNull();
      assertThat(createRoomResponse.getUserId()).isEqualTo(hostId);
    }
  }

  Integer changedCharge;
  String changedRoomName;
  String changedDescription;
  Integer changedMaxGuest;
  Integer changedBedCount;
  List<MultipartFile> changedMultipartFiles;
  MockMultipartFile changedMockMultipartFile1;
  MockMultipartFile changedMockMultipartFile2;
  MockMultipartFile changedMockMultipartFile3;

  @BeforeEach
  void setupForModify() throws IOException {
    changedCharge = 5000;
    changedRoomName = "영업 안해요";
    changedDescription = "영업 안합니다";
    changedMaxGuest = 2;
    changedBedCount = 2;
    changedMultipartFiles = new ArrayList<>();

    String testCustomerChange1 = "testCustomerChange1";
    String changePath1 = "src/test/resources/uploadFile/testCustomerChange1.png";
    String png = "png";
    changedMockMultipartFile1 = getMockMultipartFile(testCustomerChange1, png, changePath1);

    String testCustomerChange2 = "testCustomerChange2";
    String changePath2 = "src/test/resources/uploadFile/testCustomerChange2.png";
    changedMockMultipartFile2 = getMockMultipartFile(testCustomerChange2, png, changePath2);

    String testCustomerChange3 = "testCustomerChange3";
    String changePath3 = "src/test/resources/uploadFile/testCustomerChange3.png";
    changedMockMultipartFile3 = getMockMultipartFile(testCustomerChange3, png, changePath3);

    changedMultipartFiles.add(changedMockMultipartFile1);
    changedMultipartFiles.add(changedMockMultipartFile2);
    changedMultipartFiles.add(changedMockMultipartFile3);
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
          .build();

      //when
      RoomDetailResponse modify
          = roomServiceForHost.modify(updateRoomRequest, changedMultipartFiles, hostId);

      //then
      assertThat(modify)
          .usingRecursiveComparison()
          .ignoringFields("address", "roomInfo", "roomImages", "roomType", "userId")
          .isEqualTo(updateRoomRequest);

      assertThat(modify.getRoomInfo().getMaxGuest()).isEqualTo(changedMaxGuest);
      assertThat(modify.getRoomInfo().getBedCount()).isEqualTo(changedBedCount);
      assertThat(modify.getRoomImages().size()).isEqualTo(3);
      assertThat(modify.getUserId()).isEqualTo(hostId);

      List<RoomImage> changeRoomImagesList = changedMultipartFiles.stream().map(
          m -> new RoomImage(uploadService.uploadImg(m))).collect(Collectors.toList());
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
          .build();

      //when
      RoomDetailResponse modify
          = roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId);

      //then
      assertThat(modify.getCharge()).isEqualTo(changedCharge);
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
          .build();

      //when
      RoomDetailResponse modify
          = roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId);

      //then
      assertThat(modify.getCharge()).isEqualTo(zero);
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
          .build();

      //then
      assertThrows(InvalidParamException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId));
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
          .build();

      //then
      assertThrows(InvalidParamException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId));
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
          .build();

      //when
      RoomDetailResponse modify
          = roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId);

      //then
      assertThat(modify.getName()).isEqualTo(changedRoomName);
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
          .build();

      //then
      assertThrows(InvalidParamException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId));
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
          .build();

      //then
      assertThrows(InvalidParamException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId));
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
          .build();

      //then
      assertThrows(InvalidParamException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId));
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
          .build();

      //when
      RoomDetailResponse modify
          = roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId);

      //then
      assertThat(modify.getDescription()).isEqualTo(changedDescription);
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
          .build();

      //when
      RoomDetailResponse modify
          = roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId);

      //then
      assertThat(modify.getDescription()).isNull();
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
          .build();

      //when
      RoomDetailResponse modify
          = roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId);

      //then
      assertThat(modify.getRoomInfo().getMaxGuest()).isEqualTo(changedMaxGuest);
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
          .build();

      //then
      assertThrows(InvalidParamException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId));
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
          .build();

      //then
      assertThrows(InvalidParamException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId));
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
          .build();

      //when
      RoomDetailResponse modify
          = roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId);

      //then
      assertThat(modify.getRoomInfo().getBedCount()).isEqualTo(changedBedCount);
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
          .build();

      //when
      RoomDetailResponse modify
          = roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId);

      //then
      assertThat(modify.getRoomInfo().getBedCount()).isEqualTo(zero);
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
          .build();

      //then
      assertThrows(InvalidParamException.class,
          () -> roomServiceForHost.modify(updateRoomRequest, defaultMultipartFiles, hostId));
    }

    @Test
    @DisplayName("성공: Room 이미지 수정에 성공합니다.")
    public void successChangeImages() throws Exception {

      //given
      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(defaultCharge)
          .name(defaultRoomName)
          .description(defaultRoomDescription)
          .maxGuest(defaultRoomInfo.getMaxGuest())
          .bedCount(defaultRoomInfo.getBedCount())
          .build();

      //when
      RoomDetailResponse modify
          = roomServiceForHost.modify(updateRoomRequest, changedMultipartFiles, hostId);

      //then
      assertThat(modify.getRoomImages().size()).isEqualTo(changedMultipartFiles.size());
    }

    @Test
    @DisplayName("성공: 수정할 Room 이미지 목록이 null이라면 roomImage를 제외하고 수정이 이뤄집니다.")
    public void failChangeImagesToNull() throws Exception {

      //given
      changedMultipartFiles = null;

      UpdateRoomRequest updateRoomRequest = UpdateRoomRequest.builder()
          .id(roomId)
          .charge(changedCharge)
          .name(changedRoomName)
          .description(defaultRoomDescription)
          .maxGuest(changedMaxGuest)
          .bedCount(changedBedCount)
          .build();

      //when
      RoomDetailResponse modify
          = roomServiceForHost.modify(updateRoomRequest, changedMultipartFiles, hostId);

      //then
      assertThat(modify)
          .usingRecursiveComparison()
          .ignoringFields("address", "roomInfo", "roomImages", "roomType", "userId")
          .isEqualTo(updateRoomRequest);

      assertThat(modify.getRoomInfo().getMaxGuest()).isEqualTo(changedMaxGuest);
      assertThat(modify.getRoomInfo().getBedCount()).isEqualTo(changedBedCount);
      assertThat(modify.getRoomImages()).isEqualTo(defaultRoomResponse.getRoomImages());
      assertThat(modify.getUserId()).isEqualTo(hostId);
    }
  }

  @Nested
  class 상세정보_조회_findDetailById {

    @Test
    @DisplayName("성공: 상세정보 조회 성공")
    public void successFindDetailById() throws Exception {

      //when
      RoomDetailResponse roomDetailResponse = roomServiceForHost.findDetailById(roomId, hostId);

      //then
      assertThat(roomDetailResponse)
          .usingRecursiveComparison()
          .isEqualTo(defaultRoomResponse);
    }

    @Test
    @DisplayName("실패: roomId는 존재하지만 hostId가 틀릴경우")
    public void failWrongHostId() throws Exception {

      //when
      Long wrongHostId = -1L;

      //then
      assertThrows(UnAuthorizedAccessException.class,
          () -> roomServiceForHost.findDetailById(roomId, wrongHostId));
    }

    @Test
    @DisplayName("실패: hostId는 존재하지만 roomId 틀릴경우")
    public void failWrongRoomId() throws Exception {

      //when
      Long wrongRoomId = -1L;

      //then
      assertThrows(NotFoundException.class,
          () -> roomServiceForHost.findDetailById(wrongRoomId, hostId));
    }

    @Test
    @DisplayName("실패: hostId, roomId 틀릴경우")
    public void failWrongRoomIdAndWrongHostId() throws Exception {

      //when
      Long wrongRoomId = -1L;
      Long wrongHostId = -2L;

      //then
      assertThrows(NotFoundException.class,
          () -> roomServiceForHost.findDetailById(wrongRoomId, wrongHostId));
    }
  }

  @Nested
  @DisplayName("host가 등록한 room list 조회")
  class findByHostId {

    @Test
    @DisplayName("성공: 최신순으로 조회")
    public void successSortTypeIsRecently() throws Exception {

      //given
      int size = 5;
      PageRequest pageable = PageRequest.of(0, size);

      //when
      Slice<RoomSummaryResponse> byHostId
          = roomServiceForHost.findByHostId(hostId1, SortTypeForHost.RECENTLY, pageable);

      //then
      assertThat(byHostId.getSize()).isEqualTo(size);

      for (RoomSummaryResponse roomSummaryResponse : byHostId) {
        assertThat(roomSummaryResponse.getName()).contains("userId1");
      }
    }

    @Test
    @DisplayName("성공: 호스트가 등록한 Room이 없을 경우 요소 = 0")
    public void successSortTypeIsRecentlyResultIsZero() throws Exception {

      //given
      int size = 5;
      PageRequest pageable = PageRequest.of(0, size);
      Long testHostId = -1L;

      //when
      Slice<RoomSummaryResponse> byHostId
          = roomServiceForHost.findByHostId(testHostId, SortTypeForHost.RECENTLY, pageable);

      //then
      assertThat(byHostId.getSize()).isEqualTo(size);
      assertThat(byHostId.getNumberOfElements()).isEqualTo(0);
    }
  }

  @Nested
  class 삭제_remove {

    @Test
    @DisplayName("성공: 삭제 성공")
    public void success() throws Exception {

      //when
      roomServiceForHost.remove(roomId, hostId);

      //then
      assertThat(roomRepository.findById(roomId).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("실패: roomId가 존재하지 않을경우")
    public void failWrongRoomId() throws Exception {

      //given
      Long wrongRoomId = null;

      //then
      // TODO: 2022/07/05 null로 조회할 경우 InvalidDataAccessApiUsageException이 발생하는데 이에 대한 해결 방향을 회으를 통해 알아봅시다. 
      assertThrows(NotFoundException.class, () -> roomServiceForHost.remove(wrongRoomId, hostId));
    }

    @Test
    @DisplayName("실패: hostId가 다를 경우")
    public void failWrongHostId() throws Exception {

      //then
      assertThrows(UnAuthorizedAccessException.class, () -> roomServiceForHost.remove(roomId, hostId1));
    }
  }
}

