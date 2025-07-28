package com.fitness.aiservice.recommendation;

import com.fitness.aiservice.common.dto.RecommendationDTO;
import com.fitness.aiservice.common.model.Recommendation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

  private final RecommendationRepository repository;
  private final ModelMapper modelMapper;

  public RecommendationService(RecommendationRepository repository, ModelMapper modelMapper) {
    this.repository = repository;
    this.modelMapper = modelMapper;
  }

  public void saveOrUpdate(Recommendation payload) {
    repository.findByActivityId(payload.getActivityId())
        .ifPresent(recommendation -> payload.setId(recommendation.getId()));
    repository.save(payload);
  }

  public List<RecommendationDTO> findAllByUserId(String userId) {
    return repository.findAllByUserId(userId).stream()
        .map(recommendation -> modelMapper.map(recommendation, RecommendationDTO.class))
        .toList();
  }

  public RecommendationDTO findByActivityId(String activityId) {
    return repository.findByActivityId(activityId)
        .map(recommendation -> modelMapper.map(recommendation, RecommendationDTO.class))
        .orElseThrow(() -> new RuntimeException("Activity not found!"));
  }
}
