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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.logging.Level.FINE;
import static org.springframework.http.HttpMethod.GET;
import static reactor.core.publisher.Flux.empty;

@Service
public class ProductCompositeIntegrationService implements ProductService, RecommendationService, ReviewService {

  private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegrationService.class);

  //  private final RestTemplate restTemplate;
  private final WebClient webClient;
  private final ObjectMapper objectMapper;

  public final String productServiceUrl;
  public final String recommendationServiceUrl;
  public final String reviewServiceUrl;

  public ProductCompositeIntegrationService(

          WebClient.Builder webClient, ObjectMapper objectMapper,

          @Value("${app.product-service.host}") String productServiceHost, @Value("${app.product-service.port}") int productServicePort,

          @Value("${app.recommendation-service.host}") String recommendationServiceHost, @Value("${app.recommendation-service.port}") int recommendationServicePort,

          @Value("${app.review-service.host}") String reviewServiceHost, @Value("${app.review-service.port}") int reviewServicePort

  ) {

    this.webClient = webClient.build();
    this.objectMapper = objectMapper;

    productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/v1/product";
    recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/v1/recommendation";
    reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/v1/review";
  }

  @Override
  public Mono<Product> createProduct(Product body) {
    try {
      String url = productServiceUrl;
      LOG.debug("Will post a new product to URL: {}", url);

      Product product = restTemplate.postForObject(url, body, Product.class);
      LOG.debug("Created a product with id: {}", product.getProductId());

      return product;
    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  @Override
  public Mono<Product> getProduct(int productId) {
    String url = productServiceUrl + "/" + productId;

    LOG.debug("Will call getProduct API on URL: {}", url);
    return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(Product.class)
            .log(LOG.getName(), FINE)
            .onErrorMap(WebClientResponseException.class, ex -> handleException(ex));
  }

  @Override
  public Mono<Void> deleteProduct(int productId) {
    try {
      String url = productServiceUrl + "/" + productId;
      LOG.debug("Will call the deleteProduct API on URL: {}", url);

      restTemplate.delete(url);

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  @Override
  public Mono<Recommendation> createRecommendation(Recommendation body) {
    try {
      String url = recommendationServiceUrl;
      LOG.debug("Will post a new product to URL: {}", url);

      Recommendation recommendation = restTemplate.postForObject(url, body, Recommendation.class);
      LOG.debug("Created a product with id: {}", recommendation.getProductId());

      return recommendation;

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  @Override
  public Flux<Recommendation> getRecommendations(int productId) {
    String url = recommendationServiceUrl + "?productId=" + productId;

    LOG.debug("Will call getRecommendations API on URL: {}", url);

    // Return an empty result if something goes wrong to make it possible for the composite service to return partial responses
    return webClient.get().uri(url).retrieve().bodyToFlux(Recommendation.class).log(LOG.getName(), FINE).onErrorResume(e -> empty());
  }

  @Override
  public Mono<Void> deleteRecommendations(int productId) {
    try {
      String url = recommendationServiceUrl + "?productId=" + productId;
      LOG.debug("Will call the deleteRecommendations API on URL: {}", url);

      restTemplate.delete(url);

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  @Override
  public Mono<Review> createReview(Review body) {
    try {
      String url = reviewServiceUrl;
      LOG.debug("Will post a new review to URL: {}", url);

      Review review = restTemplate.postForObject(url, body, Review.class);
      LOG.debug("Created a review with id: {}", review.getProductId());

      return review;

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  @Override
  public Flux<Review> getReviews(int productId) {
    String url = reviewServiceUrl + "?productId=" + productId;

    LOG.debug("Will call getReviews API on URL: {}", url);

    return webClient.get().uri(url).retrieve().bodyToFlux(Review.class).log(LOG.getName(), FINE).onErrorResume(e -> empty());
  }

  @Override
  public Mono<Void> deleteReviews(int productId) {
    try {
      String url = reviewServiceUrl + "?productId=" + productId;

      LOG.debug("Will call the deleteReviews API on URL: {}", url);
      restTemplate.delete(url);

    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }

  private Throwable handleException(Throwable ex) {

    if (!(ex instanceof WebClientResponseException)) {
      LOG.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
      return ex;
    }

    WebClientResponseException wcre = (WebClientResponseException) ex;

    switch (Objects.requireNonNull(HttpStatus.resolve(wcre.getStatusCode().value()))) {
      case NOT_FOUND, UNPROCESSABLE_ENTITY -> throw new InvalidInputException(getErrorMessage(wcre));

      default -> {
        LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
        LOG.warn("Error body: {}", wcre.getResponseBodyAsString());
        return ex;
      }
    }
  }

  private String getErrorMessage(WebClientResponseException ex) {
    try {
      return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    } catch (IOException ioEx) {
      return ex.getMessage();
    }
  }
}
