package com.example.recommendationservice.util;

import com.example.api.core.recommendation.Recommendation;
import com.example.recommendationservice.persistence.RecommendationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface RecommendationMapper {

  @Mappings({
          @Mapping(target = "rate", source = "entity.rating"),
          @Mapping(target = "serviceAddress", ignore = true)
  })
  Recommendation entityToApi(RecommendationEntity entity);

  @Mappings({
          @Mapping(target = "rating", source = "api.rate"),
          @Mapping(target = "id", ignore = true),
          @Mapping(target = "version", ignore = true)
  })
  RecommendationEntity apiToEntity(Recommendation api);

  List<Recommendation> entityListToApiList(List<RecommendationEntity> entity);

  List<RecommendationEntity> apiListToEntityList(List<Recommendation> api);
}
