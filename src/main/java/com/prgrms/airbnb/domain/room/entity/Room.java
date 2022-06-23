package com.prgrms.airbnb.domain.room.entity;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "room")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE room SET is_deleted = true WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "charge")
  private Integer charge;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "room_type")
  private RoomType roomType;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

  @Embedded
  private Address address;

  @Embedded
  private RoomInfo roomInfo;

  @Embedded
  private ReviewInfo reviewInfo;

  @OneToMany(mappedBy = "room")
  private List<RoomImage> images = new ArrayList<>();

  public Room(Address address, Integer charge, String name, String description,
      RoomInfo roomInfo, RoomType roomType, List<RoomImage> images,
      Long userId) {
    this.address = address;
    this.charge = charge;
    this.name = name;
    this.description = description;
    this.roomInfo = roomInfo;
    this.roomType = roomType;
    this.images = images;
    this.userId = userId;
    this.isDeleted = Boolean.FALSE;
  }

  public Room(Long id, Address address, Integer charge, String name, String description,
      RoomInfo roomInfo, RoomType roomType, ReviewInfo reviewInfo, List<RoomImage> images,
      Long userId) {
    this.id = id;
    this.address = address;
    this.charge = charge;
    this.name = name;
    this.description = description;
    this.roomInfo = roomInfo;
    this.roomType = roomType;
    this.reviewInfo = reviewInfo;
    this.images = images;
    this.userId = userId;
    this.isDeleted = Boolean.FALSE;
  }

  public List<RoomImage> getImages() {
    return Collections.unmodifiableList(images);
  }

  public void changeImages(List<RoomImage> newImages) {
    if (!ObjectUtils.isEmpty(newImages)) {
      images.clear();
    }
    images.addAll(newImages);
  }

  public void changeName(String newName) {
    if (!this.name.equals(newName)) {
      setName(newName);
    }
  }

  public void changeCharge(Integer newCharge) {
    if (!this.charge.equals(newCharge)) {
      setCharge(newCharge);
    }
  }

  public void changeDescription(String newDescription) {
    if (!this.charge.equals(newDescription)) {
      setDescription(newDescription);
    }
  }

  private void setAddress(Address address) {
    if (ObjectUtils.isEmpty(address)) {
      throw new IllegalArgumentException();
    }
    this.address = address;
  }

  private void setCharge(Integer charge) {
    if (charge < 0) {
      throw new IllegalArgumentException();
    }
    this.charge = charge;
  }

  private void setName(String newName) {
    if (StringUtils.isBlank(newName)) {
      throw new IllegalArgumentException();
    }
    this.name = newName;
  }

  private void setRoomType(RoomType roomType) {
    this.roomType = roomType;
  }

  private void setUserId(Long userId) {
    if (ObjectUtils.isEmpty(id)) {
      throw new IllegalArgumentException();
    }
    this.userId = userId;
  }

  private void setDescription(String description) {
    this.description = description;
  }

  private void setImages(List<RoomImage> images) {
    this.images = images;
  }

  @Embeddable
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  class ReviewInfo {

    private Double reviewRating;
    private Long reviewCount;

    public ReviewInfo(Double reviewRating, Long reviewCount) {
      if (reviewCount < 0) {
        throw new IllegalArgumentException();
      }
      this.reviewRating = reviewRating;
      this.reviewCount = reviewCount;
    }
  }

}
