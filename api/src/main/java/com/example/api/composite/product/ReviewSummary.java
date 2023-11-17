package com.example.api.composite.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewSummary {
  private final int reviewId;
  private final String author;
  private final String subject;
}
