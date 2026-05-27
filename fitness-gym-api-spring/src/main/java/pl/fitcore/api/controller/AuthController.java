package pl.fitcore.api.controller;

import java.util.Comparator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fitcore.api.dto.AuthRequest;
import pl.fitcore.api.dto.AuthResponse;
import pl.fitcore.backend.app.FitnessBackendService;
import pl.fitcore.backend.domain.Member;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final FitnessBackendService service;

    public AuthController(FitnessBackendService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) {
        String name = valueOrDefault(request.getName(), "Nowy Klubowicz");
        String email = valueOrDefault(request.getEmail(), "demo@fitcore.local");
        Member member = service.registerMember(name, email);

        return new AuthResponse(true, "Konto utworzone w Spring API.", member.getId(), member.getName(), member.getEmail(), member.getQrCode());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        String email = valueOrDefault(request.getEmail(), "");
        Member member = service.members().stream()
            .filter(item -> item.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono konta dla adresu e-mail. Załóż nowe konto."));

        return new AuthResponse(true, "Zalogowano.", member.getId(), member.getName(), member.getEmail(), member.getQrCode());
    }

    @PostMapping("/client-panel")
    public AuthResponse clientPanel() {
        Member member = service.members().stream()
            .min(Comparator.comparing(Member::getId))
            .orElseThrow(() -> new IllegalStateException("Brak użytkowników."));

        return new AuthResponse(true, "Panel klienta gotowy.", member.getId(), member.getName(), member.getEmail(), member.getQrCode());
    }

    private String valueOrDefault(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }
}
