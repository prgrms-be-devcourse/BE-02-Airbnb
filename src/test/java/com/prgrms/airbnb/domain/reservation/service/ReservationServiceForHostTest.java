package com.prgrms.airbnb.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.reservation.dto.CreateReservationRequest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForGuest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForHost;
import com.prgrms.airbnb.domain.reservation.dto.ReservationSummaryResponse;
import com.prgrms.airbnb.domain.reservation.entity.ReservationStatus;
import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomInfo;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import com.prgrms.airbnb.domain.user.entity.Group;
import com.prgrms.airbnb.domain.user.entity.User;
import com.prgrms.airbnb.domain.user.repository.GroupRepository;
import com.prgrms.airbnb.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReservationServiceForHostTest {

  ReservationStatus reservationStatus;
  LocalDate startDate;
  LocalDate endDate;
  Integer term;
  Long userId1;
  Long userId2;
  Long userId3;
  Long roomId;

  Room room;
  Address address;
  Integer charge;
  String name;
  String description;
  RoomInfo roomInfo;
  RoomType roomType;
  List<RoomImage> images;
  RoomImage image1;
  RoomImage image2;

  User host;

  CreateReservationRequest createReservationRequest;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private ReservationServiceForGuest reservationServiceForGuest;

  @Autowired
  private ReservationServiceForHost reservationServiceForHost;

  @Autowired
  private ReservationRepository reservationRepository;

  @Autowired
  private GroupRepository groupRepository;

  @BeforeEach
  void setup() {

    //group 만들기
    Group group = groupRepository.findByName("USER_GROUP")
        .orElseThrow(() -> new IllegalStateException("Could not found group for USER_GROUP"));

    //host 만들기
    host = new User("범석",
        "provider",
        "providerId",
        "profileImage",
        group,
        new Email("qjatjr@gmail.com")
    );
    User saveHost = userRepository.save(host);
    //room 만들기
    address = new Address("address1", "address2");
    charge = 1000;
    name = "즐거운 범석네";
    description = "좋은 집";
    roomInfo = new RoomInfo(1, 1, 1, 1);
    roomType = RoomType.HOUSE;
    images = new ArrayList<>();
    image1 = new RoomImage("방");
    image2 = new RoomImage("거실");
    images.add(image1);
    images.add(image2);
    room = new Room(address,
        charge,
        name,
        description,
        roomInfo,
        roomType,
        images,
        saveHost.getId()
    );
    Room saveRoom = roomRepository.save(room);
    roomId = saveRoom.getId();
    //User 만들기
    User user1 = new User("짱구",
        "provider1",
        "providerId1",
        "profileImage1",
        group,
        new Email("Wkdrn@gmail.com")
    );
    User user2 = new User("철수",
        "provider2",
        "providerId2",
        "profileImage2",
        group,
        new Email("cjftn@gmail.com")
    );
    User user3 = new User("코난",
        "provider3",
        "providerId3",
        "profileImage3",
        group,
        new Email("zhsks@gmail.com")
    );
    userRepository.save(user1);
    userRepository.save(user2);
    userRepository.save(user3);
    userId1 = user1.getId();
    userId2 = user2.getId();
    userId3 = user3.getId();

    //예약 필드 준비
    reservationStatus = ReservationStatus.WAITED_OK;
    startDate = LocalDate.of(2023, 5, 3);
    endDate = LocalDate.of(2023, 5, 5);
    term = 3;

    createReservationRequest = CreateReservationRequest.builder()
        .reservationStatus(reservationStatus)
        .startDate(startDate)
        .endDate(endDate)
        .period(term)
        .oneDayCharge(charge)
        .userId(userId1)
        .roomId(roomId)
        .build();
  }

  @AfterEach
  void clear() {
    userRepository.deleteAll();
    roomRepository.deleteAll();
    reservationRepository.deleteAll();
  }



  @Nested
  @DisplayName("호스트 입장으로 자세한 예약 내용 보여주기")
  class FindDetailById{

    @Test
    @DisplayName("성공: 주어진 id값으로 예약 가져오기")
    void success() {
      ReservationDetailResponseForGuest save = reservationServiceForGuest
          .save(createReservationRequest);

      ReservationDetailResponseForHost detailById = reservationServiceForHost
          .findDetailById(save.getId(), host.getId());

      assertThat(save).usingRecursiveComparison().ignoringFields("host").isEqualTo(detailById);
      assertThat(detailById.getGuest().getId()).isEqualTo(userId1);
      assertThat(save.getHost().getId()).isEqualTo(host.getId());
    }

    @Test
    @DisplayName("실패: 존재하지않는 예약번호 입력")
    void failWrongReservationId() {
      reservationServiceForGuest.save(createReservationRequest);
      assertThatThrownBy(() -> reservationServiceForHost.findDetailById("abcd1234", userId1))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("Reservation 상태를 바꿀수 있는지 판단여부")
  class Approval{

    @Test
    @DisplayName("성공: 상태를 WAIT_OK 에서 ACCEPTED로 변경")
    void successChangeReservationStatus1() {
      ReservationDetailResponseForGuest save = reservationServiceForGuest
          .save(createReservationRequest);
      ReservationDetailResponseForHost approval = reservationServiceForHost
          .approval(save.getId(), host.getId(), ReservationStatus.ACCEPTED);
      assertThat(approval.getReservationStatus()).isEqualTo(ReservationStatus.ACCEPTED);
    }

    @Test
    @DisplayName("성공: 상태를 WAIT_OK 에서 ACCEPTED_BEFORE_CANCELLED 변경")
    void successChangeReservationStatus2() {
      ReservationDetailResponseForGuest save = reservationServiceForGuest
          .save(createReservationRequest);
      ReservationDetailResponseForHost approval = reservationServiceForHost
          .approval(save.getId(), host.getId(), ReservationStatus.ACCEPTED_BEFORE_CANCELLED);
      assertThat(approval.getReservationStatus()).isEqualTo(ReservationStatus.ACCEPTED_BEFORE_CANCELLED);
    }

    @Test
    @DisplayName("성공: 상태를 ACCEPTED 에서 ACCEPTED_AFTER_CANCELLED로 변경")
    void successChangeReservationStatus3() {
      ReservationDetailResponseForGuest save = reservationServiceForGuest
          .save(createReservationRequest);
      reservationServiceForHost
          .approval(save.getId(), host.getId(), ReservationStatus.ACCEPTED);
      ReservationDetailResponseForHost approval = reservationServiceForHost
          .approval(save.getId(), host.getId(), ReservationStatus.ACCEPTED_AFTER_CANCELLED);
      assertThat(approval.getReservationStatus()).isEqualTo(ReservationStatus.ACCEPTED_AFTER_CANCELLED);
    }

    @Test
    @DisplayName("성공: 상태를 ACCEPTED 에서 WAIT_REVIEW로 변경")
    void successChangeReservationStatus4() {
      ReservationDetailResponseForGuest save = reservationServiceForGuest
          .save(createReservationRequest);
      reservationServiceForHost
          .approval(save.getId(), host.getId(), ReservationStatus.ACCEPTED);
      ReservationDetailResponseForHost approval = reservationServiceForHost
          .approval(save.getId(), host.getId(), ReservationStatus.WAIT_REVIEW);
      assertThat(approval.getReservationStatus()).isEqualTo(ReservationStatus.WAIT_REVIEW);
    }

    @Test
    @DisplayName("성공: 상태를 WAIT_REVIEW 에서 COMPLETE로 변경")
    void successChangeReservationStatus5() {
      ReservationDetailResponseForGuest save = reservationServiceForGuest
          .save(createReservationRequest);
      reservationServiceForHost
          .approval(save.getId(), host.getId(), ReservationStatus.ACCEPTED);
      reservationServiceForHost
          .approval(save.getId(), host.getId(), ReservationStatus.WAIT_REVIEW);
      ReservationDetailResponseForHost approval = reservationServiceForHost
          .approval(save.getId(), host.getId(), ReservationStatus.COMPLETE);
      assertThat(approval.getReservationStatus()).isEqualTo(ReservationStatus.COMPLETE);
    }

    @Test
    @DisplayName("실패: 상태를 WAIT_OK 에서 WAIT_REVIEW, COMPLETE로 변경 불가")
    void failChangeWrongReservationStatus() {
      ReservationDetailResponseForGuest save = reservationServiceForGuest
          .save(createReservationRequest);
      assertThatThrownBy(() ->
          reservationServiceForHost
              .approval(save.getId(), host.getId(), ReservationStatus.WAIT_REVIEW))
          .isInstanceOf(IllegalArgumentException.class);
      assertThatThrownBy(() ->
          reservationServiceForHost
              .approval(save.getId(), host.getId(), ReservationStatus.COMPLETE))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("호스트 id로 예약 리스트 가져오기")
  class FindByHostId{

    @Test
    @DisplayName("성공: 예약 리스트 가져오기 성공")
    void success() {

      //예약 추가
      ReservationDetailResponseForGuest save1 = reservationServiceForGuest
          .save(createReservationRequest);

      createReservationRequest = CreateReservationRequest.builder()
          .reservationStatus(reservationStatus)
          .startDate(LocalDate.of(2023, 1, 3))
          .endDate(LocalDate.of(2023, 1, 7))
          .period(term)
          .oneDayCharge(charge)
          .userId(userId2)
          .roomId(roomId)
          .build();
      ReservationDetailResponseForGuest save2 = reservationServiceForGuest
          .save(createReservationRequest);

      createReservationRequest = CreateReservationRequest.builder()
          .reservationStatus(reservationStatus)
          .startDate(LocalDate.of(2023, 4, 3))
          .endDate(LocalDate.of(2023, 4, 7))
          .period(term)
          .oneDayCharge(charge)
          .userId(userId2)
          .roomId(roomId)
          .build();
      ReservationDetailResponseForGuest save3 = reservationServiceForGuest
          .save(createReservationRequest);

      PageRequest pageable = PageRequest.of(0, 5);
      Slice<ReservationSummaryResponse> reservationList = reservationServiceForHost
          .findByHostId(host.getId(), pageable);

      assertThat(reservationList).hasSize(3);
      assertThat(reservationList.map(ReservationSummaryResponse::getId))
          .containsOnly(save1.getId(), save2.getId(), save3.getId());

    }

    @Test
    @DisplayName("성공: 아무 숙소가 없는 host 리스트 가져오기 성공")
    void successEmptyReservationList() {

      PageRequest pageable = PageRequest.of(0, 5);
      Slice<ReservationSummaryResponse> reservationList = reservationServiceForHost
          .findByHostId(host.getId(), pageable);

      assertThat(reservationList).hasSize(0);

    }
  }
}