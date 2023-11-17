package com.example.productservice.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Long>, CrudRepository<ProductEntity, Long> {
  Optional<ProductEntity> findByProductId(int productId);
}
