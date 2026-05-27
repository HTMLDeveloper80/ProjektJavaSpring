# FitCore Spring Boot API

To jest główna brama HTTP dla frontendu React.

Spring API obsługuje zakres z tematu projektu:

- członkowie i karnety,
- zajęcia grupowe i rezerwacje,
- harmonogram trenerów,
- kontrola wejść przez QR,
- integracja z modułem treningowym.

Logika biznesowa nie jest pisana bezpośrednio w kontrolerach. Spring korzysta z modułu:

```txt
../fitness-gym-backend-java
```

## Uruchomienie

W katalogu głównym projektu:

```powershell
.\run-spring-api.ps1
```

Adres API:

```txt
http://localhost:8080/api
```

Frontend React jest już ustawiony domyślnie na:

```txt
http://localhost:8080/api
```

## Główne endpointy

```txt
GET  /api/health
POST /api/auth/register
POST /api/auth/login
POST /api/auth/client-panel
GET  /api/classes
POST /api/classes/reservations
GET  /api/trainers
POST /api/plans/select
POST /api/access/checkin
POST /api/workouts/start
POST /api/members/{memberId}/training-plan
```

## Baza SQL

Spring API korzysta z plikowej bazy H2, więc dane zostają po restarcie aplikacji.

```txt
fitness-gym-api-spring/data/fitcore-db.mv.db
```

Schemat i dane startowe są tutaj:

```txt
src/main/resources/schema.sql
src/main/resources/data.sql
```

Konsola H2:

```txt
http://localhost:8080/h2-console
```

Dane logowania:

```txt
JDBC URL: jdbc:h2:file:./data/fitcore-db
User: sa
Password:
```
