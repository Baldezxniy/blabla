package com.example.recommendationservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class RecommendationServiceApplicationTests {

  @Autowired
  private WebTestClient client;

  @Test
  void getRecommendationsByProductId() {

    int productId = 1;

    client.get()
            .uri("v1/recommendation?productId=" + productId)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.length()").isEqualTo(3)
            .jsonPath("$[0].productId").isEqualTo(productId);
  }

  @Test
  void getRecommendationsMissingParameter() {

    client.get()
            .uri("v1/recommendation")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(BAD_REQUEST)
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.path").isEqualTo("/v1/recommendation")
            .jsonPath("$.message").doesNotExist(); //.isEqualTo("Required query parameter 'productId' is not present.");
  }

  @Test
  void getRecommendationsInvalidParameter() {

    client.get()
            .uri("v1/recommendation?productId=no-integer")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(BAD_REQUEST)
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.path").isEqualTo("/v1/recommendation")
            .jsonPath("$.message").doesNotExist();//isEqualTo("Type mismatch.");
  }

  @Test
  void getRecommendationsNotFound() {

    int productIdNotFound = 113;

    client.get()
            .uri("v1/recommendation?productId=" + productIdNotFound)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.length()").isEqualTo(0);
  }

  @Test
  void getRecommendationsInvalidParameterNegativeValue() {

    int productIdInvalid = -1;

    client.get()
            .uri("v1/recommendation?productId=" + productIdInvalid)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.path").isEqualTo("/v1/recommendation")
            .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
  }
}