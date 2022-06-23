package com.prgrms.airbnb.domain.review.entity;

import com.prgrms.airbnb.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "comment")
  private String comment;

  @Column(name = "rating")
  private Integer rating;

  @Column(name = "visible")
  private Boolean visible;

  @Column(name = "reservation_id")
  private Long reservationId;

  @Builder
  public Review(String comment, Integer rating, Long reservationId, Boolean visible) {
    setComment(comment);
    setRating(rating);
    setReservationId(reservationId);
    setVisible(visible);
  }

  private void setComment(String comment) {
    if (StringUtils.isBlank(comment)) {
      throw new IllegalArgumentException();
    }
    this.comment = comment;
  }

  private void setRating(Integer rating) {
    if (ObjectUtils.isEmpty(rating)) {
      throw new IllegalArgumentException();
    }
    this.rating = rating;
  }

  private void setReservationId(Long reservationId) {
    if (ObjectUtils.isEmpty(reservationId)) {
      throw new IllegalArgumentException();
    }
    this.reservationId = reservationId;
  }

  private void setVisible(Boolean visible) {
    if (ObjectUtils.isEmpty(reservationId)) {
      throw new IllegalArgumentException();
    }
    this.visible = visible;
  }

  public Boolean isVisible() {
    return visible;
  }
}
