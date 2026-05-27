package pl.fitcore.backend.domain;

public class CheckInResult {
    private final boolean accepted;
    private final String message;

    private CheckInResult(boolean accepted, String message) {
        this.accepted = accepted;
        this.message = message;
    }

    public static CheckInResult accepted(String message) {
        return new CheckInResult(true, message);
    }

    public static CheckInResult rejected(String message) {
        return new CheckInResult(false, message);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getMessage() {
        return message;
    }
}
