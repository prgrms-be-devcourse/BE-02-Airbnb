package com.prgrms.airbnb.domain.room.dto;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomInfo;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {

  private Address address;
  private Integer charge;
  private String name;
  private String description;
  private RoomInfo roomInfo;
  private RoomType roomType;
  @Builder.Default
  private List<RoomImage> roomImages = new ArrayList<>();

  public void setRoomImages(List<RoomImage> roomImages) {
    this.roomImages = roomImages;
  }
}
