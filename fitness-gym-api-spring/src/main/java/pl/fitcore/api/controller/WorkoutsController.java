package pl.fitcore.api.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fitcore.api.dto.TrainingPlanRequest;
import pl.fitcore.api.dto.WorkoutStartRequest;
import pl.fitcore.api.support.DemoMemberResolver;
import pl.fitcore.backend.app.FitnessBackendService;
import pl.fitcore.backend.domain.TrainingPlanSummary;
import pl.fitcore.backend.domain.WorkoutSessionSummary;

@RestController
@RequestMapping("/api")
public class WorkoutsController {
    private final FitnessBackendService service;
    private final DemoMemberResolver memberResolver;

    public WorkoutsController(FitnessBackendService service, DemoMemberResolver memberResolver) {
        this.service = service;
        this.memberResolver = memberResolver;
    }

    @PostMapping("/workouts/start")
    public WorkoutSessionSummary startWorkout(@RequestBody WorkoutStartRequest request) {
        String memberId = memberResolver.resolveMemberId(request.getMemberId(), request.getUser());
        return service.startWorkoutSession(memberId);
    }

    @PostMapping("/members/{memberId}/training-plan")
    public TrainingPlanSummary assignTrainingPlan(@PathVariable String memberId, @RequestBody TrainingPlanRequest request) {
        String goal = valueOrDefault(request.getGoal(), "siła i redukcja");
        String level = valueOrDefault(request.getLevel(), "średni");
        return service.assignTrainingPlan(memberId, goal, level);
    }

    private String valueOrDefault(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }
}
