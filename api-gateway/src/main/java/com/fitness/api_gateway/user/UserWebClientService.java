package com.fitness.api_gateway.user;

import com.fitness.api_gateway.user.dto.UserRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserWebClientService {

  private final WebClient userServiceWebClient;

  public UserWebClientService(WebClient userServiceWebClient) {
    this.userServiceWebClient = userServiceWebClient;
  }

  public Mono<Boolean> validateUser(String keycloakId) {
    log.info("Validating user with Keycloak ID: {}", keycloakId);
    return userServiceWebClient.get()
        .uri("/api/users/by-keycloak/{keycloakId}/validate", keycloakId)
        .retrieve()
        .bodyToMono(Boolean.class)
        .onErrorResume(WebClientResponseException.class, e -> {
          if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            log.warn("User not found for keycloakId: {}", keycloakId);
            throw new RuntimeException("User not found!");
          } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
            log.warn("Bad request for keycloakId: {}", keycloakId);
            throw new RuntimeException("Invalid Keycloak ID!");
          }
          return Mono.error(new RuntimeException("Error validating Keycloak ID!"));
        });
  }

  public Mono<UserRequestDTO> registerUser(UserRequestDTO dto) {
    log.info("Calling User Registration API for email: {}", dto.getEmail());

    return userServiceWebClient.post()
        .uri("/api/users")
        .bodyValue(dto)
        .retrieve()
        .bodyToMono(UserRequestDTO.class)
        .onErrorResume(WebClientResponseException.class, e -> {
          if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
            return Mono.error(new RuntimeException("Bad Request: " + e.getMessage()));
          else if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
            return Mono.error(new RuntimeException("Internal Server Error: " + e.getMessage()));
          return Mono.error(new RuntimeException("Unexpected error: " + e.getMessage()));
        });
  }
}
