package com.prgrms.airbnb.domain.room.dto;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import com.prgrms.airbnb.domain.room.entity.RoomInfo;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import io.swagger.annotations.ApiModelProperty;
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
public class RoomDetailResponse {

  @ApiModelProperty(example = "Room 아이디")
  private Long id;

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

  @Builder.Default
  @ApiModelProperty(example = "게시글 사진 목록")
  private List<RoomImage> roomImages = new ArrayList<>();

  @ApiModelProperty(example = "호스트 아이디")
  private Long userId;
}
