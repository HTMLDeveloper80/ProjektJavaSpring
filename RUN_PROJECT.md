# Jak uruchomić cały projekt

Projekt ma teraz cztery części:

```txt
fitness-gym-frontend       React
fitness-gym-api-spring     Spring Boot API
fitness-gym-backend-java   czysta Java, logika domenowa dla Springa
fitness-gym-api-dotnet     ASP.NET Core API, moduł treningowy
```

## 1. Uruchom ASP.NET Core API

W pierwszym terminalu:

```powershell
.\run-dotnet-api.ps1
```

Adres:

```txt
http://localhost:5080/api/fitness
```

## 2. Uruchom Spring Boot API

W drugim terminalu:

```powershell
.\run-spring-api.ps1
```

Adres:

```txt
http://localhost:8080/api
```

Spring przy starcie odświeża lokalnie moduł `fitness-gym-backend-java`, a potem używa ASP.NET Core API do funkcji treningowych.

Baza SQL Springa zapisuje dane lokalnie tutaj:

```txt
fitness-gym-api-spring/data/fitcore-db.mv.db
```

Konsola bazy jest dostępna po uruchomieniu Springa:

```txt
http://localhost:8080/h2-console
```

Parametry logowania do konsoli:

```txt
JDBC URL: jdbc:h2:file:./data/fitcore-db
User: sa
Password:
```

## 3. Uruchom frontend React

W trzecim terminalu:

```powershell
cd .\fitness-gym-frontend
node.exe --preserve-symlinks --preserve-symlinks-main .\node_modules\vite\bin\vite.js --host 127.0.0.1 --port 5173
```

Adres:

```txt
http://127.0.0.1:5173
```

## Szybki test API

```powershell
Invoke-WebRequest http://localhost:8080/api/health -UseBasicParsing
Invoke-WebRequest http://localhost:5080/api/fitness/health -UseBasicParsing
```
