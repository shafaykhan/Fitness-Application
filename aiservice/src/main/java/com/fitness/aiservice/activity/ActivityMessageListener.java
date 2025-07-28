package com.fitness.aiservice.activity;

import com.fitness.aiservice.common.dto.ActivityDTO;
import com.fitness.aiservice.common.model.Recommendation;
import com.fitness.aiservice.recommendation.RecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActivityMessageListener {

  private final ActivityAIService activityAIService;
  private final RecommendationService recommendationService;

  public ActivityMessageListener(ActivityAIService activityAIService, RecommendationService recommendationService) {
    this.activityAIService = activityAIService;
    this.recommendationService = recommendationService;
  }

  @RabbitListener(queues = "activity.queue")
  public void processActivity(ActivityDTO dto) {
    log.info("Received Activity for processing: {}", dto);
    Recommendation recommendation = activityAIService.generateRecommendation(dto);
    log.info("Generating Recommendation: {}", recommendation);
    recommendationService.saveOrUpdate(recommendation);
  }
}
