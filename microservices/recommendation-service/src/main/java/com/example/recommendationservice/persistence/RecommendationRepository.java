package com.example.recommendationservice.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface RecommendationRepository extends ReactiveCrudRepository<RecommendationEntity, Long> {
  Flux<RecommendationEntity> findAllByProductId(int productId);
}
