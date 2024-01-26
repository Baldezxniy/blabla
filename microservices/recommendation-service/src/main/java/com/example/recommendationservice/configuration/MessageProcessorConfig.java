package com.example.recommendationservice.configuration;

import com.example.api.core.recommendation.Recommendation;
import com.example.api.core.recommendation.RecommendationService;
import com.example.api.event.Event;
import com.example.api.exceptions.EventProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MessageProcessorConfig {
  private final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

  private final RecommendationService recommendationService;

  public MessageProcessorConfig(RecommendationService recommendationService) {
    this.recommendationService = recommendationService;
  }

  @Bean
  public Consumer<Event<Integer, Recommendation>> messageProcessor() {
    return event -> {

		Recommendation recommendation1 = event.getData();
		System.out.println("RECOMMENDATION: " + recommendation1.getAuthor());

		LOG.info("Processor message created at {}...", event.getEventCreatedAt());

      switch (event.getEventType()) {
        case CREATE -> {
          Recommendation recommendation = event.getData();
          System.out.println("RECOMMENDATION: " + recommendation.getAuthor());
          LOG.info(
              "Create recommendation with ID: {}/{}",
              recommendation.getProductId(),
              recommendation.getRecommendationId());
          recommendationService.createRecommendation(recommendation).block();
        }
        case DELETE -> {
          int productId = event.getKey();
          LOG.info("Delete recommendations with ProductID: {}", productId);
          recommendationService.deleteRecommendations(productId).block();
        }
        default -> {
          String errorMessage =
              "Incorrect event type: "
                  + event.getEventType()
                  + ", expected a CREATE or DELETE event";
          LOG.warn(errorMessage);
          throw new EventProcessingException(errorMessage);
        }
      }
      LOG.info("Message processing done!");
    };
  }
}
