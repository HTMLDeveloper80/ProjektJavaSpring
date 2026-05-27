package pl.fitcore.backend.domain;

import java.util.List;

public class TrainingPlanSummary {
    private final String planId;
    private final String title;
    private final List<String> exercises;

    public TrainingPlanSummary(String planId, String title, List<String> exercises) {
        this.planId = planId;
        this.title = title;
        this.exercises = exercises;
    }

    public String getPlanId() {
        return planId;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getExercises() {
        return exercises;
    }
}
