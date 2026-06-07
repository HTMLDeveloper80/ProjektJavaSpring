package pl.fitcore.api.dto;

public class AuthResponse {
    private final boolean success;
    private final String message;
    private final String memberId;
    private final String name;
    private final String email;
    private final String qrCode;
    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;
    private final long expiresIn;

    public AuthResponse(
        boolean success,
        String message,
        String memberId,
        String name,
        String email,
        String qrCode,
        String accessToken,
        String refreshToken,
        long expiresIn
    ) {
        this.success = success;
        this.message = message;
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.qrCode = qrCode;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
        this.expiresIn = expiresIn;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getQrCode() {
        return qrCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
