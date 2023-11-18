package com.example.api.core.product;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
  private int productId;
  private String name;
  private int weight;
  private String serviceAddress;
}
