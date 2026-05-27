package pl.fitcore.api.support;

import org.springframework.stereotype.Component;
import pl.fitcore.backend.app.FitnessBackendService;
import pl.fitcore.backend.domain.Member;

@Component
public class DemoMemberResolver {
    private static final String DEMO_MEMBER_ID = "M-1001";

    private final FitnessBackendService service;

    public DemoMemberResolver(FitnessBackendService service) {
        this.service = service;
    }

    public String resolveMemberId(String memberId, String email) {
        if (memberId != null && !memberId.trim().isEmpty()) {
            return memberId.trim();
        }

        if (email != null && !email.trim().isEmpty()) {
            return service.members().stream()
                .filter(member -> member.getEmail().equalsIgnoreCase(email.trim()))
                .map(Member::getId)
                .findFirst()
                .orElse(DEMO_MEMBER_ID);
        }

        return DEMO_MEMBER_ID;
    }
}
