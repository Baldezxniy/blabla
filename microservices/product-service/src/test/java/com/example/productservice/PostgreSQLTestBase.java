package com.example.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;


public abstract class PostgreSQLTestBase {
  private static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres");

  static {
    database.start();
  }

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", database::getJdbcUrl);
    registry.add("spring.datasource.username", database::getUsername);
    registry.add("spring.datasource.password", database::getPassword);
  }

  @Test
  void test() {
    assertThat(database.isRunning()).isTrue();
  }
}