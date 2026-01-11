# interview_task

Prosta aplikacja Spring Boot działająca jako **proxy do GitHub REST API v3**.

Aplikacja udostępnia endpoint umożliwiający pobranie **wszystkich repozytoriów danego użytkownika GitHub, które nie są forkami**, wraz z informacjami o branchach.

---

## Stos technologiczny

- **Java:** 25
- **Spring Boot:** 4.0.1
- **Spring MVC:** (`RestTemplate`)
- **Maven:** Wrapper włączony do repozytorium
- **Testy integracyjne:** JUnit 5 + WireMock

---

## Architektura

Aplikacja wykorzystuje prostą architekturę warstwową, ograniczoną do jednego pakietu zgodnie z wytycznymi:

- **Controller** – wystawia endpoint REST
- **Service** – zawiera logikę biznesową i filtrowanie forków
- **Client** – komunikuje się bezpośrednio z GitHub API

---

## Uruchamianie aplikacji

```bash
./mvnw spring-boot:run
```

---

## Endpoint API

### Pobranie repozytoriów (bez forków)

`GET /api/repositories/{username}`

### Przykładowe zapytanie

`curl -X GET http://localhost:8080/api/repositories/{username} -H "Accept: application/json"`

#### Przykładowa odpowiedź (200 OK)
```json
[
  {
    "repositoryName": "example-repo",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "a1b2c3d4"
      }
    ]
  }
]
```
Użytkownik nie istnieje (404)

Jeżeli użytkownik GitHub nie istnieje, aplikacja zwraca:

```json
{
  "status": 404,
  "message": "User not found on GitHub"
}
```

## Testy

Zgodnie z wymaganiami, do projektu dołączono wyłącznie testy integracyjne, które sprawdzają logikę biznesową bez użycia mocków

### Uruchamianie testów

```bash
./mvnw test
```
