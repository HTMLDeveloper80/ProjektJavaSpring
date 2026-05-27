package pl.fitcore.backend.integration;

import java.util.Arrays;
import pl.fitcore.backend.domain.TrainingPlanSummary;
import pl.fitcore.backend.domain.WorkoutSessionSummary;

public class DemoTrainingModuleClient implements TrainingModuleClient {
    public WorkoutSessionSummary startSession(String memberId) {
        return new WorkoutSessionSummary(
            "WS-" + memberId,
            "Sesja treningowa utworzona w module .NET.",
            82
        );
    }

    public WorkoutSessionSummary analyzeSession(String memberId, String sessionId) {
        return new WorkoutSessionSummary(
            sessionId,
            "Analiza .NET: progres dodatni, rekord do aktualizacji.",
            88
        );
    }

    public TrainingPlanSummary createTrainingPlan(String memberId, String goal, String level) {
        return new TrainingPlanSummary(
            "TP-" + memberId,
            "Plan: " + goal + " (" + level + ")",
            Arrays.asList("Przysiad ze sztangą", "Wyciskanie na ławce", "Martwy ciąg", "Mobility Flow")
        );
    }
}
