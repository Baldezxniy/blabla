package com.example.recommendationservice.controller;

import static java.util.logging.Level.FINE;

import com.example.api.core.recommendation.Recommendation;
import com.example.api.core.recommendation.RecommendationService;
import com.example.api.exceptions.InvalidInputException;
import com.example.recommendationservice.persistence.RecommendationEntity;
import com.example.recommendationservice.persistence.RecommendationRepository;
import com.example.recommendationservice.util.RecommendationMapper;
import com.example.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class RecommendationServiceImpl implements RecommendationService {
  private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);

  private final RecommendationRepository repository;
  private final RecommendationMapper mapper;
  private final ServiceUtil serviceUtil;

  public RecommendationServiceImpl(
      RecommendationRepository repository,
      @Qualifier("recommendationMapperImpl") RecommendationMapper mapper,
      ServiceUtil serviceUtil) {
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
  }

  @Override
  public Mono<Recommendation> createRecommendation(Recommendation body) {

    if (body.getProductId() < 1)
      throw new InvalidInputException("Invalid productId: " + body.getProductId());

    RecommendationEntity entity = mapper.apiToEntity(body);
    Mono<Recommendation> newEntity =
        repository
            .save(entity)
            .log(LOG.getName(), FINE)
            .onErrorMap(
                DataIntegrityViolationException.class,
                ex ->
              	      new InvalidInputException(
                        "Duplicate key, Product Id: "
                            + body.getProductId()
                            + ", Recommendation Id:"
                            + body.getRecommendationId()))
            .map(e -> mapper.entityToApi(e));

    return newEntity;
  }

  @Override
  public Flux<Recommendation> getRecommendations(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    LOG.info("Will get recommendations for product with id={}", productId);

    return repository
        .findAllByProductId(productId)
        .log(LOG.getName(), FINE)
        .map(entity -> mapper.entityToApi(entity))
        .map(api -> setServiceAddress(api));
  }

  @Override
  public Mono<Void> deleteRecommendations(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    LOG.debug(
        "deleteRecommendations: tries to delete recommendations for the product with productId: {}",
        productId);
    return repository.deleteAll(repository.findAllByProductId(productId));
  }

  private Recommendation setServiceAddress(Recommendation api) {
    api.setServiceAddress(serviceUtil.getServiceAddress());
    return api;
  }
}
