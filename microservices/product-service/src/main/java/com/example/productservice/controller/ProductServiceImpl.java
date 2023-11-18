package com.example.productservice.controller;

import com.example.api.core.product.Product;
import com.example.api.core.product.ProductService;
import com.example.api.exceptions.InvalidInputException;
import com.example.api.exceptions.NotFoundException;
import com.example.productservice.persistence.ProductEntity;
import com.example.productservice.persistence.ProductRepository;
import com.example.productservice.util.ProductMapper;
import com.example.productservice.util.ProductMapperImpl;
import com.example.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;

/**
 * @RequectMapping "/v1/product"
 */
@RestController
public class ProductServiceImpl implements ProductService {

  private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

  private final ServiceUtil serviceUtil;
  public final ProductMapper mapper;
  private final ProductRepository repository;


  public ProductServiceImpl(ServiceUtil serviceUtil, ProductMapperImpl mapper, ProductRepository repository) {
    this.serviceUtil = serviceUtil;
    this.mapper = mapper;
    this.repository = repository;
  }

  @Override
  public Product createProduct(Product body) {
    try {
      ProductEntity entity = mapper.apiToEntity(body);
      ProductEntity newEntity = repository.save(entity);

      LOG.debug("createProduct: entity created for productId: {}", body.getProductId());

      return mapper.entityToApi(newEntity);
    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId());
    }
  }

  @Override
  public Product getProduct(int productId) {
    if (productId < 1)
      throw new InvalidInputException("Invalid productId: " + productId);

    ProductEntity entity = repository.findByProductId(productId)
            .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));

    Product response = mapper.entityToApi(entity);
    response.setServiceAddress(serviceUtil.getServiceAddress());

    return response;
  }

  @Override
  public void deleteProduct(int productId) {
    repository.findByProductId(productId).ifPresent(repository::delete);
  }
}
