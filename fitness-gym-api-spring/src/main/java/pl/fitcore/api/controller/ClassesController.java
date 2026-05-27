package pl.fitcore.api.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fitcore.api.dto.ApiMessageResponse;
import pl.fitcore.api.dto.ReservationRequest;
import pl.fitcore.api.support.DemoMemberResolver;
import pl.fitcore.backend.app.FitnessBackendService;
import pl.fitcore.backend.domain.GymClass;
import pl.fitcore.backend.domain.ReservationResult;

@RestController
@RequestMapping("/api/classes")
public class ClassesController {
    private final FitnessBackendService service;
    private final DemoMemberResolver memberResolver;

    public ClassesController(FitnessBackendService service, DemoMemberResolver memberResolver) {
        this.service = service;
        this.memberResolver = memberResolver;
    }

    @GetMapping
    public List<GymClass> classes() {
        return service.classes();
    }

    @GetMapping("/reservations/{memberId}")
    public List<GymClass> reservations(@PathVariable String memberId) {
        return service.reservationsForMember(memberId);
    }

    @PostMapping("/reservations")
    public ApiMessageResponse reserve(@RequestBody ReservationRequest request) {
        String memberId = memberResolver.resolveMemberId(request.getMemberId(), request.getUser());
        String classId = resolveClassId(request);
        ReservationResult result = service.reserveClass(memberId, classId);
        return new ApiMessageResponse(result.isAccepted(), result.getMessage());
    }

    private String resolveClassId(ReservationRequest request) {
        if (request.getClassId() != null && !request.getClassId().trim().isEmpty()) {
            return request.getClassId().trim();
        }

        if (request.getClassName() != null && !request.getClassName().trim().isEmpty()) {
            return service.classes().stream()
                .filter(item -> item.getName().equalsIgnoreCase(request.getClassName().trim()))
                .map(GymClass::getId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono zajęć: " + request.getClassName()));
        }

        throw new IllegalArgumentException("Brak classId albo className.");
    }
}
