package com.example.api.core.recommendation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Recommendation {
  private final int productId;
  private final int recommendationId;
  private final String author;
  private final int rate;
  private final String content;
  private final String serviceAddress;

  public Recommendation() {
    productId = 0;
    recommendationId = 0;
    author = null;
    rate = 0;
    content = null;
    serviceAddress = null;
  }
}
