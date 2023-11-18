package com.example.productservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;

@SpringBootApplication
@ComponentScan("com.example")
public class ProductServiceApplication {
  private static final Logger LOG = LoggerFactory.getLogger(ProductServiceApplication.class);

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(ProductServiceApplication.class, args);

    String postgresHost = ctx.getEnvironment().getProperty("spring.datasource.url");
    LOG.info("Connected to PostgreSQL: " + postgresHost);
  }
}
