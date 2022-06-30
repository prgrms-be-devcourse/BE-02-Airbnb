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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class HostReviewServiceTest {

  ReviewInfo reviewInfo;
  Room room;
  Review review1, review2;
  PageRequest pageRequest;
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
    pageRequest = PageRequest.of(0, 2);
  }

  @Nested
  @DisplayName("리뷰 조회 테스트")
  class FindAllByRoomId {

    @Test
    @DisplayName("성공: review를 조회합니다.")
    void findAll() {
      //given
      when(roomRepository.findById(10L)).thenReturn(Optional.of(room));
      PageRequest pageRequest = PageRequest.of(0, 2);
      when(reviewRepository.findAllByRoomId(10L, pageRequest)).thenReturn(
          new SliceImpl<>(List.of(review1, review2), pageRequest, true));
      //when
      Slice<ReviewResponse> reviewList = hostReviewService.findAllByRoomId(1L, 10L, pageRequest);
      //then
      Assertions.assertThat(reviewList.getContent().get(0)).usingRecursiveComparison()
          .isEqualTo(review1);
    }

    @Test
    @DisplayName("실패: 없는 roomId로 조회합니다.")
    void failWrongRoomId() {
      //given
      when(roomRepository.findById(anyLong())).thenThrow(new IllegalArgumentException());
      //when
      //then
      Assertions.assertThatThrownBy(
              () -> hostReviewService.findAllByRoomId(1L, anyLong(), pageRequest))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: 롬에 대한 Host 권한이 없습니다.")
    void failWrongAuthority() {
      //given
      Room roomWithWrongUserId = new Room(10L, new Address("1", "2"), 30000, "담양 떡갈비", "뷰가 좋습니다",
          new RoomInfo(1, 2, 3, 4), RoomType.HOUSE, reviewInfo,
          List.of(new RoomImage("room path1")), 999L);
      when(roomRepository.findById(10L)).thenReturn(Optional.of(roomWithWrongUserId));
      //when
      //then
      Assertions.assertThatThrownBy(() -> hostReviewService.findAllByRoomId(1L, 10L, pageRequest))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }
}