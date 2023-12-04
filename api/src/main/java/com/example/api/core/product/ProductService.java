package com.example.api.core.product;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/v1/product")
public interface ProductService {

  /**
   * Sample usage, see below.
   * <p>
   * curl -X POST $HOST:$PORT/v1/product \
   * -H "Content-Type: application/json" --data \
   * '{"productId":123,"name":"product 123","weight":123}'
   *
   * @param body A JSON representation of the new product
   * @return A JSON representation of the newly created product
   */
  @PostMapping(produces = "application/json", consumes = "application/json")
  Mono<Product> createProduct(@RequestBody Product body);

  /**
   * Sample usage: "curl $HOST:$PORT/v1/product/1".
   *
   * @param productId if of the product
   * @return if product, if found else null
   */
  @GetMapping(value = "/{productId}", produces = "application/json")
  Mono<Product> getProduct(@PathVariable("productId") int productId);

  /**
   * Sample usage, see "curl -X DELETE $HOST:$PORT/v1/product/1"
   *
   * @param productId Id of the product
   */
  @DeleteMapping("/{productId}")
  Mono<Void> deleteProduct(@PathVariable("productId") int productId);
}
