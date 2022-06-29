package com.prgrms.airbnb.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.reservation.dto.CreateReservationRequest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForGuest;
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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
class ReservationServiceForGuestTest {

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

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private ReservationServiceForGuest reservationServiceForGuest;

  @Autowired
  private ReservationRepository reservationRepository;

  @Autowired
  private GroupRepository groupRepository;

  @BeforeEach
  void setup() {

    //group 만들기
    Group group = groupRepository.findByName("USER_GROUP")
        .orElseThrow(() -> new IllegalStateException("Could not found group for USER_GROUP"));

    log.info("group 완료");
    //host 만들기
    host = new User("범석",
        "provider",
        "providerId",
        "profileImage",
        group,
        new Email("qjatjr@gmail.com")
    );
    User saveHost = userRepository.save(host);
    log.info("host 완료");
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
    log.info("room 완료");
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
    log.info("users 완료");
    //예약 필드 준비
    reservationStatus = ReservationStatus.WAITED_OK;
    log.info("status 완료");
  }

  @AfterEach
  void clear() {
    userRepository.deleteAll();
    roomRepository.deleteAll();
    reservationRepository.deleteAll();
  }

  @Nested
  @DisplayName("저장 테스트")
  class Save {

    @Test
    @DisplayName("성공: reservation 생성 성공")
    void success() {

      startDate = LocalDate.of(2022, 5, 3);
      endDate = LocalDate.of(2022, 5, 5);
      term = 3;
      CreateReservationRequest createReservationRequest = CreateReservationRequest.builder()
          .reservationStatus(reservationStatus)
          .startDate(startDate)
          .endDate(endDate)
          .period(term)
          .oneDayCharge(charge)
          .userId(userId1)
          .roomId(roomId)
          .build();

      Room getRoom = roomRepository.findById(createReservationRequest.getRoomId()).orElseThrow(IllegalArgumentException::new);
      ReservationDetailResponseForGuest responseForGuest = reservationServiceForGuest.save(createReservationRequest);

      assertThat(responseForGuest)
          .usingRecursiveComparison()
          .ignoringFields("host","roomResponseForReservation","totalPrice","id")
          .isEqualTo(createReservationRequest);
      assertThat(responseForGuest.getTotalPrice()).isEqualTo((createReservationRequest.getOneDayCharge() * createReservationRequest.getPeriod()));
      assertThat(responseForGuest.getRoomResponseForReservation().getId()).isEqualTo(createReservationRequest.getRoomId());
      assertThat(responseForGuest.getHost().getId()).isEqualTo(getRoom.getUserId());
    }

    @Test
    @DisplayName("실패: 이미 숙소에 같은날짜의 예약이 존재함")
    void failExistReservation(){
      
      //먼저 예약
      startDate = LocalDate.of(2022, 5, 3);
      endDate = LocalDate.of(2022, 5, 5);
      term = 3;
      CreateReservationRequest createReservationRequest = CreateReservationRequest.builder()
          .reservationStatus(reservationStatus)
          .startDate(startDate)
          .endDate(endDate)
          .period(term)
          .oneDayCharge(charge)
          .userId(userId1)
          .roomId(roomId)
          .build();
      ReservationDetailResponseForGuest responseForGuest = reservationServiceForGuest.save(createReservationRequest);

      assertThatThrownBy(()-> reservationServiceForGuest.save(createReservationRequest))
          .isInstanceOf(IllegalArgumentException.class);
    }


  }
}