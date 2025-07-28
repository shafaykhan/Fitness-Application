package com.fitness.aiservice.ai_model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {

  private final WebClient webClient;

  @Value("${gemini.api.url}")
  private String url;
  @Value("${gemini.api.key}")
  private String key;

  public GeminiService(WebClient.Builder webClient) {
    this.webClient = webClient.build();
  }

  public String getPromptResponse(String prompt) {
    Map<String, Object> requestBody = Map.of("contents", new Object[]{
        Map.of("parts", new Object[]{
            Map.of("text", prompt)
        })
    });

    return webClient.post()
        .uri(url + key)
        .header("Content-Type", "application/json")
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(String.class)
        .block();
  }
}
