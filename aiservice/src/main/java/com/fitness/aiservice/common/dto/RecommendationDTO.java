package com.fitness.aiservice.common.dto;

import com.fitness.aiservice.common.enums.ActivityTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationDTO {

  private String id;
  private String userId;
  private String activityId;
  private ActivityTypeEnum type;

  private String recommendation;

  private List<String> improvements;
  private List<String> suggestions;
  private List<String> safety;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
