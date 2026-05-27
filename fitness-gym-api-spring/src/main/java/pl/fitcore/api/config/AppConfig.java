package pl.fitcore.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.fitcore.backend.app.FitnessBackendService;
import pl.fitcore.backend.integration.TrainingModuleClient;
import pl.fitcore.backend.repository.FitnessRepository;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FitnessBackendService fitnessBackendService(FitnessRepository repository, TrainingModuleClient trainingModuleClient) {
        return new FitnessBackendService(repository, trainingModuleClient);
    }
}
