package com.prgrms.airbnb.domain.room.dto;

import com.prgrms.airbnb.domain.room.entity.RoomInfo;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchRoomRequest {

  private String keyword;
  private RoomType roomType;
  private Integer maxGuest;
  private Integer roomCount;
  private Integer bedCount;
  private Integer minCharge;
  private Integer maxCharge;
  private Double rating;
}
