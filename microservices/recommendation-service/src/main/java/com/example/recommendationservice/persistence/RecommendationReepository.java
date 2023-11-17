package com.example.recommendationservice.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecommendationReepository extends CrudRepository<RecommendationEntity, String> {
  List<RecommendationEntity> findAllByProductId(int productId);
}
