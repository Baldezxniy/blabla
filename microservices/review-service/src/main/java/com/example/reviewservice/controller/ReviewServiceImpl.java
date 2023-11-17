package com.example.reviewservice.controller;

import com.example.api.core.review.Review;
import com.example.api.core.review.ReviewService;
import com.example.api.exceptions.InvalidInputException;
import com.example.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @RequestMapping "v1/review"
 */
@RestController
public class ReviewServiceImpl implements ReviewService {

  private final static Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

  private final ServiceUtil serviceUtil;


  public ReviewServiceImpl(ServiceUtil serviceUtil) {
    this.serviceUtil = serviceUtil;
  }

  /**
   * @param productId @RequestParam("productId")
   * @GetMapping "/"
   */
  @Override
  public List<Review> getReviews(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    if (productId == 113) {
      LOG.debug("No recommendations found for productId: {}", productId);
      return new ArrayList<>();
    }

    List<Review> list = new ArrayList<>();
    list.add(new Review(productId, 1, "Author 1", "Subject 1", "Content 1", serviceUtil.getServiceAddress()));
    list.add(new Review(productId, 2, "Author 2", "Subject 2", "Content 2", serviceUtil.getServiceAddress()));
    list.add(new Review(productId, 3, "Author 3", "Subject 3", "Content 3", serviceUtil.getServiceAddress()));

    LOG.debug("/reviews response size: {}", list.size());

    return list;
  }
}
