package pl.fitcore.backend.domain;

public class WorkoutSessionSummary {
    private final String sessionId;
    private final String summary;
    private final int progressPercent;

    public WorkoutSessionSummary(String sessionId, String summary, int progressPercent) {
        this.sessionId = sessionId;
        this.summary = summary;
        this.progressPercent = progressPercent;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSummary() {
        return summary;
    }

    public int getProgressPercent() {
        return progressPercent;
    }
}
