package com.prgrms.airbnb.domain.room.dto;

import com.prgrms.airbnb.domain.room.entity.RoomType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchRoomRequest {

  @ApiModelProperty(example = "검색어")
  private String keyword;

  @ApiModelProperty(example = "주거 타입")
  private RoomType roomType;

  @ApiModelProperty(example = "최대 수용 인원")
  private Integer maxGuest;

  @ApiModelProperty(example = "방 개수")
  private Integer roomCount;

  @ApiModelProperty(example = "침대 개수")
  private Integer bedCount;

  @ApiModelProperty(example = "최소 가격(1박)")
  private Integer minCharge;

  @ApiModelProperty(example = "최대 가격(1박)")
  private Integer maxCharge;

  @ApiModelProperty(example = "리뷰 평점")
  private Double rating;
}
