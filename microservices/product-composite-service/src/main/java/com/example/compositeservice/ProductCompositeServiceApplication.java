package com.example.compositeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@ComponentScan("com.example")
public class ProductCompositeServiceApplication {
  private static final Logger LOG =
      LoggerFactory.getLogger(ProductCompositeServiceApplication.class);

  private final int threadPoolSize;
  private final int taskQueueSize;

  public ProductCompositeServiceApplication(
      @Value("${app.threadPoolSize:10}") Integer threadPoolSize,
      @Value("${app.taskQueueSize:100}") Integer taskQueueSize) {
    this.threadPoolSize = threadPoolSize;
    this.taskQueueSize = taskQueueSize;
  }

  @Bean
  public Scheduler publishEventScheduler() {
    LOG.info("Creates a messagingScheduler with connectionPoolSize = {}", threadPoolSize);
    return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "publish-pool");
  }

  public static void main(String[] args) {
    SpringApplication.run(ProductCompositeServiceApplication.class, args);
  }
}
