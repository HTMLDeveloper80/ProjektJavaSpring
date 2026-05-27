package pl.fitcore.backend.domain;

import java.time.LocalDateTime;

public class CheckIn {
    private final String memberId;
    private final LocalDateTime checkedInAt;

    public CheckIn(String memberId, LocalDateTime checkedInAt) {
        this.memberId = memberId;
        this.checkedInAt = checkedInAt;
    }

    public String getMemberId() {
        return memberId;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }
}
