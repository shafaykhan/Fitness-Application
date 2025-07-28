package com.fitness.aiservice.recommendation;

import com.fitness.aiservice.common.model.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends MongoRepository<Recommendation, String> {

  List<Recommendation> findAllByUserId(String userId);

  Optional<Recommendation> findByActivityId(String activityId);
}
