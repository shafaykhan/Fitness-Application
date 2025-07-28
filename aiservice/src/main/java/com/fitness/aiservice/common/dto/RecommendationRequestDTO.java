package com.fitness.aiservice.common.dto;

/*import com.fitness.activityservice.common.enums.ActivityTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;*/
import com.fitness.aiservice.common.enums.ActivityTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationRequestDTO {

  private String userId;
  private String activityId;
  private ActivityTypeEnum type;

  private String recommendation;

  private List<String> improvements;
  private List<String> suggestions;
  private List<String> safety;
}
