package com.prgrms.airbnb.domain.room.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RoomInfoTest {

  @Nested
  class 생성 {

    @Test
    @DisplayName("성공: 생성 성공")
    public void Success() throws Exception {

      //given
      Integer maxGuest = 8;
      Integer roomCount = 4;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

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

      //given
      Integer maxGuest = 8;
      Integer roomCount = 4;
      Integer bathroomCount = 2;

      //when
      Integer bedCount = 0;

      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //then
      Assertions.assertThat(roomInfo.getMaxGuest()).isEqualTo(maxGuest);
      Assertions.assertThat(roomInfo.getRoomCount()).isEqualTo(roomCount);
      Assertions.assertThat(roomInfo.getBedCount()).isEqualTo(bedCount);
      Assertions.assertThat(roomInfo.getBathroomCount()).isEqualTo(bathroomCount);
    }

    @Test
    @DisplayName("실패: maxGuest = null -> 예외 발생")
    public void failMaxGuestIsNull() throws Exception {

      //given
      Integer roomCount = 4;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

      //when
      Integer maxGuest = null;

      //then
      assertThrows(RuntimeException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: maxGuest = 0 -> 예외 발생")
    public void failMaxGuestIsZero() throws Exception {

      //given
      Integer roomCount = 4;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

      //when
      Integer maxGuest = 0;

      //then
      assertThrows(RuntimeException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: maxGuest = 음수 -> 예외 발생")
    public void failMaxGuestIsNegative() throws Exception {

      //given
      Integer roomCount = 4;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

      //when
      Integer maxGuest = -1;

      //then
      assertThrows(RuntimeException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: RoomCount = null -> 예외 발생")
    public void failRoomCountIsNull() throws Exception {

      //given
      Integer maxGuest = 8;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

      //when
      Integer roomCount = null;

      //then
      assertThrows(RuntimeException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: RoomCount = 0 -> 예외 발생")
    public void failRoomCountIsZero() throws Exception {

      //given
      Integer maxGuest = 8;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

      //when
      Integer roomCount = 0;

      //then
      assertThrows(RuntimeException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: RoomCount = 음수 -> 예외 발생")
    public void failRoomCountIsNegative() throws Exception {

      //given
      Integer maxGuest = 8;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

      //when
      Integer roomCount = -1;

      //then
      assertThrows(RuntimeException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: BedCount = null -> 예외 발생")
    public void failBedCountIsNull() throws Exception {

      //given
      Integer maxGuest = 8;
      Integer roomCount = 4;
      Integer bathroomCount = 2;

      //when
      Integer bedCount = null;

      //then
      assertThrows(RuntimeException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: BedCount = 음수 -> 예외 발생")
    public void failBedCountIsNegative() throws Exception {

      //given
      Integer maxGuest = 8;
      Integer roomCount = 4;
      Integer bathroomCount = 2;

      //when
      Integer bedCount = -1;

      //then
      assertThrows(RuntimeException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: BathroomCount = null -> 예외 발생")
    public void failBathroomCountIsNull() throws Exception {

      //given
      Integer maxGuest = 8;
      Integer roomCount = 4;
      Integer bedCount = 4;

      //when
      Integer bathroomCount = null;

      //then
      assertThrows(RuntimeException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: BathroomCount = 0 -> 예외 발생")
    public void failBathroomCountIsZero() throws Exception {

      //given
      Integer maxGuest = 8;
      Integer roomCount = 4;
      Integer bedCount = 4;

      //when
      Integer bathroomCount = 0;

      //then
      assertThrows(RuntimeException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount));
    }

    @Test
    @DisplayName("실패: BathroomCount = 음수 -> 예외 발생")
    public void failBathroomCountIsNegative() throws Exception {

      //given
      Integer maxGuest = 8;
      Integer roomCount = 4;
      Integer bedCount = 4;

      //when
      Integer bathroomCount = -1;

      //then
      assertThrows(RuntimeException.class,
          () -> new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount));
    }
  }

  @Nested
  class 수정{

    @Test
    @DisplayName("성공: MaxGuest(최대수용인원) 변경 가능")
    public void SuccessChangeMaxGuest() throws Exception {

      //given
      Integer maxGuest = 6;
      Integer roomCount = 4;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //when
      Integer newMaxGuest = 3;
      roomInfo.setMaxGuest(newMaxGuest);

      //then
      Assertions.assertThat(roomInfo.getMaxGuest()).isEqualTo(newMaxGuest);
    }

    @Test
    @DisplayName("실패: MaxGuest(최대수용인원) 0으로 변경 불가")
    public void failChangeMaxGuestToZero() throws Exception {

      //given
      Integer maxGuest = 6;
      Integer roomCount = 4;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //when
      Integer newMaxGuest = 0;

      //then
      assertThrows(RuntimeException.class, () -> roomInfo.setMaxGuest(newMaxGuest));
    }

    @Test
    @DisplayName("실패: MaxGuest(최대수용인원) 음수로 변경 불가")
    public void failChangeMaxGuestToNegative() throws Exception {

      //given
      Integer maxGuest = 6;
      Integer roomCount = 4;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //when
      Integer newMaxGuest = -1;

      //then
      assertThrows(RuntimeException.class, () -> roomInfo.setMaxGuest(newMaxGuest));
    }

    @Test
    @DisplayName("성공: 침대 개수 변경 가능")
    public void SuccessChangeBedCount() throws Exception {

      //given
      Integer maxGuest = 8;
      Integer roomCount = 4;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //when
      Integer newBedCount = 3;
      roomInfo.setBedCount(newBedCount);

      //then
      Assertions.assertThat(roomInfo.getBedCount()).isEqualTo(newBedCount);
    }

    @Test
    @DisplayName("성공: 침대 개수 0으로 변경 가능")
    public void SuccessChangeBedCountToZero() throws Exception {

      //given
      Integer maxGuest = 8;
      Integer roomCount = 4;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //when
      Integer newBedCount = 0;
      roomInfo.setBedCount(newBedCount);

      //then
      Assertions.assertThat(roomInfo.getBedCount()).isEqualTo(newBedCount);
    }

    @Test
    @DisplayName("성공: 침대 개수 음수로 변경 가능")
    public void failChangeBedCountToNegative() throws Exception {

      //given
      Integer maxGuest = 8;
      Integer roomCount = 4;
      Integer bedCount = 4;
      Integer bathroomCount = 2;

      RoomInfo roomInfo = new RoomInfo(maxGuest, roomCount, bedCount, bathroomCount);

      //when
      Integer newBedCount = -1;
      roomInfo.setBedCount(newBedCount);

      //then
      Assertions.assertThat(roomInfo.getBedCount()).isEqualTo(newBedCount);
    }

  }


}