namespace FitnessGym.Api.DotNet;

public class FitnessStore
{
    private readonly List<Exercise> _exercises = new()
    {
        new("EX-SQUAT", "Przysiad ze sztangą", "Siła", "Średni", new[] { "nogi", "pośladki", "core" }, "Podstawowy wzorzec siłowy dla dolnej części ciała."),
        new("EX-BENCH", "Wyciskanie na ławce", "Siła", "Średni", new[] { "klatka", "triceps", "barki" }, "Ćwiczenie główne dla treningu push."),
        new("EX-DEADLIFT", "Martwy ciąg", "Siła", "Mocny", new[] { "plecy", "nogi", "core" }, "Ćwiczenie bazowe rozwijające całe ciało."),
        new("EX-PULLUP", "Podciąganie", "Kalistenika", "Średni", new[] { "plecy", "biceps" }, "Kontrola masy ciała i siła górnej części pleców."),
        new("EX-HIP-THRUST", "Hip thrust", "Siła", "Lekki", new[] { "pośladki", "dwugłowe uda" }, "Akcesorium do pracy bioder i pośladków.")
    };

    private readonly List<TrainingPlan> _plans = new();
    private readonly List<WorkoutSession> _sessions = new();
    private readonly List<PersonalRecord> _records = new()
    {
        new("M-1001", "EX-SQUAT", "Przysiad ze sztangą", 140, "kg", DateTime.UtcNow.AddDays(-12)),
        new("M-1001", "EX-BENCH", "Wyciskanie na ławce", 105, "kg", DateTime.UtcNow.AddDays(-8)),
        new("M-1001", "EX-DEADLIFT", "Martwy ciąg", 180, "kg", DateTime.UtcNow.AddDays(-20))
    };

    private readonly List<Badge> _badges = new()
    {
        new("BD-CONSISTENCY", "Seria treningowa", "3 sesje w tygodniu", 120),
        new("BD-POWER", "Nowa siła", "Pobity rekord osobisty", 180),
        new("BD-STARTER", "Dobry start", "Pierwsza ukończona sesja", 60)
    };

    private readonly List<RankingEntry> _ranking = new()
    {
        new("M-1001", "Klubowicz Demo", 1260, 1),
        new("M-1003", "Marek", 1110, 2),
        new("M-1002", "Anna", 980, 3)
    };

    public IReadOnlyList<Exercise> Exercises => _exercises;

    public TrainingPlan CreatePlan(string memberId, string goal, string level)
    {
        var selectedExercises = level.Equals("lekki", StringComparison.OrdinalIgnoreCase)
            ? new[] { "EX-PULLUP", "EX-HIP-THRUST", "EX-BENCH" }
            : new[] { "EX-SQUAT", "EX-BENCH", "EX-DEADLIFT", "EX-PULLUP" };

        var plan = new TrainingPlan(
            $"TP-{memberId}-{_plans.Count + 1}",
            memberId,
            $"Plan: {goal} ({level})",
            goal,
            level,
            selectedExercises,
            DateTime.UtcNow
        );

        _plans.Add(plan);
        return plan;
    }

    public WorkoutSession StartSession(string memberId, string planId, string workoutName)
    {
        var session = new WorkoutSession(
            $"WS-{memberId}-{_sessions.Count + 1}",
            memberId,
            planId,
            "ACTIVE",
            DateTime.UtcNow,
            null,
            Array.Empty<WorkoutSet>(),
            $"Rozpoczęto sesję {workoutName}."
        );

        _sessions.Add(session);
        return session;
    }

    public WorkoutSession CompleteSession(string sessionId, WorkoutSet[] sets)
    {
        var index = _sessions.FindIndex(session => session.Id == sessionId);
        if (index < 0)
        {
            throw new InvalidOperationException($"Nie znaleziono sesji {sessionId}.");
        }

        var current = _sessions[index];
        var completed = current with
        {
            Status = "COMPLETED",
            CompletedAt = DateTime.UtcNow,
            Sets = sets,
            Summary = $"Zakończono sesję. Liczba ćwiczeń: {sets.Length}."
        };

        _sessions[index] = completed;
        UpdateRecords(completed);
        return completed;
    }

    public SessionAnalysis AnalyzeSession(string sessionId)
    {
        var session = _sessions.FirstOrDefault(item => item.Id == sessionId)
            ?? throw new InvalidOperationException($"Nie znaleziono sesji {sessionId}.");

        var memberRecords = _records
            .Where(record => record.MemberId == session.MemberId)
            .OrderByDescending(record => record.AchievedAt)
            .Take(3)
            .ToArray();

        return new SessionAnalysis(
            session.Id,
            session.MemberId,
            session.Status == "COMPLETED"
                ? "Sesja ukończona. Postęp został zapisany w dzienniku."
                : "Sesja aktywna. Po zakończeniu zostanie naliczony progres.",
            session.Status == "COMPLETED" ? 88 : 62,
            session.Sets.Any(set => set.WeightKg >= 100),
            memberRecords
        );
    }

    public IReadOnlyList<PersonalRecord> Records(string memberId)
    {
        return _records.Where(record => record.MemberId == memberId).ToList();
    }

    public PersonalRecord AddRecord(string memberId, string exerciseId, string exerciseName, decimal value, string unit)
    {
        var exercise = _exercises.FirstOrDefault(item => item.Id == exerciseId);
        var record = new PersonalRecord(
            memberId,
            string.IsNullOrWhiteSpace(exerciseId) ? $"EX-CUSTOM-{_records.Count + 1}" : exerciseId,
            string.IsNullOrWhiteSpace(exerciseName) ? exercise?.Name ?? "Własny rekord" : exerciseName,
            value,
            string.IsNullOrWhiteSpace(unit) ? "kg" : unit,
            DateTime.UtcNow
        );

        _records.Add(record);
        return record;
    }

    public MemberDashboard Dashboard(string memberId)
    {
        var sessions = _sessions.Count(session => session.MemberId == memberId && session.Status == "COMPLETED");
        var records = _records.Count(record => record.MemberId == memberId);
        var ranking = _ranking.FirstOrDefault(entry => entry.MemberId == memberId);

        return new MemberDashboard(
            memberId,
            Math.Min(100, 76 + sessions * 4),
            sessions,
            records,
            ranking?.Points ?? 500,
            _badges.Take(sessions > 0 ? 3 : 1).ToArray()
        );
    }

    public IReadOnlyList<RankingEntry> Ranking()
    {
        return _ranking;
    }

    private void UpdateRecords(WorkoutSession session)
    {
        foreach (var set in session.Sets)
        {
            var exercise = _exercises.FirstOrDefault(item => item.Id == set.ExerciseId);
            if (exercise is null)
            {
                continue;
            }

            var currentRecord = _records
                .Where(record => record.MemberId == session.MemberId && record.ExerciseId == set.ExerciseId)
                .OrderByDescending(record => record.Value)
                .FirstOrDefault();

            if (currentRecord is null || set.WeightKg > currentRecord.Value)
            {
                _records.Add(new PersonalRecord(session.MemberId, set.ExerciseId, exercise.Name, set.WeightKg, "kg", DateTime.UtcNow));
            }
        }
    }
}
