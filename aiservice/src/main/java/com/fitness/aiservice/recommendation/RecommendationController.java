package com.fitness.aiservice.recommendation;

import com.fitness.aiservice.common.dto.RecommendationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ai-recommendations")
public class RecommendationController {

  private final RecommendationService service;

  public RecommendationController(RecommendationService service) {
    this.service = service;
  }

  @GetMapping("/by-user/{userId}")
  public ResponseEntity<List<RecommendationDTO>> findAllByUserId(@PathVariable String userId) {
    return ResponseEntity.ok(service.findAllByUserId(userId));
  }

  @GetMapping("/by-activity/{activityId}")
  public ResponseEntity<RecommendationDTO> findByActivityId(@PathVariable String activityId) {
    return ResponseEntity.ok(service.findByActivityId(activityId));
  }
}
