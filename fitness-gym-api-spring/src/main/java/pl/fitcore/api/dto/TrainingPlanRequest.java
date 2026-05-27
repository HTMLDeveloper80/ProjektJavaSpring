package pl.fitcore.api.dto;

public class TrainingPlanRequest {
    private String goal;
    private String level;

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
