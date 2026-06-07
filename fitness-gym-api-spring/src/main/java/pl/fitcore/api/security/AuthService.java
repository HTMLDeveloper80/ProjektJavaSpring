package pl.fitcore.api.security;

import io.jsonwebtoken.JwtException;
import java.time.Instant;
import java.util.Locale;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fitcore.api.dto.AuthRequest;
import pl.fitcore.api.dto.AuthResponse;
import pl.fitcore.api.repository.JdbcAuthRepository;
import pl.fitcore.backend.app.FitnessBackendService;
import pl.fitcore.backend.domain.Member;

@Service
public class AuthService {
    private final FitnessBackendService fitnessBackendService;
    private final JdbcAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
        FitnessBackendService fitnessBackendService,
        JdbcAuthRepository authRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService
    ) {
        this.fitnessBackendService = fitnessBackendService;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(AuthRequest request) {
        String name = requireValue(request.getName(), "Imie jest wymagane.");
        String email = normalizeEmail(request.getEmail());
        String password = validatePassword(request.getPassword());

        if (authRepository.findMemberByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Konto dla tego adresu e-mail juz istnieje.");
        }

        Member member = fitnessBackendService.registerMember(name, email);
        authRepository.savePassword(member.getId(), passwordEncoder.encode(password));
        return issueTokenPair(member, "Konto utworzone i zalogowane.");
    }

    @Transactional
    public AuthResponse login(AuthRequest request) {
        String email = normalizeEmail(request.getEmail());
        String password = validatePassword(request.getPassword());
        Member member = authRepository.findMemberByEmail(email)
            .orElseThrow(() -> new BadCredentialsException("Nieprawidlowy e-mail lub haslo."));
        String passwordHash = authRepository.findPasswordHash(member.getId())
            .orElseThrow(() -> new BadCredentialsException("Nieprawidlowy e-mail lub haslo."));

        if (!passwordEncoder.matches(password, passwordHash)) {
            throw new BadCredentialsException("Nieprawidlowy e-mail lub haslo.");
        }

        return issueTokenPair(member, "Zalogowano.");
    }

    @Transactional
    public AuthResponse refresh(String refreshToken) {
        String token = requireValue(refreshToken, "Refresh token jest wymagany.");
        try {
            JwtService.TokenClaims claims = jwtService.parseRefreshToken(token);
            String tokenHash = jwtService.hashToken(token);
            if (!authRepository.consumeRefreshToken(tokenHash, claims.getMemberId(), Instant.now())) {
                throw new BadCredentialsException("Refresh token jest niewazny lub zostal juz uzyty.");
            }

            Member member = authRepository.findMemberById(claims.getMemberId())
                .orElseThrow(() -> new BadCredentialsException("Konto przypisane do tokenu nie istnieje."));
            return issueTokenPair(member, "Tokeny odswiezone.");
        } catch (JwtException | IllegalArgumentException exception) {
            throw new BadCredentialsException("Refresh token jest niewazny lub wygasl.", exception);
        }
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return;
        }
        authRepository.revokeRefreshToken(jwtService.hashToken(refreshToken.trim()));
    }

    private AuthResponse issueTokenPair(Member member, String message) {
        JwtService.IssuedToken accessToken = jwtService.createAccessToken(member);
        JwtService.IssuedToken refreshToken = jwtService.createRefreshToken(member);
        authRepository.saveRefreshToken(
            refreshToken.getId(),
            member.getId(),
            jwtService.hashToken(refreshToken.getValue()),
            refreshToken.getExpiresAt()
        );

        return new AuthResponse(
            true,
            message,
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getQrCode(),
            accessToken.getValue(),
            refreshToken.getValue(),
            jwtService.getAccessTokenExpiresInSeconds()
        );
    }

    private String normalizeEmail(String value) {
        String email = requireValue(value, "E-mail jest wymagany.").toLowerCase(Locale.ROOT);
        if (!email.contains("@") || email.length() > 160) {
            throw new IllegalArgumentException("Podaj poprawny adres e-mail.");
        }
        return email;
    }

    private String validatePassword(String value) {
        String password = requireValue(value, "Haslo jest wymagane.");
        if (password.length() < 6 || password.length() > 72) {
            throw new IllegalArgumentException("Haslo musi miec od 6 do 72 znakow.");
        }
        return password;
    }

    private String requireValue(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
