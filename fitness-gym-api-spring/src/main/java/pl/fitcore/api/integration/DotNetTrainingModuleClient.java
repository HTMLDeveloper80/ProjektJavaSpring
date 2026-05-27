package pl.fitcore.api.integration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.fitcore.backend.domain.TrainingPlanSummary;
import pl.fitcore.backend.domain.WorkoutSessionSummary;
import pl.fitcore.backend.integration.TrainingModuleClient;

@Component
public class DotNetTrainingModuleClient implements TrainingModuleClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public DotNetTrainingModuleClient(
        RestTemplate restTemplate,
        @Value("${fitcore.dotnet-api.base-url}") String baseUrl
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public WorkoutSessionSummary startSession(String memberId) {
        Map<String, String> request = new LinkedHashMap<>();
        request.put("memberId", memberId);
        request.put("planId", "TP-DEMO");
        request.put("workoutName", "Push Day");

        Map<String, Object> response = postForMap("/workouts/sessions/start", request);
        return new WorkoutSessionSummary(
            stringValue(response.get("id"), "WS-" + memberId),
            stringValue(response.get("summary"), "Sesja treningowa utworzona w module .NET."),
            82
        );
    }

    public WorkoutSessionSummary analyzeSession(String memberId, String sessionId) {
        Map<String, Object> response = postForMap("/workouts/sessions/" + sessionId + "/analyze", new LinkedHashMap<String, String>());
        return new WorkoutSessionSummary(
            stringValue(response.get("sessionId"), sessionId),
            stringValue(response.get("summary"), "Analiza sesji wykonana w module .NET."),
            intValue(response.get("progressPercent"), 88)
        );
    }

    public TrainingPlanSummary createTrainingPlan(String memberId, String goal, String level) {
        Map<String, String> request = new LinkedHashMap<>();
        request.put("memberId", memberId);
        request.put("goal", goal);
        request.put("level", level);

        Map<String, Object> response = postForMap("/plans", request);
        return new TrainingPlanSummary(
            stringValue(response.get("id"), "TP-" + memberId),
            stringValue(response.get("title"), "Plan: " + goal + " (" + level + ")"),
            listValue(response.get("exercises"))
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> postForMap(String path, Object request) {
        try {
            Map<String, Object> response = restTemplate.postForObject(baseUrl + path, request, Map.class);
            if (response == null) {
                throw new IllegalStateException("Moduł .NET zwrócił pustą odpowiedź.");
            }
            return response;
        } catch (RestClientException exception) {
            throw new IllegalStateException("Nie udało się połączyć z modułem .NET pod adresem " + baseUrl + ".", exception);
        }
    }

    private String stringValue(Object value, String fallback) {
        return value == null ? fallback : value.toString();
    }

    private int intValue(Object value, int fallback) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return fallback;
    }

    private List<String> listValue(Object value) {
        if (!(value instanceof List<?>)) {
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>();
        for (Object item : (List<?>) value) {
            result.add(String.valueOf(item));
        }
        return result;
    }
}
