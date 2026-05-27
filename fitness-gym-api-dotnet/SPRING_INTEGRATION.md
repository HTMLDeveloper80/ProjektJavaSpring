# Integracja Spring Boot API z ASP.NET Core API

ASP.NET Core API powinno być traktowane jako moduł treningowy. React nie musi wołać go bezpośrednio. Główny przepływ z polecenia zostaje taki:

```txt
React -> Spring Boot API -> ASP.NET Core API
```

## Wywołania z Java/Spring

Spring może zamienić obecną klasę demo:

```txt
fitness-gym-backend-java/src/main/java/pl/fitcore/backend/integration/DemoTrainingModuleClient.java
```

na klienta HTTP, który będzie wołał:

```txt
POST http://localhost:5080/api/fitness/workouts/sessions/start
POST http://localhost:5080/api/fitness/workouts/sessions/{sessionId}/analyze
POST http://localhost:5080/api/fitness/plans
GET  http://localhost:5080/api/fitness/members/{memberId}/records
```

## Mapowanie funkcji

| Funkcja z tematu | ASP.NET Core endpoint |
| --- | --- |
| Plany treningowe | `POST /api/fitness/plans` |
| Biblioteka ćwiczeń | `GET /api/fitness/exercises` |
| Dziennik sesji treningowych | `POST /api/fitness/workouts/sessions/start` i `complete` |
| Rekordy osobiste | `GET /api/fitness/members/{memberId}/records` |
| Analiza postępów | `POST /api/fitness/workouts/sessions/{sessionId}/analyze` |
| Kalkulator BMI | `POST /api/fitness/calculators/bmi` |
| Kalkulator TDEE | `POST /api/fitness/calculators/tdee` |
| Kalkulator 1RM | `POST /api/fitness/calculators/one-rep-max` |
| Ranking | `GET /api/fitness/gamification/ranking` |

## Wspólny identyfikator

Spring i .NET powinny wymieniać dane po `memberId`, np. `M-1001`.
