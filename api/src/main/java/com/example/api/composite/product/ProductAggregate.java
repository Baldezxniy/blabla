package com.example.api.composite.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductAggregate {
  private final int productId;
  private final String name;
  private final int weight;
  private final List<RecommendationSummary> recommendations;
  private final List<ReviewSummary> reviews;
  private final ServiceAddresses serviceAddress;
}
