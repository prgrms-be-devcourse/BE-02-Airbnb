package com.prgrms.airbnb.domain.room.dto;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.room.entity.RoomInfo;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {

  private Address address;

  @ApiModelProperty(example = "가격(1박)")
  private Integer charge;

  @ApiModelProperty(example = "게시글 제목")
  private String name;

  @ApiModelProperty(example = "게시글 상세설명")
  private String description;

  private RoomInfo roomInfo;

  @ApiModelProperty(example = "주거 타입")
  private RoomType roomType;
}
