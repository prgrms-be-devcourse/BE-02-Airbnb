package com.prgrms.airbnb.domain.reservation.dto;

import com.prgrms.airbnb.domain.reservation.entity.ReservationStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequest {

  @ApiModelProperty(value = "초기 예약 상태")
  private ReservationStatus reservationStatus;
  @ApiModelProperty(value = "예약 시작일")
  private LocalDate startDate;
  @ApiModelProperty(value = "예약 마지막일")
  private LocalDate endDate;
  @ApiModelProperty(value = "사용 기간", example = "1")
  private Integer period;
  @ApiModelProperty(value = "예약 금액", example = "10000")
  private Integer oneDayCharge;
  @ApiModelProperty(value = "예약 guest id", example = "1")
  private Long userId;
  @ApiModelProperty(value = "예약하려는 방 id", example = "1")
  private Long roomId;
}
