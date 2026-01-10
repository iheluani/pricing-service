# Pricing Service – Inditex Technical Test

Spring Boot backend service that resolves the applicable price for a product, brand and application date.
When multiple prices apply, the service selects the one with the highest priority, following the business rules defined in the exercise.

The solution has been implemented using clean / hexagonal architecture principles, focusing on clarity, separation of concerns and testability, as expected for a Technical Lead role.

---

## Branch information

This repository contains the solution developed in the feature branch:

`feature/pricing-service`

Please make sure to checkout this branch to review the full implementation, including:
- Business logic
- REST API
- Persistence layer
- Integration tests
- Documentation

The `main` branch is intentionally kept clean and represents the baseline before merging the solution, following a standard Git workflow.

The implementation is currently proposed via `an open Pull Request` for review.

---

## Business Context

Given:
- a product identifier (`productId`)
- a brand identifier (`brandId`)
- an application date (`applicationDate`)

The service returns the price that:
1. Matches the product and brand
2. Is valid within the given date range
3. Has the highest priority when more than one price applies

---

## API

### Get applicable price

**Endpoint**:
GET `/prices/applicable`

**Query parameters**

| Name | Type | Required | Description |
|------|------|----------|-------------|
| applicationDate | String | Yes | ISO-8601 local datetime (e.g. 2020-06-14T16:00:00) |
| productId | Long | Yes | Product identifier |
| brandId | Long | Yes | Brand identifier |

**Example request**
```bash
curl "http://localhost:8080/prices/applicable?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"
```

**Example response**
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}
```

**Error responses**
- 400 Bad Request → invalid parameters or date format
- 404 Not Found → no applicable price found

---

## How to Run

Run locally:
```
mvn spring-boot:run
```
The application will start on:
```
http://localhost:8080
```
---

## In-memory Database (H2)

This project uses an H2 in-memory database to simplify execution and ensure deterministic tests.

**H2 Console**
```bash 
http://localhost:8080/h2-console
```

**Connection details**
- JDBC URL: jdbc:h2:mem:pricingdb
- User: sa
- Password: (empty)

Initial pricing data is automatically loaded from:
```css
src/main/resources/data.sql
```

---

## Testing Strategy

**Unit tests**
```
mvn test
```

**Integration tests**
```
mvn verify
```

**Full build (recommended)**
```
mvn clean install
```

Integration tests validate the endpoint end-to-end using:
- Spring Boot test context
- H2 in-memory database
- Real JPA repositories
- MockMvc for REST calls

---

## Architecture Overview

The solution follows a hexagonal / clean architecture approach.

## Architecture (Hexagonal)

```text
(IN) Adapters / Delivery
────────────────────────────────────
HTTP
│
▼
PriceController (REST)
└─ PriceResponseDto
│
│ calls (Port In)
▼
Application / Use Case
────────────────────────────────────
GetApplicablePriceService
│
│ depends on abstraction (Port Out)
▼
PriceRepositoryPort (interface)
│
│ implemented by
▼
(OUT) Adapters / Infrastructure
────────────────────────────────────
PriceRepositoryAdapter (JPA)
├─ SpringDataPriceRepository
└─ PriceJpaEntity
│
│ queries
▼
H2 / Database
```

#### Layers:

**Domain**  
Contains the core business model and business exceptions.  
No framework dependencies.

**Application**  
Contains the use case and inbound/outbound ports.  
Orchestrates the business rules.

**Infrastructure**  
Contains framework-specific adapters:
- REST controllers and exception handling
- JPA persistence and Spring Data repositories
- Spring configuration and bootstrap data

**Flow**
- The REST adapter receives the request
- Delegates to the application use case
- The use case queries the repository through an outbound port
- The persistence adapter resolves the data and maps it to the domain model

---

## Price Resolution Logic

A price is considered applicable when:
- `brandId` and `productId` match
- `startDate <= applicationDate <= endDate`

If multiple prices match:
1. Sort by priority descending
2. Tie-break by startDate descending
3. Return the first result

The repository limits the result to one element to avoid non-unique result issues.

---

## Notes

- Dates are returned as ISO-8601 strings for API stability and clarity
- Integration tests cover all scenarios defined in the exercise
- Mockito may emit warnings related to dynamic Java agents on recent JDK versions; these warnings do not affect test execution

---

## Author

Ismael Heluani  
Technical Lead Backend Candidate
