package pl.fitcore.backend.domain;

import java.time.LocalDateTime;

public class Reservation {
    private final String memberId;
    private final String classId;
    private final LocalDateTime createdAt;

    public Reservation(String memberId, String classId, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.classId = classId;
        this.createdAt = createdAt;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getClassId() {
        return classId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
