package com.example.productservice.persistence;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class ProductEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private int version;

  @Column(name = "product_id", unique = true)
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
