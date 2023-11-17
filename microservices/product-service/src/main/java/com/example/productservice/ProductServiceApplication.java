package com.example.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;

@SpringBootApplication
@ComponentScan("com.example")
public class ProductServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(ProductServiceApplication.class, args);
  }
}
