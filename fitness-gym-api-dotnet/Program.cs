using FitnessGym.Api.DotNet;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSingleton<FitnessStore>();
builder.Services.AddCors(options =>
{
    options.AddPolicy("FitCoreCors", policy =>
    {
        policy
            .WithOrigins("http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:8080", "http://127.0.0.1:8080")
            .AllowAnyHeader()
            .AllowAnyMethod();
    });
});

var app = builder.Build();

app.UseCors("FitCoreCors");

var api = app.MapGroup("/api/fitness");

api.MapGet("/health", () => Results.Ok(new { status = "UP", service = "FitCore ASP.NET Core API" }));

api.MapGet("/exercises", (FitnessStore store) => Results.Ok(store.Exercises));

api.MapGet("/members/{memberId}/dashboard", (string memberId, FitnessStore store) =>
{
    return Results.Ok(store.Dashboard(memberId));
});

api.MapPost("/plans", (CreatePlanRequest request, FitnessStore store) =>
{
    var memberId = ValueOrDefault(request.MemberId, "M-1001");
    var goal = ValueOrDefault(request.Goal, "siła i redukcja");
    var level = ValueOrDefault(request.Level, "średni");
    return Results.Ok(store.CreatePlan(memberId, goal, level));
});

api.MapPost("/workouts/sessions/start", (StartSessionRequest request, FitnessStore store) =>
{
    var memberId = ValueOrDefault(request.MemberId, "M-1001");
    var planId = ValueOrDefault(request.PlanId, "TP-DEMO");
    var workoutName = ValueOrDefault(request.WorkoutName, "Push Day");
    return Results.Ok(store.StartSession(memberId, planId, workoutName));
});

api.MapPost("/workouts/sessions/{sessionId}/complete", (string sessionId, CompleteSessionRequest request, FitnessStore store) =>
{
    var sets = request.Sets ?? Array.Empty<WorkoutSet>();
    return Results.Ok(store.CompleteSession(sessionId, sets));
});

api.MapPost("/workouts/sessions/{sessionId}/analyze", (string sessionId, FitnessStore store) =>
{
    return Results.Ok(store.AnalyzeSession(sessionId));
});

api.MapGet("/members/{memberId}/records", (string memberId, FitnessStore store) =>
{
    return Results.Ok(store.Records(memberId));
});

api.MapPost("/members/{memberId}/records", (string memberId, AddRecordRequest request, FitnessStore store) =>
{
    if (request.Value <= 0)
    {
        return Results.BadRequest(new { message = "Wartość rekordu musi być większa od zera." });
    }

    var exerciseId = ValueOrDefault(request.ExerciseId, "");
    var exerciseName = ValueOrDefault(request.ExerciseName, "Własny rekord");
    var unit = ValueOrDefault(request.Unit, "kg");
    return Results.Ok(store.AddRecord(memberId, exerciseId, exerciseName, request.Value, unit));
});

api.MapPost("/calculators/bmi", (BmiRequest request) =>
{
    if (request.HeightCm <= 0 || request.WeightKg <= 0)
    {
        return Results.BadRequest(new { message = "Waga i wzrost muszą być większe od zera." });
    }

    var heightM = request.HeightCm / 100m;
    var bmi = decimal.Round(request.WeightKg / (heightM * heightM), 2);
    var category = bmi switch
    {
        < 18.5m => "niedowaga",
        < 25m => "norma",
        < 30m => "nadwaga",
        _ => "otyłość"
    };

    return Results.Ok(new BmiResult(bmi, category));
});

api.MapPost("/calculators/tdee", (TdeeRequest request) =>
{
    if (request.HeightCm <= 0 || request.WeightKg <= 0 || request.Age <= 0 || request.ActivityFactor <= 0)
    {
        return Results.BadRequest(new { message = "Dane kalkulatora TDEE są niepoprawne." });
    }

    var sexOffset = request.Sex.Equals("female", StringComparison.OrdinalIgnoreCase) ? -161m : 5m;
    var bmr = 10m * request.WeightKg + 6.25m * request.HeightCm - 5m * request.Age + sexOffset;
    var tdee = bmr * request.ActivityFactor;

    return Results.Ok(new TdeeResult(decimal.Round(bmr, 0), decimal.Round(tdee, 0)));
});

api.MapPost("/calculators/one-rep-max", (OneRepMaxRequest request) =>
{
    if (request.WeightKg <= 0 || request.Reps <= 0)
    {
        return Results.BadRequest(new { message = "Ciężar i liczba powtórzeń muszą być większe od zera." });
    }

    var estimated = request.WeightKg * (1m + request.Reps / 30m);
    return Results.Ok(new OneRepMaxResult(decimal.Round(estimated, 1)));
});

api.MapGet("/gamification/ranking", (FitnessStore store) => Results.Ok(store.Ranking()));

app.Run();

static string ValueOrDefault(string? value, string fallback)
{
    return string.IsNullOrWhiteSpace(value) ? fallback : value.Trim();
}
