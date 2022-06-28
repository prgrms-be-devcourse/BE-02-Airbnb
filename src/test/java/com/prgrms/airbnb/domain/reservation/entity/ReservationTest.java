package com.prgrms.airbnb.domain.reservation.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import java.time.LocalDate;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ReservationTest {

  String id;

  LocalDate startDate;

  LocalDate endDate;

  Integer term;

  Integer oneDayCharge;

  Long userId;

  Long roomId;

  @Autowired
  ReservationRepository reservationRepository;

  @BeforeEach
  void setup() {
    id = reservationRepository.createReservationId();
    startDate = LocalDate.of(2022, 6, 9);
    endDate = LocalDate.of(2022, 6, 12);
    term = 3;
    oneDayCharge = 1000;
    userId = 10L;
    roomId = 6L;
  }

  @Nested
  @DisplayName("생성 테스트")
  class Create {

    @Test
    @DisplayName("성공: 모든 조건 만족")
    public void success1() {
      Reservation reservation = new Reservation(
          id,
          ReservationStatus.WAITED_OK,
          startDate,
          endDate,
          term,
          oneDayCharge,
          userId,
          roomId
      );

      Reservation reservationDto = new Reservation(
          id,
          ReservationStatus.WAITED_OK,
          startDate,
          endDate,
          term,
          oneDayCharge,
          userId,
          roomId
      );
      assertThat(reservation).usingRecursiveComparison().isEqualTo(reservationDto);
    }

    @Test
    @DisplayName("실패: id가 null 인 경우")
    public void failIdIsNull() {
      id = null;
      assertThatThrownBy(
          () -> new Reservation(
              id,
              ReservationStatus.WAITED_OK,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: ReservationStatus가 null 인 경우")
    public void failReservationStatusIsNull() {
      assertThatThrownBy(
          () -> new Reservation(
              id,
              null,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: ReservationStatus가 WAITED_OK가 아닌 경우")
    public void failReservationStatusIsNotWAITED_OK() {
      assertThatThrownBy(
          () -> new Reservation(
              id,
              ReservationStatus.ACCEPTED,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: startDate 값이 null인 경우")
    public void failStateDateIsNull() {
      startDate = null;
      assertThatThrownBy(
          () -> new Reservation(
              id,
              ReservationStatus.WAITED_OK,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: endDate 값이 null인 경우")
    public void failEndDateIsNull() {
      endDate = null;
      assertThatThrownBy(
          () -> new Reservation(
              id,
              ReservationStatus.WAITED_OK,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: startDate값이 endDate보다 미래인 경우")
    public void failStartDateIsAfterEndDate() {
      startDate = LocalDate.of(2022, 6, 20);
      assertThatThrownBy(
          () -> new Reservation(
              id,
              ReservationStatus.WAITED_OK,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: term 값이 0인 경우")
    public void failTermIsZero() {
      term = 0;
      assertThatThrownBy(
          () -> new Reservation(
              id,
              ReservationStatus.WAITED_OK,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: term 값이 음수인 경우")
    public void failTermIsMinus() {
      term = -1;
      assertThatThrownBy(
          () -> new Reservation(
              id,
              ReservationStatus.WAITED_OK,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: term 값이 null인 경우")
    public void failTermINull() {
      term = null;
      assertThatThrownBy(
          () -> new Reservation(
              id,
              ReservationStatus.WAITED_OK,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: oneDayCharge가 음수인 경우")
    public void failOneDayChargeIsMinus() {
      oneDayCharge = -1;
      assertThatThrownBy(
          () -> new Reservation(
              id,
              ReservationStatus.WAITED_OK,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: oneDayCharge가 null인 경우")
    public void failOneDayChargeIsNull() {
      oneDayCharge = null;
      assertThatThrownBy(
          () -> new Reservation(
              id,
              ReservationStatus.WAITED_OK,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: userId가 null인 경우")
    public void failUserIdIsNull() {
      userId = null;
      assertThatThrownBy(
          () -> new Reservation(
              id,
              ReservationStatus.WAITED_OK,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: roomId가 null인 경우")
    public void failRoomIdIsNull() {
      roomId = null;
      assertThatThrownBy(
          () -> new Reservation(
              id,
              ReservationStatus.WAITED_OK,
              startDate,
              endDate,
              term,
              oneDayCharge,
              userId,
              roomId
          )
      ).isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("수정 테스트")
  class Update {

    @Test
    @DisplayName("성공: ReservationStatus 변경 가능")
    public void successChangeReservationStatus() {
      Reservation reservation = new Reservation(
          id,
          ReservationStatus.WAITED_OK,
          startDate,
          endDate,
          term,
          oneDayCharge,
          userId,
          roomId
      );
      //기본 WAIT_OK 상태에서는 ACCEPTED, GUEST_CANCELED, ACCEPTED_BEFORE_CANCELED 변경 가능
      reservation.changeStatus(ReservationStatus.ACCEPTED);

      assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.ACCEPTED);
    }

    @Test
    @DisplayName("실패: ReservationStatus 변경 실패")
    public void failChangeReservationStatus() {
      Reservation reservation = new Reservation(
          id,
          ReservationStatus.WAITED_OK,
          startDate,
          endDate,
          term,
          oneDayCharge,
          userId,
          roomId
      );
      //기본 WAIT_OK 상태에서는 ACCEPTED_AFTER_CANCELLED, WAIT_REVIEW, COMPLETE 변경 불가
      assertThatThrownBy(
          () -> reservation.changeStatus(ReservationStatus.WAIT_REVIEW)
      ).isInstanceOf(IllegalArgumentException.class);
    }
  }
}