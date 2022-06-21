package com.prgrms.airbnb.room.dto;

import com.prgrms.airbnb.common.model.Address;
import com.prgrms.airbnb.common.model.Money;
import com.prgrms.airbnb.room.domain.RoomImage;
import com.prgrms.airbnb.room.domain.RoomType;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
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
  private Money charge;
  private String name;
  private RoomType roomType;
  private RoomImage roomImage;

  @QueryProjection
  public RoomSummaryResponse(Long id, Address address, Money charge, String name,
      RoomType roomType, List<RoomImage> roomImages
  ) {
    this.id = id;
    this.address = address;
    this.charge = charge;
    this.name = name;
    this.roomType = roomType;
    this.roomImage = roomImages.get(0);
  }
}
