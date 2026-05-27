namespace FitnessGym.Api.DotNet;

public record Exercise(
    string Id,
    string Name,
    string Category,
    string Difficulty,
    string[] Muscles,
    string Description
);

public record TrainingPlan(
    string Id,
    string MemberId,
    string Title,
    string Goal,
    string Level,
    string[] Exercises,
    DateTime CreatedAt
);

public record WorkoutSet(
    string ExerciseId,
    int Sets,
    int Reps,
    decimal WeightKg
);

public record WorkoutSession(
    string Id,
    string MemberId,
    string PlanId,
    string Status,
    DateTime StartedAt,
    DateTime? CompletedAt,
    WorkoutSet[] Sets,
    string Summary
);

public record PersonalRecord(
    string MemberId,
    string ExerciseId,
    string ExerciseName,
    decimal Value,
    string Unit,
    DateTime AchievedAt
);

public record AddRecordRequest(string? MemberId, string? ExerciseId, string? ExerciseName, decimal Value, string? Unit);

public record Badge(
    string Id,
    string Name,
    string Description,
    int Points
);

public record RankingEntry(
    string MemberId,
    string DisplayName,
    int Points,
    int Position
);

public record MemberDashboard(
    string MemberId,
    int WeeklyProgressPercent,
    int CompletedSessions,
    int PersonalRecords,
    int Points,
    Badge[] Badges
);

public record CreatePlanRequest(string? MemberId, string? Goal, string? Level);

public record StartSessionRequest(string? MemberId, string? PlanId, string? WorkoutName);

public record CompleteSessionRequest(WorkoutSet[]? Sets);

public record SessionAnalysis(
    string SessionId,
    string MemberId,
    string Summary,
    int ProgressPercent,
    bool UnlockBadge,
    PersonalRecord[] UpdatedRecords
);

public record BmiRequest(decimal WeightKg, decimal HeightCm);

public record BmiResult(decimal Bmi, string Category);

public record TdeeRequest(decimal WeightKg, decimal HeightCm, int Age, string Sex, decimal ActivityFactor);

public record TdeeResult(decimal Bmr, decimal Tdee);

public record OneRepMaxRequest(decimal WeightKg, int Reps);

public record OneRepMaxResult(decimal EstimatedMaxKg);
