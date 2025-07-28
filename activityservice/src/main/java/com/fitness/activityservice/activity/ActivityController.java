package com.fitness.activityservice.activity;

import com.fitness.activityservice.common.dto.ActivityDTO;
import com.fitness.activityservice.common.dto.ActivityRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

  private final ActivityService service;

  public ActivityController(ActivityService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<ActivityDTO> saveOrUpdate(@RequestBody @Valid ActivityRequestDTO dto){
    return ResponseEntity.ok(service.saveOrUpdate(dto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ActivityDTO> findById(@PathVariable String id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @GetMapping("/by-user/{userId}")
  public ResponseEntity<List<ActivityDTO>> findAllByUserId(@PathVariable String userId) {
    return ResponseEntity.ok(service.findAllByUserId(userId));
  }
}
