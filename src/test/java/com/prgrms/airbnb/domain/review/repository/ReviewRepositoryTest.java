package com.prgrms.airbnb.domain.review.repository;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import com.prgrms.airbnb.domain.reservation.entity.ReservationStatus;
import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.domain.review.entity.Review;
import com.prgrms.airbnb.domain.review.entity.ReviewImage;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomInfo;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
class ReviewRepositoryTest {

  String comment;
  Integer rating;
  Boolean open, closed;
  Reservation reservation1, reservation2;
  Room room;
  ReviewImage reviewImage1, reviewImage2;
  @Autowired
  private ReviewRepository reviewRepository;
  @Autowired
  private ReservationRepository reservationRepository;
  @Autowired
  private RoomRepository roomRepository;

  @BeforeEach
  void setUp() {
    comment = "여기 좋습니다";
    rating = 5;
    open = true;
    closed = false;
    reviewImage1 = new ReviewImage("Path 1");
    reviewImage2 = new ReviewImage("Path 1");
  }

  @Test
  @DisplayName("성공: roomId를 통해 review를 최신순으로 검색합니다.")
  void findAllByRoomId() {
    room = new Room(new Address("1", "2"), 30000, "담양 떡갈비", "뷰가 좋습니다", new RoomInfo(1, 2, 3, 4),
        RoomType.HOUSE, List.of(new RoomImage("room path1")), 1L);
    roomRepository.save(room);
    reservation1 = new Reservation(reservationRepository.createReservationId(),
        ReservationStatus.WAITED_OK, LocalDate.of(2022, 6, 5), LocalDate.of(2022, 6, 8), 3, 30000,
        1L, room.getId());
    reservation2 = new Reservation(reservationRepository.createReservationId(),
        ReservationStatus.WAITED_OK, LocalDate.of(2022, 6, 10), LocalDate.of(2022, 6, 15), 3, 30000,
        1L, room.getId());
    reservationRepository.save(reservation1);
    reservationRepository.save(reservation2);
    Review review1 = new Review(comment, rating, reservation1.getId(), true,
        List.of(reviewImage1, reviewImage2));
    Review review2 = new Review(comment, rating, reservation2.getId(), true,
        List.of(reviewImage1, reviewImage2));
    reviewRepository.save(review1);
    reviewRepository.save(review2);
    List<Review> reviewListOrderByCreatedAtDesc = reviewRepository.findAllByRoomId(room.getId());
    Assertions.assertThat(reviewListOrderByCreatedAtDesc.size()).isEqualTo(2);
    Assertions.assertThat(reviewListOrderByCreatedAtDesc.get(0).getId())
        .isNotEqualTo(review1.getId());
  }

  @Nested
  @DisplayName("리뷰 저장 테스트")
  class SaveTest {

    @Test
    @DisplayName("성공: 빈 이미지인 경우 review를 저장합니다.")
    void empty_image_save() {
      Review review = new Review(comment, rating, "245325", true, List.of());
      reviewRepository.save(review);
      Assertions.assertThat(review.getId()).isNotNull();
      Assertions.assertThat(review.getImages()).isEmpty();
    }

    @Test
    @DisplayName("성공: 여러 이미지인 경우 review를 저장합니다.")
    void review_save() {
      Review review = new Review(comment, rating, "245325", true,
          List.of(reviewImage1, reviewImage2));
      reviewRepository.save(review);
      Assertions.assertThat(review.getId()).isNotNull();
      Assertions.assertThat(review.getImages()).isNotEmpty();
    }

    @Test
    @DisplayName("실패: comment이 null일때 예외가 발생합니다.")
    void failCommentIsNull() {
      Assertions.assertThatThrownBy(
              () -> new Review("", rating, "245325", true, List.of(reviewImage1, reviewImage2)))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: rating이 null일때 예외가 발생합니다.")
    void failRatingIsNull() {
      Assertions.assertThatThrownBy(
              () -> new Review(comment, null, "245325", true, List.of(reviewImage1, reviewImage2)))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: rating이 0~5 범위를 벗어났을때 예외가 발생합니다.")
    void failRatingIsOutOfBoundary() {
      Assertions.assertThatThrownBy(
              () -> new Review(comment, 7, "245325", true, List.of(reviewImage1, reviewImage2)))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("실패: reservationId가 null일때 예외가 발생합니다.")
    void failReservationIsNull() {
      Assertions.assertThatThrownBy(
              () -> new Review(comment, rating, null, true, List.of(reviewImage1, reviewImage2)))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: visible이 null일때 예외가 발생합니다.")
    void failVisibleIsNull() {
      Assertions.assertThatThrownBy(
              () -> new Review(comment, rating, "245325", null, List.of(reviewImage1, reviewImage2)))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("리뷰 수정 테스트")
  class ChangeTest {

    @Test
    @DisplayName("성공: review comment 수정합니다.")
    void changeComment() {
      Review review = new Review(comment, rating, "245325", true,
          List.of(reviewImage1, reviewImage2));
      reviewRepository.save(review);
      review.changeComment("change");
      Assertions.assertThat(review.getComment()).isEqualTo("change");
    }

    @Test
    @DisplayName("성공: review rating을 수정합니다.")
    void changeRating() {
      Review review = new Review(comment, rating, "245325", true,
          List.of(reviewImage1, reviewImage2));
      reviewRepository.save(review);
      review.changeRating(3);
      Assertions.assertThat(review.getRating()).isEqualTo(3);
    }

    @Test
    @DisplayName("성공: review visible을 수정합니다.")
    void changeVisible() {
      Review review = new Review(comment, rating, "245325", true,
          List.of(reviewImage1, reviewImage2));
      reviewRepository.save(review);
      review.changeVisible(false);
      Assertions.assertThat(review.getVisible()).isEqualTo(false);
    }
  }

  @Nested
  @DisplayName("리뷰 삭제 테스트")
  class DeleteTest {

    @Test
    @DisplayName("성공: review를 삭제합니다.")
    void deleteReview() {
      Review review = new Review(comment, rating, "245325", true,
          List.of(reviewImage1, reviewImage2));
      reviewRepository.save(review);
      reviewRepository.deleteById(review.getId());
      Assertions.assertThat(reviewRepository.findById(review.getId())).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("실패: 없는 ID로 review를 삭제합니다.")
    void faildeleteReview() {
      Review review = new Review(comment, rating, "245325", true,
          List.of(reviewImage1, reviewImage2));
      reviewRepository.save(review);
      Assertions.assertThatThrownBy(
              () -> reviewRepository.deleteById(3L))
          .isInstanceOf(EmptyResultDataAccessException.class);
    }
  }
}