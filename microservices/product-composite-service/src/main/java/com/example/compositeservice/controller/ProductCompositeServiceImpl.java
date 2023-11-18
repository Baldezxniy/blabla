package com.example.compositeservice.controller;

import com.example.api.composite.product.*;
import com.example.api.core.product.Product;
import com.example.api.core.recommendation.Recommendation;
import com.example.api.core.review.Review;
import com.example.compositeservice.service.ProductCompositeIntegrationService;
import com.example.util.http.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @RequestMapping "v1/product-composite"
 */
@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {
  private final ServiceUtil serviceUtil;
  private final ProductCompositeIntegrationService integration;

  public ProductCompositeServiceImpl(
          ServiceUtil serviceUtil,
          ProductCompositeIntegrationService integration
  ) {
    this.serviceUtil = serviceUtil;
    this.integration = integration;
  }

  @Override
  public void createProduct(ProductAggregate body) {

  }

  /**
   * @param productId @PathVariable("productId")
   * @GetMapping "/{productId}"
   */
  @Override
  public ProductAggregate getProduct(int productId) {
    Product product = integration.getProduct(productId);
    List<Recommendation> recommendations =
            integration.getRecommendations(productId);
    List<Review> reviews = integration.getReviews(productId);

    return createProductAggregate(product, recommendations,
            reviews, serviceUtil.getServiceAddress());
  }

  @Override
  public void deleteProduct(int productId) {

  }

  private ProductAggregate createProductAggregate(
          Product product, List<Recommendation> recommendations, List<Review> reviews, String serviceAddress
  ) {
// 1. Setup product info
    int productId = product.getProductId();
    String name = product.getName();
    int weight = product.getWeight();

    // 2. Copy summary recommendation info, if available
    List<RecommendationSummary> recommendationSummaries =
            (recommendations == null) ? null : recommendations.stream()
                    .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate()))
                    .collect(Collectors.toList());

    // 3. Copy summary review info, if available
    List<ReviewSummary> reviewSummaries =
            (reviews == null) ? null : reviews.stream()
                    .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject()))
                    .collect(Collectors.toList());

    // 4. Create info regarding the involved microservices addresses
    String productAddress = product.getServiceAddress();
    String reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
    String recommendationAddress = (recommendations != null && recommendations.size() > 0) ? recommendations.get(0).getServiceAddress() : "";
    ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

    return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
  }
}
