package com.fitness.userservice.user;

import com.fitness.userservice.common.dto.UserRequestDTO;
import com.fitness.userservice.common.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid UserRequestDTO dto) {
    return ResponseEntity.ok(service.registerUser(dto));
  }

  @PutMapping
  public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserRequestDTO dto) {
    return ResponseEntity.ok(service.updateUser(dto));
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @GetMapping("/by-keycloak/{keycloakId}")
  public ResponseEntity<UserDTO> findByKeycloakId(@PathVariable String keycloakId) {
    return ResponseEntity.ok(service.findByKeycloakId(keycloakId));
  }

  @GetMapping("/{id}/validate")
  public ResponseEntity<Boolean> validateUserById(@PathVariable UUID id) {
    return ResponseEntity.ok(service.existsById(id));
  }

  @GetMapping("/by-keycloak/{keycloakId}/validate")
  public ResponseEntity<Boolean> validateUserByKeycloakId(@PathVariable String keycloakId) {
    return ResponseEntity.ok(service.existsByKeycloakId(keycloakId));
  }
}
