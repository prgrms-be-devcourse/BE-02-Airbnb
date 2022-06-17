package com.prgrms.airbnb.review.domain;

import com.prgrms.airbnb.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String comment;

    private Integer rating;

    private Long reservationId;

    private Boolean visible;

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
