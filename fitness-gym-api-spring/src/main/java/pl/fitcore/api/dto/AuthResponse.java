package pl.fitcore.api.dto;

public class AuthResponse {
    private final boolean success;
    private final String message;
    private final String memberId;
    private final String name;
    private final String email;
    private final String qrCode;

    public AuthResponse(boolean success, String message, String memberId, String name, String email, String qrCode) {
        this.success = success;
        this.message = message;
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.qrCode = qrCode;
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
}
