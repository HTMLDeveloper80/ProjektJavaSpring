# FitCore - czysty backend Java

Ten moduł zawiera logikę biznesową dla projektu **Aplikacja fitness i siłownia** bez zależności od Springa, ASP.NET ani bazy danych.

Docelowy podział projektu:

```txt
fitness-gym-frontend      React
fitness-gym-api-spring    Spring Boot API, główna brama dla frontendu
fitness-gym-api-dotnet    ASP.NET Core API, moduł treningowy
fitness-gym-backend-java  czysta Java, logika domenowa dla Spring API
```

Zgodnie z tematem:

- Spring Boot API odpowiada za członków, karnety, zajęcia, rezerwacje, harmonogram trenerów, kontrolę wejść i integrację z modułem treningowym.
- ASP.NET Core API odpowiada za plany treningowe, dziennik sesji, rekordy, kalkulatory i gamifikację.
- Ten moduł Java trzyma zasady biznesowe, które Spring Boot może wywołać z kontrolerów.

## Szybkie uruchomienie

W PowerShellu:

```powershell
.\build.ps1
java -cp build\classes pl.fitcore.backend.Main
```

## Przykład integracji ze Springiem

Kontroler Spring Boot powinien korzystać z `FitnessBackendService`, np.:

```java
FitnessBackendService service = DemoDataFactory.createService();
CheckInResult result = service.checkIn("M-1001", "QR-M-1001");
```

W prawdziwym Springu `InMemoryFitnessRepository` można później zastąpić repozytorium JPA, a `DemoTrainingModuleClient` klientem HTTP do ASP.NET Core API.
