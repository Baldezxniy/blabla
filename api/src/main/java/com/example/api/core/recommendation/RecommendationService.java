package com.example.api.core.recommendation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("v1/recommendation")
public interface RecommendationService {

  /**
   * @return the recommendations of the product
   */
  @GetMapping(produces = "application/json")
  List<Recommendation> getRecommendations(
          @RequestParam("productId") int productId
  );
}
