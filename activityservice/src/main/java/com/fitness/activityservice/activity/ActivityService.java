package com.fitness.activityservice.activity;

import com.fitness.activityservice.common.dto.ActivityDTO;
import com.fitness.activityservice.common.dto.ActivityRequestDTO;
import com.fitness.activityservice.common.model.Activity;
import com.fitness.activityservice.user.UserWebClientService;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

  private final ActivityRepository repository;
  private final ModelMapper modelMapper;
  private final UserWebClientService userWebClientService;
  private final RabbitTemplate rabbitTemplate;

  @Value("${rabbitmq.exchange.name}")
  private String exchange;
  @Value("${rabbitmq.routing.key}")
  private String routingKey;

  public ActivityService(ActivityRepository repository,
                         ModelMapper modelMapper,
                         UserWebClientService userWebClientService,
                         RabbitTemplate rabbitTemplate) {
    this.repository = repository;
    this.modelMapper = modelMapper;
    this.userWebClientService = userWebClientService;
    this.rabbitTemplate = rabbitTemplate;
  }

  public ActivityDTO saveOrUpdate(ActivityRequestDTO dto) {
    if (!userWebClientService.validateUser(dto.getUserId()))
      throw new RuntimeException("User not found!");

    Activity activity = new Activity();

    if (dto.getId() != null)
      activity = findEntityById(dto.getId());

    modelMapper.map(dto, activity);
    Activity savedActivity = repository.save(activity);

    try {
      rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
    } catch (Exception e) {
      throw new RuntimeException("Failed to publish Activity to RabbitMQ");
    }

    return modelMapper.map(savedActivity, ActivityDTO.class);
  }

  public ActivityDTO findById(String id) {
    Activity entity = findEntityById(id);
    return modelMapper.map(entity, ActivityDTO.class);
  }

  public List<ActivityDTO> findAllByUserId(String userId) {
    return repository.findAllByUserId(userId).stream()
        .map(activity -> modelMapper.map(activity, ActivityDTO.class))
        .toList();
  }

  private Activity findEntityById(String id) {
    return repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Activity not found!"));
  }
}
