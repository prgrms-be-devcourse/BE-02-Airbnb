package com.prgrms.airbnb.domain.review.entity;

import com.prgrms.airbnb.domain.common.entity.BaseEntity;
import com.prgrms.airbnb.domain.room.entity.RoomImage;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

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
    private String reservationId;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> images = new ArrayList<>();

    public Review(String comment, Integer rating, String reservationId, Boolean visible, List<ReviewImage> images) {
        setComment(comment);
        setRating(rating);
        setReservationId(reservationId);
        setVisible(visible);
        images.forEach(this::setImage);
    }

    public Review(Long id, String comment, Integer rating, String reservationId, Boolean visible, List<ReviewImage> images) {
        this.id = id;
        setComment(comment);
        setRating(rating);
        setReservationId(reservationId);
        setVisible(visible);
        images.forEach(this::setImage);
    }

    public Boolean isVisible() {
        return visible;
    }

    public void changeComment(String comment) {
        setComment(comment);
    }

    public void changeRating(Integer rating) {
        setRating(rating);
    }

    public void changeVisible(Boolean visible) {
        setVisible(visible);
    }

    public void addImage(ReviewImage reviewImage) {
        reviewImage.setReview(this);
    }

    public void deleteImage(RoomImage roomImage) {
        roomImage.deleteRoom();
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

    private void setReservationId(String reservationId) {
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

    private void setImage(ReviewImage reviewImage) {
        reviewImage.setReview(this);
    }
}
