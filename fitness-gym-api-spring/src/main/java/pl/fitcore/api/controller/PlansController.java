package pl.fitcore.api.controller;

import java.util.Locale;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fitcore.api.dto.ApiMessageResponse;
import pl.fitcore.api.dto.PlanSelectRequest;
import pl.fitcore.api.security.CurrentMemberGuard;
import pl.fitcore.api.support.DemoMemberResolver;
import pl.fitcore.backend.app.FitnessBackendService;
import pl.fitcore.backend.domain.Membership;
import pl.fitcore.backend.domain.MembershipType;

@RestController
@RequestMapping("/api/plans")
public class PlansController {
    private final FitnessBackendService service;
    private final DemoMemberResolver memberResolver;
    private final CurrentMemberGuard currentMemberGuard;

    public PlansController(
        FitnessBackendService service,
        DemoMemberResolver memberResolver,
        CurrentMemberGuard currentMemberGuard
    ) {
        this.service = service;
        this.memberResolver = memberResolver;
        this.currentMemberGuard = currentMemberGuard;
    }

    @PostMapping("/select")
    public ApiMessageResponse select(@RequestBody PlanSelectRequest request) {
        String requestedMemberId = memberResolver.resolveMemberId(request.getMemberId(), request.getUser());
        String memberId = currentMemberGuard.requireCurrentMember(requestedMemberId);
        MembershipType type = MembershipType.valueOf(valueOrDefault(request.getPlanName(), "PRO").toUpperCase(Locale.ROOT));
        Membership membership = service.selectMembership(memberId, type);
        return new ApiMessageResponse(true, "Wybrano karnet " + membership.getType() + " ważny do " + membership.getValidTo() + ".");
    }

    private String valueOrDefault(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }
}
