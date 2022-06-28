package com.prgrms.airbnb.domain.room.service;

import static com.prgrms.airbnb.domain.room.service.RoomServiceForHostTest.getMockMultipartFile;
import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.room.dto.CreateRoomRequest;
import com.prgrms.airbnb.domain.room.dto.RoomDetailResponse;
import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.dto.SearchRoomRequest;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
class RoomServiceForGuestTest {

  @Autowired
  RoomServiceForGuest roomServiceForGuest;

  @Autowired
  GroupRepository groupRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoomServiceForHost roomServiceForHost;

  @Autowired
  RoomRepository roomRepository;

  @Autowired
  RoomImageRepository roomImageRepository;

  Long hostId1;
  Long hostId2;
  Long hostId3;

  Address defaultAddress;
  Integer defaultCharge;
  String defaultRoomName;
  String defaultRoomDescription;
  RoomInfo defaultRoomInfo;
  RoomType defaultRoomType;
  List<MultipartFile> defaultMultipartFiles;

  MockMultipartFile mockMultipartFile1;
  MockMultipartFile mockMultipartFile2;

  Long roomId;

  @BeforeEach
  void setupForFindByHostId() throws IOException {
    defaultAddress = new Address("default address1", "default address2");
    defaultCharge = 1000;
    defaultRoomName = "default roomName";
    defaultRoomDescription = "default roomDescription";
    defaultRoomInfo = new RoomInfo(1, 1, 1, 1);
    defaultRoomType = RoomType.APARTMENT;
    defaultMultipartFiles = new ArrayList<>();

    mockMultipartFile1 = getMockMultipartFile("testCustomerUpload1", "png",
        "/Users/hyunggeunpark/Desktop/uploadTest/testCustomerUpload1.png");
    mockMultipartFile2 = getMockMultipartFile("testCustomerUpload2", "png",
        "/Users/hyunggeunpark/Desktop/uploadTest/testCustomerUpload2.png");

    defaultMultipartFiles.add(mockMultipartFile1);
    defaultMultipartFiles.add(mockMultipartFile2);

    Group group = groupRepository.findByName("USER_GROUP")
        .orElseThrow(() -> new IllegalStateException("Could not found group for USER_GROUP"));

    User user1 = userRepository.save(new User("user1", "provider1", "providerId1",
        "profileImage1", group, new Email("aaa1@gmail.com")));

    User user2 = userRepository.save(new User("user2", "provider2", "providerId2",
        "profileImage2", group, new Email("aaa2@gmail.com")));

    User user3 = userRepository.save(new User("user3", "provider3", "providerId3",
        "profileImage3", group, new Email("aaa3@gmail.com")));

    hostId1 = user1.getId();
    hostId2 = user2.getId();
    hostId3 = user3.getId();

    RoomType[] roomTypes = {RoomType.APARTMENT, RoomType.DORMITORY, RoomType.HOUSE};

    for (int i = 1; i < 11; i++) {
      Address address = new Address("address1" + i, "address2" + i);
      CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
          .address(address)
          .charge(defaultCharge * i)
          .name("userId1의 Room")
          .description(defaultRoomDescription)
          .roomInfo(new RoomInfo(i, i, i, i))
          .roomType(roomTypes[i % roomTypes.length])
          .build();
      roomServiceForHost.save(createRoomRequest, defaultMultipartFiles, hostId1);
    }

    for (int i = 11; i < 21; i++) {
      Address address = new Address("address1" + i, "address2" + i);
      CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
          .address(address)
          .charge(defaultCharge * i)
          .name("userId2의 Room")
          .description(defaultRoomDescription)
          .roomInfo(new RoomInfo(i, i, i, i))
          .roomType(roomTypes[i % roomTypes.length])
          .build();
      roomServiceForHost.save(createRoomRequest, defaultMultipartFiles, hostId2);
    }

    for (int i = 21; i < 31; i++) {
      Address address = new Address("address1" + i, "address2" + i);
      CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
          .address(address)
          .charge(defaultCharge * i)
          .name("userId3의 Room")
          .description(defaultRoomDescription)
          .roomInfo(new RoomInfo(i, i, i, i))
          .roomType(roomTypes[i % roomTypes.length])
          .build();
      roomServiceForHost.save(createRoomRequest, defaultMultipartFiles, hostId3);
    }

    CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
        .address(defaultAddress)
        .charge(defaultCharge)
        .name(defaultRoomName)
        .description(defaultRoomDescription)
        .roomInfo(defaultRoomInfo)
        .roomType(defaultRoomType)
        .build();

    roomId = roomServiceForHost.save(createRoomRequest, defaultMultipartFiles, hostId1).getId();
  }

