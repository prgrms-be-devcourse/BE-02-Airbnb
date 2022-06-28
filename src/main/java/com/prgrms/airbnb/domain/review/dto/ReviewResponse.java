package com.prgrms.airbnb.domain.review.dto;

import com.prgrms.airbnb.domain.review.entity.ReviewImage;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

  private Long id;
  private String comment;
  private Integer rating;
  private Boolean visible;
  @Builder.Default
  private List<ReviewImage> images = new ArrayList<>();
}
