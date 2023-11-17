package com.example.recommendationservice.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "recommendations", indexes = {
        @Index(name = "idx_recommendation_product_id_recommendation_id", columnList = "product_id,recommendation_id", unique = true)
})
public class RecommendationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Version
  private int version;

  @Column(name = "product_id")
  private int productId;
  @Column(name = "recommendation_id")
  private int recommendationId;
  private String author;
  private int rating;
  private String content;

  public RecommendationEntity() {
  }

  public RecommendationEntity(int productId, int recommendationId, String author, int rating, String content) {
    this.productId = productId;
    this.recommendationId = recommendationId;
    this.author = author;
    this.rating = rating;
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  public Integer getVersion() {
    return version;
  }

  public int getProductId() {
    return productId;
  }

  public int getRecommendationId() {
    return recommendationId;
  }

  public String getAuthor() {
    return author;
  }

  public int getRating() {
    return rating;
  }

  public String getContent() {
    return content;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public void setRecommendationId(int recommendationId) {
    this.recommendationId = recommendationId;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
