# Przepływ React -> Spring API -> .NET API

Ten plik opisuje, gdzie pasuje czysty moduł Java w wymaganiach projektu.

## Warstwy

```txt
React
  |
  v
Spring Boot API (Java)
  |
  |-- fitness-gym-backend-java
  |     czyste klasy Java: członkowie, karnety, wejścia, zajęcia, rezerwacje
  |
  v
ASP.NET Core API (C#)
      plany treningowe, sesje treningowe, rekordy, kalkulatory, gamifikacja
```

## Mapowanie akcji z tematu

| Akcja użytkownika | Endpoint Spring API | Metoda w czystej Javie | Delegacja do .NET |
| --- | --- | --- | --- |
| Członek wchodzi na siłownię | `POST /api/access/checkin` | `checkIn(memberId, qrCode)` | `startSession(memberId)` |
| Członek rezerwuje zajęcia | `POST /api/classes/reservations` | `reserveClass(memberId, classId)` | brak |
| Członek wybiera karnet | `POST /api/plans/select` | `selectMembership(memberId, type)` | brak |
| Instruktor tworzy plan | `POST /api/members/{id}/training-plan` | `assignTrainingPlan(memberId, goal, level)` | `createTrainingPlan(...)` |
| Członek zaczyna trening | `POST /api/workouts/start` | `startWorkoutSession(memberId)` | `startSession(memberId)` |

## Co później zamienić

- `InMemoryFitnessRepository` można zamienić na repozytoria Spring Data JPA.
- `DemoTrainingModuleClient` można zamienić na klienta HTTP wywołującego ASP.NET Core.
- `FitnessBackendService` zostaje sercem logiki i może być normalnym beanem Springa.
