package com.prgrms.airbnb.room.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Embeddable
@Getter
@Access(value = AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

  public void changeMaxGuest(Integer newMaxGuest) {
    if (!this.maxGuest.equals(newMaxGuest)) {
      setMaxGuest(newMaxGuest);
    }
  }

  public void changeBedCount(Integer newBedCount) {
    if (!this.bedCount.equals(newBedCount)) {
      setBedCount(newBedCount);
    }
  }

  private void setMaxGuest(Integer maxGuest) {
    if (ObjectUtils.isEmpty(maxGuest)) {
      throw new IllegalArgumentException();
    }
    this.maxGuest = maxGuest;
  }

  private void setRoomCount(Integer roomCount) {
    if (ObjectUtils.isEmpty(roomCount)) {
      throw new IllegalArgumentException();
    }
    this.roomCount = roomCount;
  }

  private void setBedCount(Integer bedCount) {
    if (ObjectUtils.isEmpty(maxGuest)) {
      throw new IllegalArgumentException();
    }
    this.bedCount = bedCount;
  }

  private void setBathroomCount(Integer bathroomCount) {
    if (ObjectUtils.isEmpty(bathroomCount)) {
      throw new IllegalArgumentException();
    }
    this.bathroomCount = bathroomCount;
  }
}
