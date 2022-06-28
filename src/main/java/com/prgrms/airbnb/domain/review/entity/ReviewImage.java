package com.prgrms.airbnb.domain.review.entity;

import com.prgrms.airbnb.domain.common.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "path")
  private String path;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "review_id", referencedColumnName = "id", nullable = false)
  private Review review;

  public ReviewImage(String path) {
    this.path = path;
  }

  public void setReview(Review review) {
    if (this.review != null) {
      this.review.getImages().remove(this);
    }
    this.review = review;
    review.getImages().add(this);
  }

  public void deleteReview() {
    this.review.getImages().remove(this);
    this.review = null;
  }
}