  @AfterEach
  void clear() {
    roomRepository.deleteAll();
    userRepository.deleteAll();
    roomImageRepository.deleteAll();
  }

  @Nested
  class 상세조회_findDetailById {

    @Test
    @DisplayName("성공: Room 상세정보 조회에 성공합니다.")
    public void success() throws Exception {

      //given
      Room room = roomRepository.findById(roomId)
          .orElseThrow(RuntimeException::new);
      //when
      RoomDetailResponse roomDetailResponse = roomServiceForGuest.findDetailById(roomId);

      //then
      assertThat(roomDetailResponse)
          .usingRecursiveComparison()
          .isEqualTo(room);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 RoomId로 조회하는 경우 예외 발생")
    public void failFindNonExistId() throws Exception {

      //given
      Long nonExistId = -1L;

      //then
      Assertions.assertThrows(RuntimeException.class,
          () -> roomServiceForGuest.findDetailById(nonExistId));
    }

    @Test
    @DisplayName("실패: RoomId = null로 조회하는 경우 예외 발생")
    public void failRoomIdIsNull() throws Exception {

      //given
      Long nonExistId = null;

      //then
      Assertions.assertThrows(RuntimeException.class,
          () -> roomServiceForGuest.findDetailById(nonExistId));
    }
  }

  @Nested
  class 검색조회_findAll {

    @Test
    @DisplayName("성공: 조건 없을 시 전체 조회 -> 중복 없음")
    public void success() throws Exception {

      //given
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      assertThat(sliceOfRoomSummary.getNumberOfElements())
          .isEqualTo(roomRepository.findAll().size());
    }

    @Test
    @DisplayName("성공: 1개 키워드로 검색할 수 있음")
    public void successKeyword() throws Exception {

      //given
      String keyword = "Id3";
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .keyword(keyword)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      assertThat(sliceOfRoomSummary.getNumberOfElements()).isEqualTo(10);
      for (RoomSummaryResponse roomSummaryResponse : sliceOfRoomSummary) {
        assertThat(roomSummaryResponse.getName()).containsIgnoringCase(keyword);
      }
    }

    @Test
    @DisplayName("성공: 2개 이상 키워드로 검색할 수 있음")
    public void successKeywords() throws Exception {

      //given
      String keyword1 = "Room";
      String keyword2 = "3";
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .keyword(keyword1 + " " + keyword2)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      assertThat(sliceOfRoomSummary.getNumberOfElements()).isEqualTo(10);
      for (RoomSummaryResponse roomSummaryResponse : sliceOfRoomSummary) {
        assertThat(roomSummaryResponse.getName()).containsIgnoringCase(keyword1);
        assertThat(roomSummaryResponse.getName()).containsIgnoringCase(keyword2);
      }
    }

    @Test
    @DisplayName("성공: 대소문자 구분 없이 키워드 검색 가능")
    public void successKeywordIgnoreCase() throws Exception {

      //given
      String keyword = "id3";
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .keyword(keyword)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      assertThat(sliceOfRoomSummary.getNumberOfElements()).isEqualTo(10);
      for (RoomSummaryResponse roomSummaryResponse : sliceOfRoomSummary) {
        assertThat(roomSummaryResponse.getName()).containsIgnoringCase(keyword);
      }
    }

    @Test
    @DisplayName("성공: 검색어 없을 시 전체 조회")
    public void successKeywordIsNull() throws Exception {

      //given
      String keyword = null;
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .keyword(keyword)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      assertThat(sliceOfRoomSummary.getNumberOfElements())
          .isEqualTo(roomRepository.findAll().size());
    }

