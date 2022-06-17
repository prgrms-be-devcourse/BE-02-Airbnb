package com.prgrms.airbnb.room.dto;

import com.prgrms.airbnb.common.model.Address;
import com.prgrms.airbnb.common.model.Money;
import com.prgrms.airbnb.room.domain.RoomImage;
import com.prgrms.airbnb.room.domain.RoomInfo;
import com.prgrms.airbnb.room.domain.RoomType;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {

  private Address address;
  private Money charge;
  private String name;
  private String description;
  private RoomInfo roomInfo;
  private RoomType roomType;
  @Builder.Default
  private List<RoomImage> images = new ArrayList<>();
  private Long userId;
}
