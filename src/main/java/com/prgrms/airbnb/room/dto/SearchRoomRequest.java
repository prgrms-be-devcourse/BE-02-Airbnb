package com.prgrms.airbnb.room.dto;

import com.prgrms.airbnb.room.domain.RoomInfo;
import com.prgrms.airbnb.room.domain.RoomType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchRoomRequest {

  private String keyword;
  private RoomType roomType;
  private RoomInfo roomInfo;
  private Integer minCharge;
  private Integer maxCharge;
  private Double rating;

}
