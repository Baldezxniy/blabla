package com.example.recommendationservice;

import com.example.recommendationservice.persistence.RecommendationEntity;
import com.example.recommendationservice.persistence.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@DataR2dbcTest
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersistenceTests extends PostgreSQLTestBase {
  @Autowired
  RecommendationRepository repository;

  private RecommendationEntity savedEntity;

  @BeforeEach
  void setupDb() {
    StepVerifier.create(repository.deleteAll()).verifyComplete();

    RecommendationEntity entity = new RecommendationEntity(1, 2, "a", 3, "c");
    StepVerifier.create(repository.save(entity)).expectNextMatches(createdEntity -> {
      savedEntity = createdEntity;
      return areProductEqual(entity, savedEntity);
    }).verifyComplete();
  }


  @Test
  void create() {
    RecommendationEntity newEntity = new RecommendationEntity(1, 3, "a", 3, "c");

    StepVerifier.create(repository.save(newEntity)).expectNextMatches(createdEntity -> newEntity.getProductId() == createdEntity.getProductId()).verifyComplete();

    StepVerifier.create(repository.findById(newEntity.getId())).expectNextMatches(foundEntity -> areProductEqual(newEntity, foundEntity)).verifyComplete();

    StepVerifier.create(repository.count()).expectNext(2L).verifyComplete();
  }

  @Test
  void update() {

    // Version 1 -> 2
    savedEntity.setAuthor("a2");
    StepVerifier.create(repository.save(savedEntity)).expectNextMatches(updatedEntity -> updatedEntity.getAuthor().equals("a2")).verifyComplete();

    StepVerifier.create(repository.findById(savedEntity.getId())).expectNextMatches(foundEntity -> foundEntity.getVersion() == 2 && foundEntity.getAuthor().equals("a2")).verifyComplete();

    // Version 2 -> 3
    savedEntity.setAuthor("a3");
    StepVerifier.create(repository.save(savedEntity)).expectNextMatches(updatedEntity -> updatedEntity.getAuthor().equals("a3")).verifyComplete();

    StepVerifier.create(repository.findById(savedEntity.getId())).expectNextMatches(foundEntity -> foundEntity.getVersion() == 3 && foundEntity.getAuthor().equals("a3")).verifyComplete();
  }

  @Test
  void delete() {
    StepVerifier.create(repository.delete(savedEntity)).verifyComplete();
    StepVerifier.create(repository.existsById(savedEntity.getId())).expectNext(false).verifyComplete();
  }

  @Test
  void getByProductId() {
    StepVerifier.create(repository.findAllByProductId(savedEntity.getProductId())).expectNextMatches(actualEntity -> areProductEqual(savedEntity, actualEntity)).verifyComplete();
  }

  @Test
  void duplicateError() {
    RecommendationEntity entity = new RecommendationEntity(1, 2, "a", 3, "c");

    StepVerifier.create(repository.save(entity)).expectError(DataIntegrityViolationException.class).verify();
  }

  @Test
  void optimisticLockError() {

    // Store the saved entity in two separate entity objects
    RecommendationEntity entity1 = repository.findById(savedEntity.getId()).block();
    RecommendationEntity entity2 = repository.findById(savedEntity.getId()).block();

    // Update the entity using the first entity object
    entity1.setAuthor("a1");
    repository.save(entity1).block();

    //  Update the entity using the second entity object.
    // This should fail since the second entity now holds an old version number, i.e. an Optimistic Lock Error
    StepVerifier.create(repository.save(entity2)).expectError(OptimisticLockingFailureException.class).verify();

    // Get the updated entity from the database and verify its new sate
    StepVerifier.create(repository.findById(entity1.getId())).expectNextMatches(foundEntity -> foundEntity.getVersion() == 2 && foundEntity.getAuthor().equals("a1")).verifyComplete();
  }

  private void assertEqualsRecommendation(RecommendationEntity expectedEntity, RecommendationEntity actualEntity) {
    assertEquals(expectedEntity.getId(), actualEntity.getId());
    assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
    assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
    assertEquals(expectedEntity.getRecommendationId(), actualEntity.getRecommendationId());
    assertEquals(expectedEntity.getAuthor(), actualEntity.getAuthor());
    assertEquals(expectedEntity.getRating(), actualEntity.getRating());
    assertEquals(expectedEntity.getContent(), actualEntity.getContent());
  }

  private boolean areProductEqual(RecommendationEntity expectedEntity, RecommendationEntity actualEntity) {
    return (expectedEntity.getId().equals(actualEntity.getId())) && (expectedEntity.getProductId() == actualEntity.getProductId()) && (expectedEntity.getRecommendationId() == actualEntity.getRecommendationId()) && (expectedEntity.getAuthor().equals(actualEntity.getAuthor())) && (expectedEntity.getRating() == actualEntity.getRating()) && (expectedEntity.getContent().equals(actualEntity.getContent()));
  }
}
