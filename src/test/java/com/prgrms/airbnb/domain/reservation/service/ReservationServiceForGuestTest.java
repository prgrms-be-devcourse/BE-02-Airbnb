package com.prgrms.airbnb.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.common.entity.Email;
import com.prgrms.airbnb.domain.common.exception.BadRequestException;
import com.prgrms.airbnb.domain.common.exception.InvalidParamException;
import com.prgrms.airbnb.domain.common.exception.NotFoundException;
import com.prgrms.airbnb.domain.reservation.dto.CreateReservationRequest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationDetailResponseForGuest;
import com.prgrms.airbnb.domain.reservation.dto.ReservationSummaryResponse;
import com.prgrms.airbnb.domain.reservation.entity.Reservation;
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

  CreateReservationRequest createReservationRequest;

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

    //group ?????????
    Group group = groupRepository.findByName("USER_GROUP")
        .orElseThrow(() -> new IllegalStateException("Could not found group for USER_GROUP"));

    //host ?????????
    host = new User("??????",
        "provider",
        "providerId",
        "profileImage",
        group,
        new Email("qjatjr@gmail.com")
    );
    User saveHost = userRepository.save(host);

    //room ?????????
    address = new Address("address1", "address2");
    charge = 1000;
    name = "????????? ?????????";
    description = "?????? ???";
    roomInfo = new RoomInfo(1, 1, 1, 1);
    roomType = RoomType.HOUSE;
    images = new ArrayList<>();
    image1 = new RoomImage("???");
    image2 = new RoomImage("??????");
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

    //User ?????????
    User user1 = new User("??????",
        "provider1",
        "providerId1",
        "profileImage1",
        group,
        new Email("Wkdrn@gmail.com")
    );
    User user2 = new User("??????",
        "provider2",
        "providerId2",
        "profileImage2",
        group,
        new Email("cjftn@gmail.com")
    );
    User user3 = new User("??????",
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

    //?????? ?????? ??????
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
  @DisplayName("?????? ?????????")
  class Save {

    @Test
    @DisplayName("??????: reservation ?????? ??????")
    void success() {
      Room getRoom = roomRepository.findById(createReservationRequest.getRoomId())
          .orElseThrow(() -> {
            throw new NotFoundException(this.getClass().getName());
          });
      ReservationDetailResponseForGuest responseForGuest = reservationServiceForGuest
          .save(createReservationRequest);

      assertThat(responseForGuest)
          .usingRecursiveComparison()
          .ignoringFields("host", "roomResponseForReservation", "totalPrice", "id")
          .isEqualTo(createReservationRequest);
      assertThat(responseForGuest.getTotalPrice()).isEqualTo(
          (createReservationRequest.getOneDayCharge() * createReservationRequest.getPeriod()));
      assertThat(responseForGuest.getRoomResponseForReservation().getId())
          .isEqualTo(createReservationRequest.getRoomId());
      assertThat(responseForGuest.getHost().getId()).isEqualTo(getRoom.getUserId());
    }

    @Test
    @DisplayName("??????: ?????? ????????? ????????? ??????????????? ???")
    void failPassedByDate() {
      createReservationRequest = CreateReservationRequest.builder()
          .reservationStatus(reservationStatus)
          .startDate(LocalDate.of(2020, 4, 4))
          .endDate(LocalDate.of(2020, 4, 7))
          .period(term)
          .oneDayCharge(charge)
          .userId(userId1)
          .roomId(roomId)
          .build();

      assertThatThrownBy(() -> reservationServiceForGuest.save(createReservationRequest))
          .isInstanceOf(InvalidParamException.class);
    }

    @Test
    @DisplayName("??????: ?????? ????????? ??????????????? ????????? ?????????")
    void failExistReservationByDate() {
      ReservationDetailResponseForGuest responseForGuest = reservationServiceForGuest
          .save(createReservationRequest);

      assertThatThrownBy(() -> reservationServiceForGuest.save(createReservationRequest))
          .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("??????: ???????????? ?????? ????????? ?????? ???")
    void failWrongRoomId() {
      createReservationRequest = CreateReservationRequest.builder()
          .reservationStatus(reservationStatus)
          .startDate(startDate)
          .endDate(endDate)
          .period(term)
          .oneDayCharge(charge)
          .userId(userId1)
          .roomId(Long.MAX_VALUE)
          .build();
      assertThatThrownBy(() -> reservationServiceForGuest.save(createReservationRequest))
          .isInstanceOf(NotFoundException.class);
    }
  }

  @Nested
  @DisplayName("????????? ???????????? ????????? ?????? ?????? ????????????")
  class FindDetailById {

    @Test
    @DisplayName("??????: ????????? id????????? ?????? ????????????")
    void success() {
      ReservationDetailResponseForGuest save = reservationServiceForGuest
          .save(createReservationRequest);

      ReservationDetailResponseForGuest detailById = reservationServiceForGuest
          .findDetailById(save.getId(), userId1);

      assertThat(save).usingRecursiveComparison().isEqualTo(detailById);
    }

    @Test
    @DisplayName("??????: ?????????????????? ???????????? ??????")
    void failWrongReservationId() {
      reservationServiceForGuest.save(createReservationRequest);
      assertThatThrownBy(() -> reservationServiceForGuest.findDetailById("abcd1234", userId1))
          .isInstanceOf(NotFoundException.class);
    }
  }

  @Nested
  @DisplayName("?????? ???????????? ????????????")
  class Cancel {

    @Test
    @DisplayName("??????: ?????? ??????")
    void success() {

      ReservationDetailResponseForGuest save = reservationServiceForGuest
          .save(createReservationRequest);

      reservationServiceForGuest.cancel(userId1, save.getId());
      ReservationDetailResponseForGuest detailById = reservationServiceForGuest
          .findDetailById(save.getId(), userId1);
      assertThat(detailById.getReservationStatus()).isEqualTo(ReservationStatus.GUEST_CANCELLED);
    }

    @Test
    @DisplayName("??????: ?????????????????? ???????????? ?????? ???")
    void failWrongReservation() {
      ReservationDetailResponseForGuest save = reservationServiceForGuest
          .save(createReservationRequest);

      assertThatThrownBy(() -> reservationServiceForGuest.cancel(userId1, "abcd1234"))
          .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("??????: ????????? ??? ?????? ??????")
    void failCancelByReservationStatus() {
      ReservationDetailResponseForGuest save = reservationServiceForGuest
          .save(createReservationRequest);
      Reservation reservation = reservationRepository.findById(save.getId()).orElseThrow(() -> {
        throw new NotFoundException(this.getClass().getName());
      });

      //WAIT_OK ??? ACCEPTED ??????????????? ?????? ??????
      reservation.changeStatus(ReservationStatus.ACCEPTED);
      reservation.changeStatus(ReservationStatus.WAIT_REVIEW);

      assertThatThrownBy(() -> reservationServiceForGuest.cancel(userId1, reservation.getId()))
          .isInstanceOf(BadRequestException.class);
    }
  }

  @Nested
  @DisplayName("?????? id??? ?????? ????????? ????????????")
  class FindByUserId {

    @Test
    @DisplayName("??????: ?????? ????????? ???????????? ??????")
    void success() {

      //?????? ??????
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
      Slice<ReservationSummaryResponse> list = reservationServiceForGuest
          .findByUserId(userId2, pageable);

      assertThat(list).hasSize(2);
      assertThat(list.map(ReservationSummaryResponse::getId))
          .containsOnly(save2.getId(), save3.getId());
    }

    @Test
    @DisplayName("??????: ?????? ????????? ????????? ?????? ??????")
    void failNotExistReservationByUserId() {

      //?????? ??????
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
      Slice<ReservationSummaryResponse> list = reservationServiceForGuest
          .findByUserId(userId3, pageable);

      assertThat(list).hasSize(0);
      assertThat(list.map(ReservationSummaryResponse::getId)).isEmpty();
    }
  }
}