package com.fitness.activityservice.common.dto;

import com.fitness.activityservice.common.enums.ActivityTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequestDTO {

  private String id;

  @NotBlank(message = "UserId is required!")
  private String userId;

  @NotNull(message = "type is required!")
  private ActivityTypeEnum type;

  private Integer duration;
  private Integer caloriesBurned;
  private LocalDateTime startTime;

  private Map<String, Object> additionalMetrics;
}
