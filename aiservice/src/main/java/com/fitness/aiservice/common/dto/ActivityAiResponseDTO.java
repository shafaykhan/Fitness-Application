package com.fitness.aiservice.common.dto;

import com.fitness.aiservice.common.model.Recommendation;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActivityAiResponseDTO {
  private Analysis analysis;
  private List<Improvement> improvements;
  private List<Suggestion> suggestions;
  private List<String> safety;

  public Recommendation prepareRecommendation(ActivityDTO dto) {
    return Recommendation.builder()
        .userId(dto.getUserId())
        .activityId(dto.getId())
        .type(dto.getType())
        .recommendation(
            "overall: " + analysis.getOverall() + "\n\n" +
            "pace: " + analysis.getPace() + "\n\n" +
            "heartRate: " + analysis.getHeartRate() + "\n\n" +
            "caloriesBurned: " + analysis.getCaloriesBurned() + "\n\n"
        )
        .improvements(improvements.stream()
            .map(improvement -> improvement.getArea() + ": " + improvement.getRecommendation())
            .toList())
        .suggestions(suggestions.stream()
            .map(suggestion -> suggestion.getWorkout() + ": " + suggestion.getDescription())
            .toList())
        .safety(safety)
        .createdAt(LocalDateTime.now())
        .build();
  }
}

@Data
class Analysis {
  private String overall;
  private String pace;
  private String heartRate;
  private String caloriesBurned;
}

@Data
class Improvement {
  private String area;
  private String recommendation;
}

@Data
class Suggestion {
  private String workout;
  private String description;
}
