package com.fitness.activityservice.activity;

import com.fitness.activityservice.common.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {

  List<Activity> findAllByUserId(String userId);
}
