package pl.fitcore.backend.domain;

import java.time.LocalDate;

public class Membership {
    private final String id;
    private final String memberId;
    private final MembershipType type;
    private final LocalDate validFrom;
    private final LocalDate validTo;

    public Membership(String id, String memberId, MembershipType type, LocalDate validFrom, LocalDate validTo) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public static Membership starting(String memberId) {
        return monthly(memberId, MembershipType.START);
    }

    public static Membership monthly(String memberId, MembershipType type) {
        LocalDate now = LocalDate.now();
        return new Membership("MS-" + memberId + "-" + type, memberId, type, now, now.plusMonths(1));
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(validFrom) && !today.isAfter(validTo);
    }

    public String getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public MembershipType getType() {
        return type;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }
}
