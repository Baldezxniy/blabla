package com.example.recommendationservice.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends CrudRepository<RecommendationEntity, Long> {
  List<RecommendationEntity> findAllByProductId(int productId);
}
