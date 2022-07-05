package com.prgrms.airbnb.domain.room.entity;

import com.prgrms.airbnb.domain.common.exception.InvalidParamException;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.ObjectUtils;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Access(value = AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RoomInfo {

  @ApiModelProperty(example = "최대 수용 인원")
  private Integer maxGuest;

  @ApiModelProperty(example = "방 개수")
  private Integer roomCount;

  @ApiModelProperty(example = "침대 개수")
  private Integer bedCount;

  @ApiModelProperty(example = "화장실 개수")
  private Integer bathroomCount;

  public RoomInfo(Integer maxGuest, Integer roomCount, Integer bedCount, Integer bathroomCount) {
    setMaxGuest(maxGuest);
    setRoomCount(roomCount);
    setBedCount(bedCount);
    setBathroomCount(bathroomCount);
  }

  public void changeMaxGuest(Integer maxGuest) {
    setMaxGuest(maxGuest);
  }

  public void changeBedCount(Integer bedCount) {
    setBedCount(bedCount);
  }

  private void setMaxGuest(Integer maxGuest) {
    if (ObjectUtils.isEmpty(maxGuest) || (maxGuest <= 0)) {
      throw new InvalidParamException("최대 수용 인원은 0보다 작거나 같을 수 없습니다.");
    }
    this.maxGuest = maxGuest;
  }

  private void setRoomCount(Integer roomCount) {
    if (ObjectUtils.isEmpty(roomCount) || (roomCount <= 0)) {
      throw new InvalidParamException("최대 방 개수는 0보다 작거나 같을 수 없습니다.");
    }
    this.roomCount = roomCount;
  }

  private void setBedCount(Integer bedCount) {
    if (ObjectUtils.isEmpty(bedCount) || bedCount < 0) {
      throw new InvalidParamException("침대 개수는 0보다 작을 수 없습니다.");
    }
    this.bedCount = bedCount;
  }

  private void setBathroomCount(Integer bathroomCount) {
    if (ObjectUtils.isEmpty(bathroomCount) || bathroomCount <= 0) {
      throw new InvalidParamException("화장실 개수는 0보다 작거나 같을 수 없습니다.");
    }
    this.bathroomCount = bathroomCount;
  }
}
