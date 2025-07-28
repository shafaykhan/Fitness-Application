package com.fitness.activityservice.common.dto;

import com.fitness.activityservice.common.enums.ActivityTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDTO {

  private String id;
  private String userId;

  private ActivityTypeEnum type;

  private Integer duration;
  private Integer caloriesBurned;
  private LocalDateTime startTime;

  private Map<String, Object> additionalMetrics;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
