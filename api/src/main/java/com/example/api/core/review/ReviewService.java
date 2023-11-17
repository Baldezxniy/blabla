package com.example.api.core.review;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/v1/review")
public interface ReviewService {

  @GetMapping(produces = "application/json")
  List<Review> getReviews(@RequestParam("productId") int productId);
}
