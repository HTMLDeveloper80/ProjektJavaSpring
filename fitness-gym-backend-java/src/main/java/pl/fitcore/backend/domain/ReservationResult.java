package pl.fitcore.backend.domain;

public class ReservationResult {
    private final boolean accepted;
    private final String message;

    private ReservationResult(boolean accepted, String message) {
        this.accepted = accepted;
        this.message = message;
    }

    public static ReservationResult accepted(String message) {
        return new ReservationResult(true, message);
    }

    public static ReservationResult rejected(String message) {
        return new ReservationResult(false, message);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getMessage() {
        return message;
    }
}
