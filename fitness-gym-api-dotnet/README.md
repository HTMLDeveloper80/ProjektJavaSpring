# FitCore ASP.NET Core API

Ten moduł realizuje część z tematu projektu przypisaną do **ASP.NET Core API (C#)**:

- plany treningowe i biblioteka ćwiczeń,
- dziennik sesji treningowych,
- rekordy osobiste i śledzenie postępów,
- kalkulatory BMI, TDEE i 1RM,
- gamifikacja: odznaki i ranking.

Docelowy przepływ:

```txt
React -> Spring Boot API -> ASP.NET Core API
```

Spring Boot API pozostaje główną bramą dla Reacta, a ten moduł jest wywoływany przez Springa przy akcjach treningowych.

## Uruchomienie

Z katalogu głównego:

```powershell
.\run-dotnet-api.ps1
```

Adres API:

```txt
http://localhost:5080/api/fitness
```

## Endpointy

```txt
GET  /api/fitness/health
GET  /api/fitness/exercises
GET  /api/fitness/members/{memberId}/dashboard
POST /api/fitness/plans
POST /api/fitness/workouts/sessions/start
POST /api/fitness/workouts/sessions/{sessionId}/complete
POST /api/fitness/workouts/sessions/{sessionId}/analyze
GET  /api/fitness/members/{memberId}/records
POST /api/fitness/calculators/bmi
POST /api/fitness/calculators/tdee
POST /api/fitness/calculators/one-rep-max
GET  /api/fitness/gamification/ranking
```
