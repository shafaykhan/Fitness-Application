package com.fitness.activityservice.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
public class UserWebClientService {

  private final WebClient userServiceWebClient;

  public UserWebClientService(WebClient userServiceWebClient) {
    this.userServiceWebClient = userServiceWebClient;
  }

  public Boolean validateUser(String userId) {
    log.info("Validating user with ID: {}", userId);
    try {
      Boolean isValid = userServiceWebClient.get()
          .uri("/api/users/{userId}/validate", userId)
          .retrieve()
          .bodyToMono(Boolean.class)
          .block();

      log.info("Validation result for userId {}: {}", userId, isValid);
      return isValid;

    } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        log.warn("User not found for userId: {}", userId);
        throw new RuntimeException("User not found!");
      } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
        log.warn("Bad request for userId: {}", userId);
        throw new RuntimeException("Invalid user ID!");
      }

    } catch (Exception e) {
      log.error("Error validating userId {}: {}", userId, e.getMessage(), e);
    }

    return false;
  }

}
