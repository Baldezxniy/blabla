package com.example.api.core.recommendation;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("v1/recommendation")
public interface RecommendationService {

  /**
   * Sample usage, see below.
   * <p>
   * curl -X POST $HOST:$PORT/v1/recommendation \
   * -H "Content-Type: application/json" --data \
   * '{"productId":123,"recommendationId":456,"author":"me","rate":5,"content":"yada, yada, yada"}'
   *
   * @param body a JSON representation of the new recommendation
   * @return A JSON representation of the newly created recommendation
   */
  @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
  Recommendation createRecommendation(@RequestBody Recommendation body);

  /**
   * Sample usage: "curl -X GET $HOST:$PORT/v1/recommendation?productId=1
   *
   * @return the recommendations of the product
   */
  @GetMapping(value = "/", produces = "application/json")
  List<Recommendation> getRecommendations(
          @RequestParam(value = "productId", required = true) int productId
  );

  /**
   * Sample usage: "curl -X DELETE $HOST:$PORT/v1/recommendation?productId=1".
   *
   * @param productId Id of the product
   */
  @DeleteMapping(value = "/", produces = "application/json")
  void deleteRecommendation(@RequestParam(value = "productId", required = true)  int productId);
}
