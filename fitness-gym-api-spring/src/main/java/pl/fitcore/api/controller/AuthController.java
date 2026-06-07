package pl.fitcore.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fitcore.api.dto.ApiMessageResponse;
import pl.fitcore.api.dto.AuthRequest;
import pl.fitcore.api.dto.AuthResponse;
import pl.fitcore.api.dto.RefreshTokenRequest;
import pl.fitcore.api.security.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshTokenRequest request) {
        return authService.refresh(request.getRefreshToken());
    }

    @PostMapping("/logout")
    public ApiMessageResponse logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return new ApiMessageResponse(true, "Wylogowano.");
    }
}
