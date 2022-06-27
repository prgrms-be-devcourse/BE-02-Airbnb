package com.prgrms.airbnb.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {
    private String comment;
    private Integer rating;
    private Boolean visible;
}
