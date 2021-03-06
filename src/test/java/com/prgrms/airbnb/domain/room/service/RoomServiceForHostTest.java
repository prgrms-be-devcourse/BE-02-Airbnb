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
          .name("userId1??? Room")
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
          .name("userId2??? Room")
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
          .name("userId3??? Room")
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
  class ??????_save {

    @Test
    @DisplayName("??????: room ????????? ???????????????.")
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
    @DisplayName("??????: room ????????? ???????????????.")
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
    @DisplayName("??????: DB??? ?????? ????????? Room??? ??????????????? ??? Room ??? ????????? ??? ????????????.")
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
    @DisplayName("??????: ????????? ????????? ????????? ?????? ????????? ??? ????????????.")
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
    changedRoomName = "?????? ?????????";
    changedDescription = "?????? ????????????";
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
  class ??????_modify {

    @Test
    @DisplayName("??????: Room ?????? ????????? ???????????????.")
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
    @DisplayName("??????: Room ?????? ????????? ???????????????.")
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
    @DisplayName("??????: Room ????????? 0?????? ????????? ???????????????.")
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
    @DisplayName("??????: Room ????????? null ????????? ???????????????.")
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
    @DisplayName("??????: Room ????????? ????????? ????????? ???????????????.")
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
    @DisplayName("??????: Room ?????? ????????? ???????????????.")
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
    @DisplayName("??????: Room ????????? null ????????? ???????????????.")
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
    @DisplayName("??????: Room ????????? blank ????????? ???????????????.")
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
    @DisplayName("??????: Room ????????? empty ????????? ???????????????.")
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
    @DisplayName("??????: Room ???????????? ????????? ???????????????.")
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
    @DisplayName("??????: Room ???????????? null ????????? ???????????????.")
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
    @DisplayName("??????: Room ?????????????????? ????????? ???????????????.")
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
    @DisplayName("??????: Room ????????????????????? 0?????? ????????? ???????????????.")
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
    @DisplayName("??????: Room ????????????????????? ????????? ????????? ???????????????.")
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
    @DisplayName("??????: Room ?????? ?????? ????????? ???????????????.")
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
    @DisplayName("??????: Room ?????? ?????? 0?????? ????????? ???????????????.")
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
    @DisplayName("??????: Room ?????? ?????? ????????? ????????? ???????????????.")
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
    @DisplayName("??????: Room ????????? ????????? ???????????????.")
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
    @DisplayName("??????: ????????? Room ????????? ????????? null????????? roomImage??? ???????????? ????????? ???????????????.")
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
  class ????????????_??????_findDetailById {

    @Test
    @DisplayName("??????: ???????????? ?????? ??????")
    public void successFindDetailById() throws Exception {

      //when
      RoomDetailResponse roomDetailResponse = roomServiceForHost.findDetailById(roomId, hostId);

      //then
      assertThat(roomDetailResponse)
          .usingRecursiveComparison()
          .isEqualTo(defaultRoomResponse);
    }

    @Test
    @DisplayName("??????: roomId??? ??????????????? hostId??? ????????????")
    public void failWrongHostId() throws Exception {

      //when
      Long wrongHostId = -1L;

      //then
      assertThrows(UnAuthorizedAccessException.class,
          () -> roomServiceForHost.findDetailById(roomId, wrongHostId));
    }

    @Test
    @DisplayName("??????: hostId??? ??????????????? roomId ????????????")
    public void failWrongRoomId() throws Exception {

      //when
      Long wrongRoomId = -1L;

      //then
      assertThrows(NotFoundException.class,
          () -> roomServiceForHost.findDetailById(wrongRoomId, hostId));
    }

    @Test
    @DisplayName("??????: hostId, roomId ????????????")
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
  @DisplayName("host??? ????????? room list ??????")
  class findByHostId {

    @Test
    @DisplayName("??????: ??????????????? ??????")
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
    @DisplayName("??????: ???????????? ????????? Room??? ?????? ?????? ?????? = 0")
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
  class ??????_remove {

    @Test
    @DisplayName("??????: ?????? ??????")
    public void success() throws Exception {

      //when
      roomServiceForHost.remove(roomId, hostId);

      //then
      assertThat(roomRepository.findById(roomId).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("??????: roomId??? ???????????? ????????????")
    public void failWrongRoomId() throws Exception {

      //given
      Long wrongRoomId = null;

      //then
      // TODO: 2022/07/05 null??? ????????? ?????? InvalidDataAccessApiUsageException??? ??????????????? ?????? ?????? ?????? ????????? ????????? ?????? ???????????????. 
      assertThrows(NotFoundException.class, () -> roomServiceForHost.remove(wrongRoomId, hostId));
    }

    @Test
    @DisplayName("??????: hostId??? ?????? ??????")
    public void failWrongHostId() throws Exception {

      //then
      assertThrows(UnAuthorizedAccessException.class, () -> roomServiceForHost.remove(roomId, hostId1));
    }
  }
}

