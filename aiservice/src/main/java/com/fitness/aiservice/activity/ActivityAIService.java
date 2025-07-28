package com.fitness.aiservice.activity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.ai_model.GeminiService;
import com.fitness.aiservice.common.dto.ActivityAiResponseDTO;
import com.fitness.aiservice.common.dto.ActivityDTO;
import com.fitness.aiservice.common.model.Recommendation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ActivityAIService {

  private final GeminiService geminiService;

  public ActivityAIService(GeminiService geminiService) {
    this.geminiService = geminiService;
  }

  public Recommendation generateRecommendation(ActivityDTO dto) {
    String prompt = createPromptForActivity(dto);
    String aiResponse = geminiService.getPromptResponse(prompt);
    log.info("RESPONSE FROM AI: {}", aiResponse);
    return processAiResponse(dto, aiResponse);
  }

  private Recommendation processAiResponse(ActivityDTO dto, String aiResponse) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode rootNode = mapper.readTree(aiResponse);

      JsonNode textNode = rootNode.path("candidates")
          .get(0)
          .path("content")
          .path("parts")
          .get(0)
          .path("text");

      String jsonContent = textNode.asText()
          .replaceAll("```json\\n", "")
          .replaceAll("\\n```", "")
          .trim();

      return prepareRecommendationFromJsonContentV1(dto, mapper, jsonContent);
    } catch (Exception e) {
      log.error("Error occurred while converting JSON String into Recommendation Object: ", e);
      return createDefaultRecommendation(dto);
    }
  }

  private Recommendation prepareRecommendationFromJsonContentV1(ActivityDTO dto, ObjectMapper mapper,
                                                                String jsonContent) throws JsonProcessingException {
    ActivityAiResponseDTO responseDTO = mapper.readValue(jsonContent, ActivityAiResponseDTO.class);
    return responseDTO.prepareRecommendation(dto);
  }

  private Recommendation prepareRecommendationFromJsonContentV2(ActivityDTO dto, ObjectMapper mapper,
                                                                String jsonContent) throws JsonProcessingException {
    JsonNode analysisJson = mapper.readTree(jsonContent);
    JsonNode analysisNode = analysisJson.path("analysis");

    StringBuilder fullAnalysis = new StringBuilder();
    addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
    addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
    addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
    addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories:");

    List<String> improvements = extractImprovements(analysisJson.path("improvements"));
    List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
    List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

    return Recommendation.builder()
        .userId(dto.getUserId())
        .activityId(dto.getId())
        .type(dto.getType())
        .recommendation(fullAnalysis.toString().trim())
        .improvements(improvements)
        .suggestions(suggestions)
        .safety(safety)
        .createdAt(LocalDateTime.now())
        .build();
  }

  private Recommendation createDefaultRecommendation(ActivityDTO dto) {
    return Recommendation.builder()
        .userId(dto.getUserId())
        .activityId(dto.getId())
        .type(dto.getType())
        .recommendation("Unable to generate detailed analysis")
        .improvements(Collections.singletonList("Continue with your current routine"))
        .suggestions(Collections.singletonList("Consider consulting a fitness professional"))
        .safety(Arrays.asList(
            "Always warm up before exercise",
            "Stay hydrated",
            "Listen to your body"
        ))
        .createdAt(LocalDateTime.now())
        .build();
  }

  private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
    if (!analysisNode.path(key).isMissingNode()) {
      fullAnalysis.append(prefix)
          .append(analysisNode.path(key).asText())
          .append("\n\n");
    }
  }

  private List<String> extractImprovements(JsonNode improvementsNode) {
    List<String> improvements = new ArrayList<>();
    if (improvementsNode.isArray()) {
      improvementsNode.forEach(improvement -> {
        String area = improvement.path("area").asText();
        String detail = improvement.path("recommendation").asText();
        improvements.add(String.format("%s: %s", area, detail));
      });
    }
    return improvements.isEmpty() ?
        Collections.singletonList("No specific improvements provided") :
        improvements;
  }

  private List<String> extractSuggestions(JsonNode suggestionsNode) {
    List<String> suggestions = new ArrayList<>();
    if (suggestionsNode.isArray()) {
      suggestionsNode.forEach(suggestion -> {
        String workout = suggestion.path("workout").asText();
        String description = suggestion.path("description").asText();
        suggestions.add(String.format("%s: %s", workout, description));
      });
    }
    return suggestions.isEmpty() ?
        Collections.singletonList("No specific suggestions provided") :
        suggestions;
  }

  private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
    List<String> safety = new ArrayList<>();
    if (safetyNode.isArray()) {
      safetyNode.forEach(item -> safety.add(item.asText()));
    }
    return safety.isEmpty() ?
        Collections.singletonList("Follow general safety guidelines") :
        safety;
  }

  private String createPromptForActivity(ActivityDTO dto) {
    return String.format("""
            Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
            {
              "analysis": {
                "overall": "Overall analysis here",
                "pace": "Pace analysis here",
                "heartRate": "Heart rate analysis here",
                "caloriesBurned": "Calories analysis here"
              },
              "improvements": [
                {
                  "area": "Area name",
                  "recommendation": "Detailed recommendation"
                }
              ],
              "suggestions": [
                {
                  "workout": "Workout name",
                  "description": "Detailed workout description"
                }
              ],
              "safety": [
                "Safety point 1",
                "Safety point 2"
              ]
            }

            Analyze this activity:
            Activity Type: %s
            Duration: %d minutes
            Calories Burned: %d
            Additional Metrics: %s
            
            Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
            Ensure the response follows the EXACT JSON format shown above.
            """,
        dto.getType(),
        dto.getDuration(),
        dto.getCaloriesBurned(),
        dto.getAdditionalMetrics()
    );
  }
}
