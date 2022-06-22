package com.prgrms.airbnb.room.dto;

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
public class UpdateRoomRequest {
  private Long id;
  private Integer charge;
  private String name;
  private String description;
  private RoomInfo roomInfo;
  private RoomType roomType;
  @Builder.Default
  private List<RoomImage> images = new ArrayList<>();
  private Long userId;

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
