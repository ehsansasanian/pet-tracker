# Pet Tracking Service

A Kotlin/Spring Boot service that ingests live data from cat & dog trackers, stores it, and answers queries.

---

## Tech Stack

* Kotlin • JVM 21
* Spring Boot 3.4
* H2 in‑memory SQL

---

## Architecture & Layer Separation

*Influenced by DDD / Hexagonal architecture, but intentionally lightweight.*

| Layer                     | Responsibility                                 | Key artefacts                           |
|---------------------------|------------------------------------------------|-----------------------------------------|
| **Web / Presentation**    | HTTP, JSON, validation, RFC‑7807 error mapping | `PetController`, `RestExceptionHandler` |
| **Service / Application** | Transactions, orchestration, DTO mapping       | `PetServiceImpl`, commands & DTOs       |
| **Domain Model**          | Business rules & invariants                    | `PetEntity`, `PetType`, `TrackerType`   |
| **Data Access (Adapter)** | Persistence behind a port (`PetDao`)           | `PetDaoImpl`, `JpaPetRepository`        |

*Changing the storage technology only requires another **PetDao** implementation; no service or controller code
changes.*

---

## Getting Started

```
# 1.Clone & build
mvn clean package

# 2.Run locally
mvn spring-boot:run    # app starts on http://localhost:8080
```

### Test Suite

```
mvn test     # unit + slice + controller tests
```

---

## REST API

| Verb     | Path                             | Purpose                                    |
|----------|----------------------------------|--------------------------------------------|
| **POST** | `/api/pets`                      | Create a new pet (returns 201 + Location)  |
| **GET**  | `/api/pets`                      | List pets, optional \`?inZone=true         |
| **GET**  | `/api/pets/{id}`                 | Fetch one pet by id                        |
| **POST** | `/api/pets/{id}/tracking`        | Update in‑zone / lost‑tracker flags        |
| **GET**  | `/api/pets/outside-power-saving` | Aggregated counts of pets outside the zone |

### Sample cURL requests

```bash
# 1. Create a pet
curl -X POST http://localhost:8080/api/pets \
     -H "Content-Type: application/json" \
     -d '{
           "petType":"CAT",
           "trackerType":"CAT_SMALL",
           "ownerId":123,
           "inZone":true,
           "lostTracker":false
         }'

# 2. List pets (all)
curl http://localhost:8080/api/pets

# 2b. List pets outside the zone
curl http://localhost:8080/api/pets?inZone=false

# 3. Fetch a single pet
curl http://localhost:8080/api/pets/1

# 4. Update tracking for a pet
curl -X POST http://localhost:8080/api/pets/1/tracking \
     -H "Content-Type: application/json" \
     -d '{ "inZone": false, "lostTracker": true }'

# 5. Counts of pets outside the power‑saving zone
curl http://localhost:8080/api/pets/outside-power-saving | jq
```

Alternatively, you can use [Postman](https://www.postman.com/) to test the API. Please see ```./postman```

---

## Database

The default profile starts an **in‑memory H2** database.

---

© 2025 Pet Tracking Service – MIT Licence
