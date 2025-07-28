package com.fitness.api_gateway.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

  private UUID id;

  private String keycloakId;

  @NotBlank(message = "Username is required!")
  private String username;

  private String firstName;
  private String lastName;

  @NotBlank(message = "Email is required!")
  @Email(message = "Invalid email format!")
  private String email;

  @NotBlank(message = "Password is required!")
  @Size(min = 6, message = "Password must have at least 6 Characters!")
  private String password;
}
