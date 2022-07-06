package com.prgrms.airbnb.domain.room.entity;

import com.prgrms.airbnb.domain.common.entity.Address;
import com.prgrms.airbnb.domain.common.entity.BaseEntity;
import com.prgrms.airbnb.domain.common.exception.InvalidParamException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
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

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RoomImage> roomImages = new ArrayList<>();

  public Room(Address address, Integer charge, String name, String description, RoomInfo roomInfo,
      RoomType roomType, List<RoomImage> images, Long userId) {

    setAddress(address);
    setCharge(charge);
    setName(name);
    this.description = description;
    this.roomInfo = roomInfo;
    setRoomType(roomType);
    images.forEach(this::setImage);
    setUserId(userId);
    this.isDeleted = Boolean.FALSE;
  }

  public Room(Long id, Address address, Integer charge, String name, String description,
      RoomInfo roomInfo, RoomType roomType, ReviewInfo reviewInfo, List<RoomImage> images,
      Long userId) {
    this.id = id;
    setAddress(address);
    setCharge(charge);
    setName(name);
    this.description = description;
    this.roomInfo = roomInfo;
    setRoomType(roomType);
    this.reviewInfo = reviewInfo;
    images.forEach(this::setImage);
    setUserId(userId);
    this.isDeleted = Boolean.FALSE;
  }

  public void enrollRoomImages(List<RoomImage> roomImages) {
    roomImages.forEach(roomImage -> roomImage.setRoom(this));
  }

  public void deleteRoomImages(List<RoomImage> deleteRoomImages) {
    this.roomImages.removeIf(deleteRoomImages::contains);
  }

  public void changeCharge(Integer charge) {
    setCharge(charge);
  }

  public void changeName(String newName) {
    setName(newName);
  }

  public void changeDescription(String description) {
    this.description = description;
  }

  public void setImage(RoomImage roomImage) {
    if (roomImage == null) {
      throw new InvalidParamException("[" + this.getClass().getName() + "] 잘못된 입력입니다.");
    }
    roomImage.setRoom(this);
  }

  public void deleteImage(RoomImage roomImage) {
    if (!this.roomImages.contains(roomImage)) {
      throw new InvalidParamException("[" + this.getClass().getName() + "] 존재하지 않는 이미지입니다.");
    }
    roomImage.deleteRoom();
  }

  private void setCharge(Integer charge) {
    if (charge == null || charge < 0) {
      throw new InvalidParamException("[" + this.getClass().getName() + "] 1박 가격은 0보다 작을 수 없습니다.");
    }
    this.charge = charge;
  }

  private void setName(String newName) {
    if (StringUtils.isBlank(newName)) {
      throw new InvalidParamException("[" + this.getClass().getName() + "] 게시글 제목은 필수로 입력해야합니다.");
    }
    this.name = newName;
  }

  private void setRoomType(RoomType roomType) {
    if (ObjectUtils.isEmpty(roomType)) {
      throw new InvalidParamException("[" + this.getClass().getName() + "] 주거타입은 필수로 설정해야 합니다.");
    }
    this.roomType = roomType;
  }

  private void setUserId(Long userId) {
    if (ObjectUtils.isEmpty(userId)) {
      throw new InvalidParamException("[" + this.getClass().getName() + "] 유효하지 않은 UserId 입니다.");
    }
    this.userId = userId;
  }

  private void setAddress(Address address) {
    if (ObjectUtils.isEmpty(address)) {
      throw new InvalidParamException("[" + this.getClass().getName() + "] 유효하지 않은 주소입니다.");
    }
    this.address = address;
  }

  @Embeddable
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReviewInfo {

    private Double reviewRating = 0.0;
    private Long reviewCount = 0L;

    public ReviewInfo(Double reviewRating, Long reviewCount) {
      if (reviewCount < 0) {
        throw new InvalidParamException(
            "[" + this.getClass().getName() + "] 리뷰 개수는 0보다 작을 수 없습니다.");
      }
      this.reviewRating = reviewRating;
      this.reviewCount = reviewCount;
    }

    public void addReviewInfo(Integer newRating) {
      double totalRating = reviewRating * reviewCount;
      reviewCount += 1;
      reviewRating = (totalRating + newRating) / reviewCount;
    }

    public void changeReviewInfo(Integer oldRating, Integer newRating) {
      double totalRating = reviewRating * reviewCount - oldRating;
      reviewRating = (totalRating + newRating) / reviewCount;
    }
  }
}