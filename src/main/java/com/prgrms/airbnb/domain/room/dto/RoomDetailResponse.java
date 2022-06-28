package com.prgrms.airbnb.domain.room.dto;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomInfo;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailResponse {

  private Long id;
  private Address address;
  private Integer charge;
  private String name;
  private String description;
  private RoomInfo roomInfo;
  private RoomType roomType;
  @Builder.Default
  private List<RoomImage> roomImages = new ArrayList<>();
  private Long userId;

}
