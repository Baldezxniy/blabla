package com.example.productservice.persistence;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
  Mono<ProductEntity> findByProductId(int productId);

  @Modifying
  @Query("DELETE FROM products")
  Mono<Void> deleteAll();
}
