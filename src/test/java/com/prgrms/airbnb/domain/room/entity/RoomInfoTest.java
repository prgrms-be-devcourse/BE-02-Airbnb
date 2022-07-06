package com.prgrms.airbnb.domain.room.entity;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.prgrms.airbnb.domain.common.exception.InvalidParamException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RoomInfoTest {

  Integer maxGuest;
  Integer roomCount;
  Integer bedCount;
  Integer bathroomCount;

  @BeforeEach
  void setup() {
    maxGuest = 8;
    roomCount = 4;
    bedCount = 4;
    bathroomCount = 2;
  }

  @Nested
  class 생성 {

    @Test
    @DisplayName("성공: 생성 성공")
    public void Success() throws Exception {

      //when
      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //then
      Assertions.assertThat(roomInfo.getMaxGuest()).isEqualTo(maxGuest);
      Assertions.assertThat(roomInfo.getRoomCount()).isEqualTo(roomCount);
      Assertions.assertThat(roomInfo.getBedCount()).isEqualTo(bedCount);
      Assertions.assertThat(roomInfo.getBathroomCount()).isEqualTo(bathroomCount);
    }

    @Test
    @DisplayName("성공: 침대 개수가 0이어도 생성 성공")
    public void SuccessBedCountIsZero() throws Exception {

      //when
      Integer testBedCount = 0;

      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, testBedCount, bathroomCount);

      //then
      Assertions.assertThat(roomInfo.getBedCount()).isEqualTo(testBedCount);
    }

    @Test
    @DisplayName("실패: maxGuest = null -> 예외 발생")
    public void failMaxGuestIsNull() throws Exception {

      //when
      Integer testMaxGuest = null;

      //then
      assertThrows(InvalidParamException.class,
          () -> new RoomInfo(testMaxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: maxGuest = 0 -> 예외 발생")
    public void failMaxGuestIsZero() throws Exception {

      //when
      Integer testMaxGuest = 0;

      //then
      assertThrows(InvalidParamException.class,
          () -> new RoomInfo(testMaxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: maxGuest = 음수 -> 예외 발생")
    public void failMaxGuestIsNegative() throws Exception {

      //when
      Integer testMaxGuest = -1;

      //then
      assertThrows(InvalidParamException.class,
          () -> new RoomInfo(testMaxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: RoomCount = null -> 예외 발생")
    public void failRoomCountIsNull() throws Exception {

      //when
      Integer testRoomCount = null;

      //then
      assertThrows(InvalidParamException.class,
          () -> new RoomInfo(maxGuest, testRoomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: RoomCount = 0 -> 예외 발생")
    public void failRoomCountIsZero() throws Exception {

      //when
      Integer testRoomCount = 0;

      //then
      assertThrows(InvalidParamException.class,
          () -> new RoomInfo(maxGuest, testRoomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: RoomCount = 음수 -> 예외 발생")
    public void failRoomCountIsNegative() throws Exception {

      //when
      Integer testRoomCount = -1;

      //then
      assertThrows(InvalidParamException.class,
          () -> new RoomInfo(maxGuest, testRoomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: BedCount = null -> 예외 발생")
    public void failBedCountIsNull() throws Exception {

      //when
      Integer testBedCount = null;

      //then
      assertThrows(InvalidParamException.class,
          () -> new RoomInfo(maxGuest, roomCount, testBedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: BedCount = 음수 -> 예외 발생")
    public void failBedCountIsNegative() throws Exception {

      //when
      Integer testBedCount = -1;

      //then
      assertThrows(InvalidParamException.class,
          () -> new RoomInfo(maxGuest, roomCount, testBedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: BathroomCount = null -> 예외 발생")
    public void failBathroomCountIsNull() throws Exception {

      //when
      Integer testBathroomCount = null;

      //then
      assertThrows(InvalidParamException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, testBathroomCount));
    }

    @Test
    @DisplayName("실패: BathroomCount = 0 -> 예외 발생")
    public void failBathroomCountIsZero() throws Exception {

      //when
      Integer testBathroomCount = 0;

      //then
      assertThrows(InvalidParamException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, testBathroomCount));
    }

    @Test
    @DisplayName("실패: BathroomCount = 음수 -> 예외 발생")
    public void failBathroomCountIsNegative() throws Exception {

      //when
      Integer testBathroomCount = -1;

      //then
      assertThrows(InvalidParamException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, testBathroomCount));
    }
  }

  @Nested
  class 수정{

    @Test
    @DisplayName("성공: MaxGuest(최대수용인원) 변경 가능")
    public void SuccessChangeMaxGuest() throws Exception {

      //given
      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //when
      Integer newMaxGuest = 3;
      roomInfo.changeMaxGuest(newMaxGuest);

      //then
      Assertions.assertThat(roomInfo.getMaxGuest()).isEqualTo(newMaxGuest);
    }

    @Test
    @DisplayName("실패: MaxGuest(최대수용인원) 0으로 변경 불가")
    public void failChangeMaxGuestToZero() throws Exception {

      //given
      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //when
      Integer newMaxGuest = 0;

      //then
      assertThrows(InvalidParamException.class, () -> roomInfo.changeMaxGuest(newMaxGuest));
    }

    @Test
    @DisplayName("실패: MaxGuest(최대수용인원) 음수로 변경 불가")
    public void failChangeMaxGuestToNegative() throws Exception {

      //given
      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //when
      Integer newMaxGuest = -1;

      //then
      assertThrows(InvalidParamException.class, () -> roomInfo.changeMaxGuest(newMaxGuest));
    }

    @Test
    @DisplayName("성공: 침대 개수 변경 가능")
    public void SuccessChangeBedCount() throws Exception {

      //given
      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //when
      Integer newBedCount = 3;
      roomInfo.changeBedCount(newBedCount);

      //then
      Assertions.assertThat(roomInfo.getBedCount()).isEqualTo(newBedCount);
    }

    @Test
    @DisplayName("성공: 침대 개수 0으로 변경 가능")
    public void SuccessChangeBedCountToZero() throws Exception {

      //given
      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //when
      Integer newBedCount = 0;
      roomInfo.changeBedCount(newBedCount);

      //then
      Assertions.assertThat(roomInfo.getBedCount()).isEqualTo(newBedCount);
    }

    @Test
    @DisplayName("성공: 침대 개수 음수로 변경 가능")
    public void failChangeBedCountToNegative() throws Exception {

      //given
      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //when
      Integer newBedCount = -1;

      //then
      assertThrows(InvalidParamException.class, () -> roomInfo.changeBedCount(newBedCount));
    }
  }
}