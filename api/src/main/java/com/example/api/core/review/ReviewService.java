package com.example.api.core.review;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequestMapping("/v1/review")
public interface ReviewService {
  /**
   * Sample usage, see below
   *
   * curl -X POST $HOST:$PORT/v1/review \
   *    -H "Content-Type": application/json" --data \
   *   '{"productId":123,"reviewId":456,"author":"me","subject":"yada, yada, yada","content":"yada, yada, yada"}'
   *
   * @param body A JSON representation of the new review
   * @return A JSON representation of the newly created review
   */
  @PostMapping(produces = "application/json", consumes = "application/json")
  Mono<Review> createReview(@RequestBody Review body);

  /**
   * Sample usage: "curl $HOST:$PORT/v1/review?productId=1".
   *
   * @param productId Id of the product
   * @return the reviews  of the product
   */
  @GetMapping(produces = "application/json")
  Flux<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);

  /**
   * Sample usage: "curl -X DELETE $HOST:$PORT/review?productId=1".
   *
   * @param productId Id of the product
   */
  @DeleteMapping
  Mono<Void> deleteReviews(@RequestParam(value = "productId", required = true) int productId);

}
