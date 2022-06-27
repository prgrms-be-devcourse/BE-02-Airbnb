package com.prgrms.airbnb.domain.room.dto;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

  @QueryProjection
  public RoomSummaryResponse(Long id, Address address, Integer charge, String name,
      RoomType roomType, List<RoomImage> roomImages
  ) {
    this.id = id;
    this.address = address;
    this.charge = charge;
    this.name = name;
    this.roomType = roomType;
    this.roomImage = roomImages.get(0);
  }

  public void setRoomImage(RoomImage roomImage) {
    this.roomImage = roomImage;
  }
}
