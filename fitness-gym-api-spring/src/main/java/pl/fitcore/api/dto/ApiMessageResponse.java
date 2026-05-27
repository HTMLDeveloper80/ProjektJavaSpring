package pl.fitcore.api.dto;

public class ApiMessageResponse {
    private final boolean success;
    private final String message;

    public ApiMessageResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
