package com.example.recommendationservice.controller;

import com.example.api.core.recommendation.Recommendation;
import com.example.api.core.recommendation.RecommendationService;
import com.example.api.exceptions.InvalidInputException;
import com.example.recommendationservice.persistence.RecommendationEntity;
import com.example.recommendationservice.persistence.RecommendationRepository;
import com.example.recommendationservice.util.RecommendationMapper;
import com.example.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecommendationServiceImpl implements RecommendationService {
  private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);

  private final RecommendationRepository repository;
  private final RecommendationMapper mapper;
  private final ServiceUtil serviceUtil;

  public RecommendationServiceImpl(RecommendationRepository repository, RecommendationMapper mapper, ServiceUtil serviceUtil) {
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
  }

  @Override
  public Recommendation createRecommendation(Recommendation body) {
    try {
      RecommendationEntity entity = mapper.apiToEntity(body);
      RecommendationEntity newEntity = repository.save(entity);

      LOG.debug("createRecommendation: created a recommendation entity: {}/{}", body.getProductId(), body.getRecommendationId());
      return mapper.entityToApi(newEntity);

    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId());
    }
  }

  @Override
  public List<Recommendation> getRecommendations(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    List<RecommendationEntity> entityList = repository.findAllByProductId(productId);
    List<Recommendation> list = mapper.entityListToApiList(entityList);
    list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

    LOG.debug("/recommendation response size: {}", list.size());

    return list;
  }

  @Override
  public void deleteRecommendation(int productId) {
    LOG.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);
    repository.deleteAll(repository.findAllByProductId(productId));
  }
}
