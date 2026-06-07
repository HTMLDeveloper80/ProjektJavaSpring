package pl.fitcore.api.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.fitcore.api.security.CurrentMemberGuard;

@RestController
@RequestMapping("/api/fitness")
public class FitnessProxyController {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final CurrentMemberGuard currentMemberGuard;

    public FitnessProxyController(
        RestTemplate restTemplate,
        @Value("${fitcore.dotnet-api.base-url}") String baseUrl,
        CurrentMemberGuard currentMemberGuard
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.currentMemberGuard = currentMemberGuard;
    }

    @GetMapping("/exercises")
    public Object exercises() {
        return get("/exercises");
    }

    @GetMapping("/members/{memberId}/dashboard")
    public Object dashboard(@PathVariable String memberId) {
        currentMemberGuard.requireCurrentMember(memberId);
        return get("/members/" + memberId + "/dashboard");
    }

    @GetMapping("/members/{memberId}/records")
    public Object records(@PathVariable String memberId) {
        currentMemberGuard.requireCurrentMember(memberId);
        return get("/members/" + memberId + "/records");
    }

    @PostMapping("/members/{memberId}/records")
    public Object addRecord(@PathVariable String memberId, @RequestBody Map<String, Object> body) {
        currentMemberGuard.requireCurrentMember(memberId);
        return post("/members/" + memberId + "/records", body);
    }

    @GetMapping("/gamification/ranking")
    public Object ranking() {
        return get("/gamification/ranking");
    }

    @PostMapping("/calculators/bmi")
    public Object bmi(@RequestBody Map<String, Object> request) {
        return post("/calculators/bmi", request);
    }

    @PostMapping("/calculators/tdee")
    public Object tdee(@RequestBody Map<String, Object> request) {
        return post("/calculators/tdee", request);
    }

    @PostMapping("/calculators/one-rep-max")
    public Object oneRepMax(@RequestBody Map<String, Object> request) {
        return post("/calculators/one-rep-max", request);
    }

    private Object get(String path) {
        try {
            return restTemplate.getForObject(baseUrl + path, Object.class);
        } catch (RestClientException exception) {
            throw new IllegalStateException("Moduł .NET nie odpowiada pod adresem " + baseUrl + ".", exception);
        }
    }

    private Object post(String path, Object request) {
        try {
            return restTemplate.postForObject(baseUrl + path, request, Object.class);
        } catch (RestClientException exception) {
            throw new IllegalStateException("Moduł .NET nie odpowiada pod adresem " + baseUrl + ".", exception);
        }
    }
}
