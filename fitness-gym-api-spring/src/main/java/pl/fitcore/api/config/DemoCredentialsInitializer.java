package pl.fitcore.api.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.fitcore.api.repository.JdbcAuthRepository;

@Component
public class DemoCredentialsInitializer implements ApplicationRunner {
    private static final String[] DEMO_MEMBER_IDS = {"M-1001", "M-1002", "M-1003"};

    private final JdbcAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final String demoPassword;

    public DemoCredentialsInitializer(
        JdbcAuthRepository authRepository,
        PasswordEncoder passwordEncoder,
        @Value("${fitcore.auth.demo-password}") String demoPassword
    ) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.demoPassword = demoPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        Arrays.stream(DEMO_MEMBER_IDS)
            .filter(memberId -> !authRepository.findPasswordHash(memberId).isPresent())
            .forEach(memberId ->
                authRepository.savePassword(memberId, passwordEncoder.encode(demoPassword)));
    }
}
