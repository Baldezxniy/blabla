package com.example.productservice.persistence;

import jakarta.annotation.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "products")
public class ProductEntity {
  @Id
  private Long id;
  @Version()
  private int version;
  @Column("product_id")
  @Nullable
  private int productId;

  private String name;
  private int weight;

  public ProductEntity() {
  }

  public ProductEntity(int productId, String name, int weight) {
    this.productId = productId;
    this.name = name;
    this.weight = weight;
  }

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }
}
