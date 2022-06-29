package com.prgrms.airbnb.domain.review.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.review.dto.ReviewResponse;
import com.prgrms.airbnb.domain.review.entity.Review;
import com.prgrms.airbnb.domain.review.entity.ReviewImage;
import com.prgrms.airbnb.domain.review.repository.ReviewRepository;
import com.prgrms.airbnb.domain.room.entity.Room;
import com.prgrms.airbnb.domain.room.entity.Room.ReviewInfo;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomInfo;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import com.prgrms.airbnb.domain.room.repository.RoomRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HostReviewServiceTest {

  ReviewInfo reviewInfo;
  Room room;
  Review review1, review2;

  @InjectMocks
  private HostReviewService hostReviewService;
  @Mock
  private ReviewRepository reviewRepository;
  @Mock
  private RoomRepository roomRepository;

  @BeforeEach
  void setUp() {
    room = new Room(10L, new Address("1", "2"), 30000, "담양 떡갈비", "뷰가 좋습니다",
        new RoomInfo(1, 2, 3, 4), RoomType.HOUSE, reviewInfo, List.of(new RoomImage("room path1")),
        1L);
    review1 = new Review("comment", 5, "245325", true,
        List.of(new ReviewImage("Path 1"), new ReviewImage("Path 2")));
    review2 = new Review("comment", 5, "245325", true,
        List.of(new ReviewImage("Path 1"), new ReviewImage("Path 2")));
  }

  @AfterEach
  void clear() {
    roomRepository.deleteAll();
    reviewRepository.deleteAll();
  }

  @Nested
  @DisplayName("리뷰 조회 테스트")
  class FindAllByRoomId {

    @Test
    @DisplayName("성공: review를 조회합니다.")
    void findAll() {
      //given
      when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
      when(reviewRepository.findAllByRoomId(anyLong())).thenReturn(List.of(review1, review2));
      //when
      List<ReviewResponse> reviewList = hostReviewService.findAllByRoomId(1L, anyLong());
      //then
      Assertions.assertThat(reviewList.get(0)).usingRecursiveComparison().isEqualTo(review1);
    }

    @Test
    @DisplayName("실패: 없는 roomId로 조회합니다.")
    void failWrongRoomId() {
      //given
      when(roomRepository.findById(anyLong())).thenThrow(new IllegalArgumentException());
      //when
      //then
      Assertions.assertThatThrownBy(() -> hostReviewService.findAllByRoomId(1L, anyLong()))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: 없는 roomId로 조회합니다.")
    void failWrongAuthority() {
      //given
      when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
      //when
      //then
      List<ReviewResponse> reviewList = hostReviewService.findAllByRoomId(1L, anyLong());
    }
  }
}