package com.example.compositeservice.service;

import com.example.api.core.product.Product;
import com.example.api.core.product.ProductService;
import com.example.api.core.recommendation.Recommendation;
import com.example.api.core.recommendation.RecommendationService;
import com.example.api.core.review.Review;
import com.example.api.core.review.ReviewService;
import com.example.api.exceptions.InvalidInputException;
import com.example.api.exceptions.NotFoundException;
import com.example.util.http.HttpErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpMethod.GET;

@Service
public class ProductCompositeIntegrationService implements ProductService, RecommendationService, ReviewService {

  private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegrationService.class);

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  public final String productServiceUrl;
  public final String recommendationServiceUrl;
  public final String reviewServiceUrl;

  public ProductCompositeIntegrationService(

          RestTemplate restTemplate,
          ObjectMapper objectMapper,

          @Value("${app.product-service.host}") String productServiceHost,
          @Value("${app.product-service.port}") int productServicePort,

          @Value("${app.recommendation-service.host}") String recommendationServiceHost,
          @Value("${app.recommendation-service.port}") int recommendationServicePort,

          @Value("${app.review-service.host}") String reviewServiceHost,
          @Value("${app.review-service.port}") int reviewServicePort

  ) {

    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;

    productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/v1/product/";
    recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/v1/recommendation?productId=";
    reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/v1/review?productId=";
  }

  @Override
  public Product createProduct(Product body) {
    return null;
  }

  @Override
  public Product getProduct(int productId) {
    try {
      String url = productServiceUrl + productId;

      LOG.debug("Will call getProduct API on URL: {}", url);
      Product product = restTemplate.getForObject(url, Product.class);
      LOG.debug("Found a product with id: {}", product.getProductId());

      return product;
    } catch (HttpClientErrorException ex) {
      switch (Objects.requireNonNull(HttpStatus.resolve(ex.getStatusCode().value()))) {

        case NOT_FOUND -> throw new NotFoundException(getErrorMessage(ex));

        case UNPROCESSABLE_ENTITY -> throw new InvalidInputException(getErrorMessage(ex));

        default -> {
          LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
          LOG.warn("Error body: {}", ex.getResponseBodyAsString());
          throw ex;
        }
      }
    }
  }

  @Override
  public void deleteProduct(int productId) {

  }

  private String getErrorMessage(HttpClientErrorException ex) {
    try {
      return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    } catch (IOException ioEx) {
      return ex.getMessage();
    }
  }

  @Override
  public Recommendation createRecommendation(Recommendation body) {
    return null;
  }

  @Override
  public List<Recommendation> getRecommendations(int productId) {
    try {
      String url = recommendationServiceUrl + productId;

      LOG.debug("Will call getRecommendations API on URL: {}", url);
      List<Recommendation> recommendations = restTemplate
              .exchange(url, GET, null, new ParameterizedTypeReference<List<Recommendation>>() {
              })
              .getBody();

      LOG.debug("Found {} recommendations for a product with id: {}", recommendations.size(), productId);
      return recommendations;

    } catch (Exception ex) {
      LOG.warn("Got an exception while requesting recommendations, return zero recommendations: {}", ex.getMessage());
      return new ArrayList<>();
    }
  }

  @Override
  public void deleteRecommendation(int productId) {

  }

  @Override
  public Review createReview(Review body) {
    return null;
  }

  @Override
  public List<Review> getReviews(int productId) {
    try {
      String url = reviewServiceUrl + productId;

      LOG.debug("Will call getReviews API on URL: {}", url);
      List<Review> reviews = restTemplate
              .exchange(url, GET, null, new ParameterizedTypeReference<List<Review>>() {
              })
              .getBody();

      LOG.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
      return reviews;

    } catch (Exception ex) {
      LOG.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
      return new ArrayList<>();
    }
  }

  @Override
  public void deleteReview(int productId) {

  }

  private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
    switch (Objects.requireNonNull(HttpStatus.resolve(ex.getStatusCode().value()))) {

      case NOT_FOUND:
        return new NotFoundException(getErrorMessage(ex));

      case UNPROCESSABLE_ENTITY:
        return new InvalidInputException(getErrorMessage(ex));

      default:
        LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        LOG.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
  }
}
