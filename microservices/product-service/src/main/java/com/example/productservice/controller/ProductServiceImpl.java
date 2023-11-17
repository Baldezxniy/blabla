package com.example.productservice.controller;

import com.example.api.core.product.Product;
import com.example.api.core.product.ProductService;
import com.example.api.exceptions.InvalidInputException;
import com.example.api.exceptions.NotFoundException;
import com.example.util.http.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;

/**
 * @RequectMapping "/v1/product"
 */
@RestController
public class ProductServiceImpl implements ProductService {

  private final ServiceUtil serviceUtil;

  public ProductServiceImpl(ServiceUtil serviceUtil) {
    this.serviceUtil = serviceUtil;
  }

  /**
   * @GetMapping "/{productId}"
   */
  @Override
  public Product getProduct(int productId) {
    if (productId < 1)
      throw new InvalidInputException("Invalid productId: " + productId);

    if (productId == 13)
      throw new NotFoundException("No product found for productId: " + productId);

    return new Product(productId, "name-" + productId, 123, serviceUtil.getServiceAddress());
  }
}
