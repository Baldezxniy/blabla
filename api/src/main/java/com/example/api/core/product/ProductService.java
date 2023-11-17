package com.example.api.core.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/product")
public interface ProductService {

  /**
   * Sample usage: "curl $HOST:$PORT/product/1".
   *
   * @param productId if of the product
   * @return if product, if found else null
   */
  @GetMapping(value = "/{productId}", produces = "application/json")
  Product getProduct(@PathVariable("productId") int productId);
}
