package pl.fitcore.backend.integration;

import pl.fitcore.backend.domain.TrainingPlanSummary;
import pl.fitcore.backend.domain.WorkoutSessionSummary;

public interface TrainingModuleClient {
    WorkoutSessionSummary startSession(String memberId);

    WorkoutSessionSummary analyzeSession(String memberId, String sessionId);

    TrainingPlanSummary createTrainingPlan(String memberId, String goal, String level);
}
