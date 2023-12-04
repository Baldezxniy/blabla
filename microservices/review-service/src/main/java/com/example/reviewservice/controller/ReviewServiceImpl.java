package com.example.reviewservice.controller;

import com.example.api.core.review.Review;
import com.example.api.core.review.ReviewService;
import com.example.api.exceptions.InvalidInputException;
import com.example.reviewservice.persistence.ReviewEntity;
import com.example.reviewservice.persistence.ReviewRepository;
import com.example.reviewservice.util.ReviewMapper;
import com.example.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static java.util.logging.Level.FINE;

@RestController
public class ReviewServiceImpl implements ReviewService {
  private final static Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

  private final ReviewRepository repository;
  private final ReviewMapper mapper;
  private final ServiceUtil serviceUtil;

  private final Scheduler jdbcScheduler;

  public ReviewServiceImpl(
          ReviewRepository repository,
          @Qualifier("reviewMapperImpl") ReviewMapper mapper,
          ServiceUtil serviceUtil,
          @Qualifier("jdbcScheduler") Scheduler jdbcScheduler
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
    this.jdbcScheduler = jdbcScheduler;
  }

  @Override
  public Mono<Review> createReview(Review body) {

    if (body.getProductId() < 1) {
      throw new InvalidInputException("Invalid productId: " + body.getProductId());
    }
    return Mono.fromCallable(() -> internalCreateReview(body))
            .subscribeOn(jdbcScheduler);
  }

  private Review internalCreateReview(Review body) {
    try {
      ReviewEntity entity = mapper.apiToEntity(body);
      ReviewEntity newEntity = repository.save(entity);

      LOG.debug("createReview: created a review entity: {}/{}", body.getProductId(), body.getReviewId());
      return mapper.entityToApi(newEntity);

    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId());
    }
  }

  @Override
  public Flux<Review> getReviews(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }


    return Mono.fromCallable(() -> internalGetReviews(productId))
            .flatMapMany(Flux::fromIterable)
            .log(LOG.getName(), FINE)
            .subscribeOn(jdbcScheduler);
  }

  private List<Review> internalGetReviews(int productId) {
    List<ReviewEntity> entityList = repository.findByProductId(productId);
    List<Review> list = mapper.entityListToApiList(entityList);
    list.forEach(review -> review.setServiceAddress(serviceUtil.getServiceAddress()));

    LOG.debug("Response size: {}", list.size());

    return list;
  }

  @Override
  public Mono<Void> deleteReviews(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    return Mono.fromRunnable(() -> internalDeleteReviews(productId)).subscribeOn(jdbcScheduler).then();
  }

  private void internalDeleteReviews(int productId) {

    LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);

    repository.deleteAll(repository.findByProductId(productId));
  }
}
