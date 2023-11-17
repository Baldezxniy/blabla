package com.example.reviewservice;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class MySqlTestBase {

  public static MySQLContainer database = new MySQLContainer("mysql:8.0.32");

  static {
    database.start();
  }

  @DynamicPropertySource
  static void databaseProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", ()-> database.getJdbcUrl());//database::getJdbcUrl);
    registry.add("spring.datasource.username", () -> database.getUsername());
    registry.add("spring.datasource.password", () -> database.getPassword());
  }
}