    @Test
    @DisplayName("성공: 최대 가격으로 검색 가능")
    public void successMaxCharge() throws Exception {

      //given
      int maxCharge = 1000;
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .maxCharge(maxCharge)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      for (RoomSummaryResponse roomSummaryResponse : sliceOfRoomSummary) {
        assertThat(roomSummaryResponse.getCharge()).isLessThanOrEqualTo(maxCharge);
      }
    }

    @Test
    @DisplayName("성공: 최소 가격으로 검색 가능")
    public void successMinCharge() throws Exception {

      //given
      int minCharge = 20000;
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .minCharge(minCharge)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      for (RoomSummaryResponse roomSummaryResponse : sliceOfRoomSummary) {
        assertThat(roomSummaryResponse.getCharge()).isGreaterThanOrEqualTo(minCharge);
      }
    }

    @Test
    @DisplayName("성공: 최소 가격 + 최대 가격으로 검색 가능")
    public void successMinAndMaxCharge() throws Exception {

      //given
      int minCharge = 20000;
      int maxCharge = 40000;
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .maxCharge(maxCharge)
          .minCharge(minCharge)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      for (RoomSummaryResponse roomSummaryResponse : sliceOfRoomSummary) {
        assertThat(roomSummaryResponse.getCharge()).isGreaterThanOrEqualTo(minCharge);
        assertThat(roomSummaryResponse.getCharge()).isLessThanOrEqualTo(maxCharge);
      }
    }

    // TODO: 2022/06/27 최소 가격, 최대 가격이 반전되었을때 이를 방지하는 로직을 어디에 둘 것 인가?
    @Test
    @DisplayName("성공: 최소 가격이 최대 가격보다 클 때  -> 조회 개수 = 0")
    public void successMinAndMaxCharge2() throws Exception {

      //given
      int minCharge = 40000;
      int maxCharge = 20000;
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .maxCharge(maxCharge)
          .minCharge(minCharge)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      assertThat(sliceOfRoomSummary.getNumberOfElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("성공: roomType 으로 조회")
    public void successRoomType() throws Exception {

      //given
      RoomType roomType = RoomType.APARTMENT;
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .roomType(roomType)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      for (RoomSummaryResponse roomSummaryResponse : sliceOfRoomSummary) {
        assertThat(roomSummaryResponse.getRoomType()).isEqualTo(roomType);
      }
    }

    @Test
    @DisplayName("성공: 최대 인원수로 조회")
    public void successMaxGuest() throws Exception {

      //given
      int maxGuest = 1;
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .maxGuest(maxGuest)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      assertThat(sliceOfRoomSummary.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("성공: 방 개수로 조회")
    public void successRoomCount() throws Exception {

      //given
      int roomCount = 21;
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .roomCount(roomCount)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      assertThat(sliceOfRoomSummary.getNumberOfElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("성공: 최소 침대 개수로 조회")
    public void successBedCount() throws Exception {

      //given
      int bedCount = 21;
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .bedCount(bedCount)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      assertThat(sliceOfRoomSummary.getNumberOfElements()).isEqualTo(10);
    }
    @Test
    @DisplayName("성공: 복합 조건")
    public void successComplex() throws Exception {

      //given
      int minCharge = 25000;
      int maxCharge = 30000;
      int bedCount = 26;
      int maxGuest = 29;
      String keyword = "3";
      SearchRoomRequest searchRoomRequest = SearchRoomRequest.builder()
          .keyword(keyword)
          .minCharge(minCharge)
          .maxCharge(maxCharge)
          .bedCount(bedCount)
          .maxGuest(maxGuest)
          .build();

      PageRequest pageable = PageRequest.of(0, 100);

      //when
      Slice<RoomSummaryResponse> sliceOfRoomSummary
          = roomServiceForGuest.findAll(searchRoomRequest, pageable);

      //then
      assertThat(sliceOfRoomSummary.getNumberOfElements()).isEqualTo(4);
      for (RoomSummaryResponse roomSummaryResponse : sliceOfRoomSummary) {
        assertThat(roomSummaryResponse.getCharge()).isGreaterThanOrEqualTo(minCharge);
        assertThat(roomSummaryResponse.getCharge()).isLessThanOrEqualTo(maxCharge);
        assertThat(roomSummaryResponse.getName()).containsIgnoringCase(keyword);
      }
    }
  }
}
