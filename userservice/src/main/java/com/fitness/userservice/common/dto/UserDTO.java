package com.fitness.userservice.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

  private UUID id;

  private String keycloakId;

  private String firstName;
  private String lastName;
  private String email;
  private String username;
  private String password;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
