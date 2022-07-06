package com.prgrms.airbnb.domain.room.dto;

import com.prgrms.airbnb.domain.room.entity.RoomImage;
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
public class UpdateRoomRequest {

  @ApiModelProperty(example = "Room 아이디")
  private Long id;

  @ApiModelProperty(example = "가격(1박)")
  private Integer charge;

  @ApiModelProperty(example = "게시글 제목")
  private String name;

  @ApiModelProperty(example = "게시글 상세설명")
  private String description;

  @ApiModelProperty(example = "최대 수용 인원")
  private Integer maxGuest;

  @ApiModelProperty(example = "침대 개수")
  private Integer bedCount;

  @Builder.Default
  @ApiModelProperty(example = "게시글 사진 목록")
  private List<RoomImage> roomImages = new ArrayList<>();
}
