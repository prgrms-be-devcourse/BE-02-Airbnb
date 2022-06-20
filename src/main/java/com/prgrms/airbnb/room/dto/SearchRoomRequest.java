package com.prgrms.airbnb.room.dto;

import com.prgrms.airbnb.common.model.Money;
import com.prgrms.airbnb.room.domain.RoomInfo;
import com.prgrms.airbnb.room.domain.RoomType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SearchRoomRequest {

  private String keyword;
  private RoomType roomType;
  private RoomInfo roomInfo;
  private Money minCharge;
  private Money maxCharge;
  private Double rating;

}
