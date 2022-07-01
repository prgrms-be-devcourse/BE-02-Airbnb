package com.prgrms.airbnb.domain.room.entity;

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

  private Integer maxGuest;

  private Integer roomCount;

  private Integer bedCount;

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
      throw new IllegalArgumentException();
    }
    this.maxGuest = maxGuest;
  }

  private void setRoomCount(Integer roomCount) {
    if (ObjectUtils.isEmpty(roomCount) || (roomCount <= 0)) {
      throw new IllegalArgumentException();
    }
    this.roomCount = roomCount;
  }

  private void setBedCount(Integer bedCount) {
    if (ObjectUtils.isEmpty(bedCount) || bedCount < 0) {
      throw new IllegalArgumentException();
    }
    this.bedCount = bedCount;
  }

  private void setBathroomCount(Integer bathroomCount) {
    if (ObjectUtils.isEmpty(bathroomCount) || bathroomCount <= 0) {
      throw new IllegalArgumentException();
    }
    this.bathroomCount = bathroomCount;
  }
}
