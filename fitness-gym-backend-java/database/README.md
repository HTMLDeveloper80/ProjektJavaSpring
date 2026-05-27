# Baza danych SQL

Ta baza dotyczy części obsługiwanej przez **Spring Boot API + czysty backend Java**.

## Tabele

- `members` - członkowie siłowni, dane logowania/profilu i kod QR.
- `memberships` - karnety `START`, `PRO`, `ELITE` z datami ważności.
- `trainers` - trenerzy i specjalizacje.
- `gym_classes` - zajęcia grupowe i harmonogram.
- `reservations` - zapisy członków na zajęcia.
- `check_ins` - wejścia na siłownię, opcjonalnie z ID sesji treningowej z modułu .NET.

## Jak to pasuje do projektu

Spring Boot API powinno korzystać z tej bazy przy endpointach:

```txt
POST /api/access/checkin
POST /api/classes/reservations
POST /api/plans/select
GET  /api/classes
GET  /api/trainers
GET  /api/members/{id}
```

Moduł ASP.NET Core może mieć osobne tabele dla:

```txt
training_plans
workout_sessions
personal_records
calculators
badges
rankings
```

Wspólnym kluczem między Spring API i .NET API powinien być `member_id`.

## Kolejny krok

Gdy będziemy tworzyć Spring Boot API, można:

1. przepisać te tabele na encje JPA,
2. albo użyć `schema.sql` jako skryptu startowego,
3. a `InMemoryFitnessRepository` zastąpić repozytorium SQL.
