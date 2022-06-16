package com.prgrms.airbnb.review.domain;

import com.prgrms.airbnb.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD)
@Getter
@Table(name = "reviews")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String comment;

    private Integer rating;

    private Long reservationId;

    @Builder
    public Review(String comment, Integer rating, Long reservationId) {
        this.comment = comment;
        this.rating = rating;
        this.reservationId = reservationId;
    }
}
