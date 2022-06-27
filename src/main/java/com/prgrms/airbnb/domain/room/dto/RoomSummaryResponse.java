package com.prgrms.airbnb.domain.room.dto;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomSummaryResponse {

  private Long id;
  private Address address;
  private Integer charge;
  private String name;
  private RoomType roomType;
  private RoomImage roomImage;

  public void setRoomImage(RoomImage roomImage) {
    this.roomImage = roomImage;
  }
}
