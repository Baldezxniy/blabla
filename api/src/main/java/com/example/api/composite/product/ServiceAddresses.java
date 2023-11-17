package com.example.api.composite.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServiceAddresses {
  private final String cmp;
  private final String pro;
  private final String rev;
  private final String rec;
}
