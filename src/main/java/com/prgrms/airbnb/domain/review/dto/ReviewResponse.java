package com.prgrms.airbnb.domain.review.dto;

import com.prgrms.airbnb.domain.review.entity.ReviewImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String comment;
    private Integer rating;
    private Boolean visible;
    private List<ReviewImage> images;
}
