package com.prgrms.airbnb.domain.room.dto;

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
public class UpdateRoomRequest {

  private Long id;
  private Integer charge;
  private String name;
  private String description;
  private Integer maxGuest;
  private Integer bedCount;
  @Builder.Default
  private List<RoomImage> images = new ArrayList<>();

}
