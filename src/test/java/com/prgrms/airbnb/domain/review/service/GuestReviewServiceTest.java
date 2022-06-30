package com.prgrms.airbnb.domain.review.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.common.service.UploadService;
import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import com.prgrms.airbnb.domain.reservation.entity.ReservationStatus;
import com.prgrms.airbnb.domain.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.domain.review.dto.CreateReviewRequest;
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
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class GuestReviewServiceTest {

  ReviewInfo reviewInfo;
  Room room;
  Reservation reservation1;
  Review review1, review2;
  PageRequest pageRequest;
  @InjectMocks
  private GuestReviewService guestReviewService;
  @Mock
  private UploadService uploadService;
  @Mock
  private ReviewRepository reviewRepository;
  @Mock
  private RoomRepository roomRepository;
  @Mock
  private ReservationRepository reservationRepository;

  @BeforeEach
  void setUp() {
    reviewInfo = new ReviewInfo(4.4, 5L);
    room = new Room(10L, new Address("1", "2"), 30000, "담양 떡갈비", "뷰가 좋습니다",
        new RoomInfo(1, 2, 3, 4), RoomType.HOUSE, reviewInfo, List.of(new RoomImage("room path1")),
        1L);

    reservation1 = new Reservation("reservationRepository.createReservationId()",
        ReservationStatus.WAITED_OK, LocalDate.now().minusDays(5), LocalDate.now().minusDays(3), 3,
        30000, 1L, 10L);
    review1 = new Review("comment", 5, "245325", true,
        List.of(new ReviewImage("Path 1"), new ReviewImage("Path 2")));
    review2 = new Review("comment", 5, "245325", true,
        List.of(new ReviewImage("Path 1"), new ReviewImage("Path 2")));
    pageRequest = PageRequest.of(0, 2);
  }

  @Nested
  @DisplayName("리뷰 저장 테스트")
  class Save {

    @Test
    @DisplayName("성공: 리뷰를 저장합니다.")
    public void save() {
      //given
      CreateReviewRequest request = new CreateReviewRequest("hello", 5, true);
      MockMultipartFile multipartFile = new MockMultipartFile("img", "img", "text/plain",
          "img".getBytes());
      List<MultipartFile> multipartFiles = new ArrayList<>();
      multipartFiles.add(multipartFile);
      reservation1.changeStatus(ReservationStatus.ACCEPTED);
      reservation1.changeStatus(ReservationStatus.WAIT_REVIEW);
      when(reservationRepository.findById(reservation1.getId())).thenReturn(
          Optional.of(reservation1));
      when(uploadService.uploadImg(any())).thenReturn("path");
      when(roomRepository.findById(10L)).thenReturn(Optional.of(room));
      when(reviewRepository.save(any())).thenReturn(review1);
      //when
      ReviewResponse reviewResponse = guestReviewService.save(1L, reservation1.getId(), request,
          multipartFiles);
      //then
      Assertions.assertThat(reservation1.getReservationStatus()).isEqualTo(ReservationStatus.COMPLETE);
      System.out.println(room.getReviewInfo().getReviewRating());
      Assertions.assertThat(room.getReviewInfo().getReviewCount()).isEqualTo(6);
    }

    @Test
    @DisplayName("실패: 리뷰를 남길수 없는 상태입니다..")
    public void failCantReviewed() {
      //given
      CreateReviewRequest request = new CreateReviewRequest("hello", 5, true);
      MockMultipartFile multipartFile = new MockMultipartFile("img", "img", "text/plain",
          "img".getBytes());
      List<MultipartFile> multipartFiles = new ArrayList<>();
      multipartFiles.add(multipartFile);
      when(reservationRepository.findById(reservation1.getId())).thenReturn(
          Optional.of(reservation1));
      //when
      //then
      Assertions.assertThatThrownBy(
              () -> guestReviewService.save(1L, reservation1.getId(), request, multipartFiles))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: 리뷰 저장 권한이 없습니다.")
    public void failAuthentication() {
      //given
      CreateReviewRequest request = new CreateReviewRequest("hello", 5, true);
      MockMultipartFile multipartFile = new MockMultipartFile("img", "img", "text/plain",
          "img".getBytes());
      List<MultipartFile> multipartFiles = new ArrayList<>();
      multipartFiles.add(multipartFile);
      reservation1.changeStatus(ReservationStatus.ACCEPTED);
      reservation1.changeStatus(ReservationStatus.WAIT_REVIEW);
      when(reservationRepository.findById(reservation1.getId())).thenReturn(
          Optional.of(reservation1));
      //when
      //then
      Assertions.assertThatThrownBy(
              () -> guestReviewService.save(2L, reservation1.getId(), request, multipartFiles))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }
}