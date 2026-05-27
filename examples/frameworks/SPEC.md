# Spec

## Problem Statement

### Scenario REST

```bash
Create a basic endpoint POST /api/v1/sum which require an object which has 2 properties param1 as integer and param2 as integer add the controller in controller package
```

## OpenAPI contract

Shared contract: [`openapi.yaml`](openapi.yaml)

### Scenario Security

```bash
Add a protected admin endpoint GET /api/v1/admin/ping that returns {"status":"ok"}.
Only users with role ADMIN may access it.

Keep POST /api/v1/sum public (no authentication required).

Use HTTP Basic authentication with in-memory test users:
- user / user  → role USER
- admin / admin → role ADMIN

Expected behavior:
- GET /api/v1/admin/ping without credentials → 401 Unauthorized
- GET /api/v1/admin/ping with user:user → 403 Forbidden
- GET /api/v1/admin/ping with admin:admin → 200 OK, body {"status":"ok"}

Add controller tests that verify the three cases above.
Extend openapi.yaml with a basicAuth security scheme and document /api/v1/admin/ping.
```

### Scenario messaging

```bash
Add Kafka event messaging around the existing sum flow.

When POST /api/v1/sum returns successfully, publish a SumCalculated event to topic sum-calculated
with JSON payload: param1, param2, result (the computed sum).

Add a consumer on the same topic that handles SumCalculated events (store the last received event
in an in-memory component so tests can assert delivery).

Use framework-native Kafka support:
- Spring Boot: spring-kafka (KafkaTemplate producer, @KafkaListener consumer)
- Quarkus: SmallRye Reactive Messaging (@Channel / @Incoming / @Outgoing)
- Micronaut: micronaut-kafka (@KafkaClient producer, @KafkaListener consumer)

Configure Kafka with Testcontainers in integration tests:
- Start a KafkaContainer and wire bootstrap servers into test configuration
- Create the sum-calculated topic in test configuration (e.g. Spring Boot @TestConfiguration with NewTopic)
- Add an integration test that POSTs to /api/v1/sum and asserts the consumer received the expected event

Expected behavior:
- POST /api/v1/sum with {"param1": 10, "param2": 32} → 200 OK and a SumCalculated event on sum-calculated with result 42
- Consumer processes the event without error

Keep existing REST and Security scenarios unchanged.
```

