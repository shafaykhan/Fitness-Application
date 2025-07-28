package com.fitness.api_gateway.common.filter;

import com.fitness.api_gateway.user.dto.UserRequestDTO;
import com.fitness.api_gateway.user.UserWebClientService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class KeycloakUserSyncFilter implements WebFilter {

  private final UserWebClientService userWebClientService;

  public KeycloakUserSyncFilter(UserWebClientService userWebClientService) {
    this.userWebClientService = userWebClientService;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String token = exchange.getRequest().getHeaders().getFirst("Authorization");
    String keycloakId = exchange.getRequest().getHeaders().getFirst("X-Keycloak-Id");

    UserRequestDTO userRequestDTO = prepareUserRequestDTO(token);

    if (keycloakId == null) {
      keycloakId = userRequestDTO.getKeycloakId();
    }

    if (keycloakId != null && token != null) {
      String finalUserId = keycloakId;

      return userWebClientService.validateUser(keycloakId)
          .flatMap(exist -> {
            if (!exist) {
              if (userRequestDTO != null) {
                return userWebClientService.registerUser(userRequestDTO)
                    .then(Mono.empty());
              } else {
                return Mono.empty();
              }
            } else {
              log.info("User already exist, Skipping sync.");
              return Mono.empty();
            }
          })
          .then(Mono.defer(() -> {
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-Keycloak-Id", finalUserId)
                .build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
          }));
    }
    return chain.filter(exchange);
  }

  private UserRequestDTO prepareUserRequestDTO(String token) {
    try {
      String tokenWithoutBearer = token.replace("Bearer ", "").trim();
      SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
      JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

      return UserRequestDTO.builder()
          .keycloakId(claims.getStringClaim("sub"))
          .username(claims.getStringClaim("preferred_username"))
          .firstName(claims.getStringClaim("given_name"))
          .lastName(claims.getStringClaim("family_name"))
          .email(claims.getStringClaim("email"))
          .password(claims.getStringClaim("preferred_username") + "@123")
          .build();
    } catch (Exception e) {
      log.error("Failed to parse JWT and extract user claims: {}", e.getMessage(), e);
      throw new RuntimeException("Unable to extract user details from token");
    }
  }
}
