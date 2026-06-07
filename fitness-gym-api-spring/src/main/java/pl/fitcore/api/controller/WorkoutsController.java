package pl.fitcore.api.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fitcore.api.dto.TrainingPlanRequest;
import pl.fitcore.api.dto.WorkoutStartRequest;
import pl.fitcore.api.security.CurrentMemberGuard;
import pl.fitcore.api.support.DemoMemberResolver;
import pl.fitcore.backend.app.FitnessBackendService;
import pl.fitcore.backend.domain.TrainingPlanSummary;
import pl.fitcore.backend.domain.WorkoutSessionSummary;

@RestController
@RequestMapping("/api")
public class WorkoutsController {
    private final FitnessBackendService service;
    private final DemoMemberResolver memberResolver;
    private final CurrentMemberGuard currentMemberGuard;

    public WorkoutsController(
        FitnessBackendService service,
        DemoMemberResolver memberResolver,
        CurrentMemberGuard currentMemberGuard
    ) {
        this.service = service;
        this.memberResolver = memberResolver;
        this.currentMemberGuard = currentMemberGuard;
    }

    @PostMapping("/workouts/start")
    public WorkoutSessionSummary startWorkout(@RequestBody WorkoutStartRequest request) {
        String requestedMemberId = memberResolver.resolveMemberId(request.getMemberId(), request.getUser());
        String memberId = currentMemberGuard.requireCurrentMember(requestedMemberId);
        return service.startWorkoutSession(memberId);
    }

    @PostMapping("/members/{memberId}/training-plan")
    public TrainingPlanSummary assignTrainingPlan(@PathVariable String memberId, @RequestBody TrainingPlanRequest request) {
        memberId = currentMemberGuard.requireCurrentMember(memberId);
        String goal = valueOrDefault(request.getGoal(), "siła i redukcja");
        String level = valueOrDefault(request.getLevel(), "średni");
        return service.assignTrainingPlan(memberId, goal, level);
    }

    private String valueOrDefault(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }
}
