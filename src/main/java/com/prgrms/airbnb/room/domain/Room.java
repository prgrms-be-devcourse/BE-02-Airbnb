package com.prgrms.airbnb.room.domain;

import com.prgrms.airbnb.common.jpa.MoneyConverter;
import com.prgrms.airbnb.common.model.Address;
import com.prgrms.airbnb.common.model.BaseEntity;
import com.prgrms.airbnb.common.model.Money;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.util.ObjectUtils;

@Entity
@Table(name = "room")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE room SET is_deleted = true WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  private Address address;

  @Convert(converter = MoneyConverter.class)
  private Money charge;

  private String name;

  private String description;

  @Embedded
  private RoomInfo roomInfo;

  @Enumerated(value = EnumType.STRING)
  private RoomType roomType;

  @Embedded
  private ReviewInfo reviewInfo;

  private Boolean isDeleted = Boolean.FALSE;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "image_id")
  @OrderColumn(name = "image_idx")
  private List<RoomImage> images = new ArrayList<>();

  private Long userId;

  @Builder
  public Room(Long id, Address address, Money charge, String name, RoomInfo roomInfo,
      RoomType roomType, List<RoomImage> images, Long userId, String description,
      ReviewInfo reviewInfo) {
    this.id = id;
    setAddress(address);
    setCharge(charge);
    setName(name);
    this.roomInfo = roomInfo;
    setRoomType(roomType);
    setImages(images);
    setUserId(userId);
    setDescription(description);
    this.reviewInfo = reviewInfo;
  }

  public List<RoomImage> getImages() {
    return Collections.unmodifiableList(images);
  }

  public void changeImages(List<RoomImage> newImages) {
    if (this.images != null) {
      images.clear();
    }
    images.addAll(newImages);
  }

  public void changeName(String newName) {
    if (!this.name.equals(newName)) {
      setName(newName);
    }
  }

  public void changeCharge(Money newCharge) {
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

  private void setCharge(Money charge) {
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
