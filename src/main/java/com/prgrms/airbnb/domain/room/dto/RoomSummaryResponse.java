package com.prgrms.airbnb.domain.room.dto;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomSummaryResponse {

  @ApiModelProperty(example = "Room 아이디")
  private Long id;

  private Address address;

  @ApiModelProperty(example = "가격(1박)")
  private Integer charge;

  @ApiModelProperty(example = "게시글 제목")
  private String name;

  @ApiModelProperty(example = "주거 타입")
  private RoomType roomType;

  @ApiModelProperty(example = "게시글 대표 사진(썸네일)")
  private RoomImage roomImage;

  public void setRoomImage(RoomImage roomImage) {
    this.roomImage = roomImage;
  }
}
