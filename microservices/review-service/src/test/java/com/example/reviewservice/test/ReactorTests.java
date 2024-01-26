package com.example.reviewservice.test;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ReactorTests {
  @Test
  void reactorTest1() {
    String result = Mono.just("").map(s -> "123").block();

    Assertions.assertEquals(result, "123");
  }

  public Mono<String> nonBlockingMethod1sec(String data) {
    return Mono.just(data).delayElement(Duration.ofSeconds(1));
  }

  @Test
  void reactorTest2() {
    String result =
        nonBlockingMethod1sec("Hello world").flatMap(s -> nonBlockingMethod1sec(s)).block();

    Assertions.assertEquals(result, "Hello world");
  }

  List<Integer> collectTasks() {
    return new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
  }

  @Test
  void reactorTest3() {
    List<String> result =
        nonBlockingMethod1sec("Hello world")
            .flatMap(
                businessContext ->
                    Flux.fromIterable((collectTasks()))
                        .map(el -> businessContext + el)
                        .collectList())
            .block();

    System.out.println(Arrays.toString(result.toArray()));

    Assertions.assertEquals(collectTasks().size(), result.size());
  }

  @Test
  void reactorTest4() {
    List<String> result =
        nonBlockingMethod1sec("Hello world")
            .flatMap(
                context ->
                    Flux.fromIterable(collectTasks())
                        .flatMap(
                            task ->
                                Mono.deferContextual(
                                    reactiveContext -> {
                                      String hash =
                                          context + task + reactiveContext.get("requestId");
                                      return Mono.just(hash);
                                    }))
                        .collectList())
            .contextWrite(ctx -> ctx.put("requestId", UUID.randomUUID().toString()))
            .block();

    System.out.println(Arrays.toString(result.toArray()));
    Assertions.assertEquals(collectTasks().size(), result.size());
  }

  private final Logger logger = getLogger(ReactorTests.class);

  String doSomethingBlocking(String data) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return data;
  }

  Mono<String> doSomethingNonBlocking(String data) {
    return Mono.just(data).delayElement(Duration.ofSeconds(1));
  }

  Scheduler pool = Schedulers.newBoundedElastic(10, Integer.MAX_VALUE, "test-pool");

  @Test
  void reactorTest5() {
    AtomicInteger counter = new AtomicInteger(0);

    List<String> result =
        nonBlockingMethod1sec("hello world")
            .flatMap(
                _1 ->
                    Flux.fromIterable(collectTasks())
                        .parallel()
                        .runOn(pool, 1)
                        .flatMap(
                            task ->
                                Mono.deferContextual(
                                    _2 -> {
                                      return doSomethingNonBlocking(task.toString())
                                          .doOnRequest(
                                              _3 ->
                                                  logger.info(
                                                      "Added task in pool {}. task number {}",
                                                      counter.incrementAndGet(),
                                                      task))
                                          .doOnNext(
                                              _4 ->
                                                  logger.info(
                                                      "Non blocking code finished {}. task number {}",
                                                      counter.incrementAndGet(),
                                                      task))
                                          .map(
                                              it -> {
                                                logger.info(
                                                    "Blocking task add (remove action), task number {}",
                                                    task);
                                                return doSomethingBlocking(it);
                                              })
                                          .doOnNext(
                                              _5 ->
                                                  logger.info(
                                                      "Removed task from pool {}. task number {}",
                                                      counter.incrementAndGet(),
                                                      task));
                                    }),
                            true,
                            1,
                            1)
                        .sequential()
                        .collectList())
            .block();

    System.out.println(Arrays.toString(result.toArray()));
    Assertions.assertEquals(collectTasks().size(), result.size());
  }
}
