package pl.fitcore.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.fitcore.backend.domain.Member;

@Service
public class JwtService {
    private static final String TOKEN_TYPE_CLAIM = "type";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final SecretKey signingKey;
    private final Duration accessTokenDuration;
    private final Duration refreshTokenDuration;

    public JwtService(
        @Value("${fitcore.jwt.secret}") String secret,
        @Value("${fitcore.jwt.access-token-minutes}") long accessTokenMinutes,
        @Value("${fitcore.jwt.refresh-token-days}") long refreshTokenDays
    ) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTokenDuration = Duration.ofMinutes(accessTokenMinutes);
        this.refreshTokenDuration = Duration.ofDays(refreshTokenDays);
    }

    public IssuedToken createAccessToken(Member member) {
        return createToken(member, ACCESS_TOKEN_TYPE, accessTokenDuration);
    }

    public IssuedToken createRefreshToken(Member member) {
        return createToken(member, REFRESH_TOKEN_TYPE, refreshTokenDuration);
    }

    public TokenClaims parseAccessToken(String token) {
        return parseToken(token, ACCESS_TOKEN_TYPE);
    }

    public TokenClaims parseRefreshToken(String token) {
        return parseToken(token, REFRESH_TOKEN_TYPE);
    }

    public String hashToken(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                .digest(token.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder(digest.length * 2);
            for (byte value : digest) {
                result.append(String.format("%02x", value));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available.", exception);
        }
    }

    public long getAccessTokenExpiresInSeconds() {
        return accessTokenDuration.getSeconds();
    }

    private IssuedToken createToken(Member member, String type, Duration duration) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(duration);
        String tokenId = UUID.randomUUID().toString();
        String token = Jwts.builder()
            .setId(tokenId)
            .setSubject(member.getId())
            .claim("email", member.getEmail())
            .claim(TOKEN_TYPE_CLAIM, type)
            .setIssuedAt(Date.from(issuedAt))
            .setExpiration(Date.from(expiresAt))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact();
        return new IssuedToken(token, tokenId, expiresAt);
    }

    private TokenClaims parseToken(String token, String expectedType) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .getBody();

        String type = claims.get(TOKEN_TYPE_CLAIM, String.class);
        if (!expectedType.equals(type)) {
            throw new JwtException("Unexpected token type.");
        }

        return new TokenClaims(
            claims.getSubject(),
            claims.get("email", String.class),
            claims.getId(),
            claims.getExpiration().toInstant()
        );
    }

    public static final class IssuedToken {
        private final String value;
        private final String id;
        private final Instant expiresAt;

        public IssuedToken(String value, String id, Instant expiresAt) {
            this.value = value;
            this.id = id;
            this.expiresAt = expiresAt;
        }

        public String getValue() {
            return value;
        }

        public String getId() {
            return id;
        }

        public Instant getExpiresAt() {
            return expiresAt;
        }
    }

    public static final class TokenClaims {
        private final String memberId;
        private final String email;
        private final String tokenId;
        private final Instant expiresAt;

        public TokenClaims(String memberId, String email, String tokenId, Instant expiresAt) {
            this.memberId = memberId;
            this.email = email;
            this.tokenId = tokenId;
            this.expiresAt = expiresAt;
        }

        public String getMemberId() {
            return memberId;
        }

        public String getEmail() {
            return email;
        }

        public String getTokenId() {
            return tokenId;
        }

        public Instant getExpiresAt() {
            return expiresAt;
        }
    }
}
