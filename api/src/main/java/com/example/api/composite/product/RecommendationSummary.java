package com.example.api.composite.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecommendationSummary {
  private final int recommendationId;
  private final String author;
  private final int rate;
}
