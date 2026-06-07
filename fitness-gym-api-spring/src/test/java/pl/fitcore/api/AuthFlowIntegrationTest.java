package pl.fitcore.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:fitcore-auth-test;MODE=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1",
    "spring.sql.init.mode=always",
    "fitcore.auth.demo-password=demo123"
})
class AuthFlowIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginProtectsPrivateEndpointsAndRefreshTokenCanOnlyBeUsedOnce() throws Exception {
        JsonNode login = postJson("/api/auth/login", mapOf(
            "email", "demo@fitcore.local",
            "password", "demo123"
        ), 200);

        String accessToken = login.get("accessToken").asText();
        String refreshToken = login.get("refreshToken").asText();

        mockMvc.perform(get("/api/classes/reservations/M-1001"))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/classes/reservations/M-1001")
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/classes/reservations/M-1002")
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isForbidden());

        JsonNode refreshed = postJson("/api/auth/refresh", mapOf(
            "refreshToken", refreshToken
        ), 200);

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mapOf("refreshToken", refreshToken))))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.success").value(false));

        mockMvc.perform(get("/api/classes/reservations/M-1001")
                .header("Authorization", "Bearer " + refreshed.get("accessToken").asText()))
            .andExpect(status().isOk());
    }

    @Test
    void wrongPasswordIsRejected() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mapOf(
                    "email", "demo@fitcore.local",
                    "password", "wrong-password"
                ))))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.success").value(false));
    }

    private JsonNode postJson(String path, Map<String, String> body, int expectedStatus) throws Exception {
        MvcResult result = mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().is(expectedStatus))
            .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private Map<String, String> mapOf(String... values) {
        Map<String, String> result = new HashMap<>();
        for (int index = 0; index < values.length; index += 2) {
            result.put(values[index], values[index + 1]);
        }
        return result;
    }
}
