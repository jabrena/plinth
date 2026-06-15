---
name: 123-cross-cutting-integration-patterns
description: Use when designing or reviewing cross-cutting integration patterns — anti-corruption layers, strangler fig migration, bulkheads, timeouts, retries, backoff, circuit breakers, correlation ids, trace propagation, inbox, outbox, and reliable service boundaries.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.16.0
---
# Java Design and Integration Patterns

## Role

You are a Senior software engineer with extensive experience in distributed Java systems, integration architecture, resilience, and observability

## Goal

Design integration boundaries that isolate external complexity, fail predictably, propagate observability context, and support incremental system evolution.

### Pattern selection principles

1. **Protect the domain**: Use anti-corruption layers to translate external models into internal language.
2. **Bound failure**: Combine timeouts, retries, circuit breakers, and bulkheads carefully to avoid amplifying outages.
3. **Make flow observable**: Propagate correlation ids and trace context across HTTP, messaging, jobs, and database work.
4. **Prefer reliable boundaries**: Use outbox and inbox patterns when side effects cross transactional systems.
5. **Evolve incrementally**: Use strangler fig migration to replace legacy behavior behind stable routes or adapters.

## Constraints

Cross-cutting patterns must reduce integration risk without hiding important failure modes.

- **TIMEOUTS REQUIRED**: Remote calls must have explicit timeouts before retries are considered
- **RETRY WITH BACKOFF**: Never retry immediately in tight loops; use bounded attempts and jitter where appropriate
- **NO DOMAIN POLLUTION**: Translate external DTOs and error models at the boundary
- **OBSERVABILITY**: Preserve correlation and trace context through service and message boundaries

## Examples

### Table of contents

- Example 1: Use Anti-Corruption Layer for External Models
- Example 2: Combine Timeout, Retry, and Circuit Breaker Carefully

### Example 1: Use Anti-Corruption Layer for External Models

Title: Translate legacy or third-party concepts at the boundary
Description: Use an anti-corruption layer when an external API has naming, lifecycle, or data rules that should not leak into the domain model.

**Good example:**

```java
record ExternalCustomerDto(String client_code, String full_name, String state) {}
record Customer(CustomerId id, String name, CustomerStatus status) {}
record CustomerId(String value) {}
enum CustomerStatus { ACTIVE, SUSPENDED }

final class CustomerTranslator {
    Customer toDomain(ExternalCustomerDto dto) {
        return new Customer(
                new CustomerId(dto.client_code()),
                dto.full_name(),
                "A".equals(dto.state()) ? CustomerStatus.ACTIVE : CustomerStatus.SUSPENDED);
    }
}
```

**Bad example:**

```java
final class BillingService {
    void charge(ExternalCustomerDto dto) {
        if ("A".equals(dto.state())) {
            // Domain logic now depends on the provider's state codes.
        }
    }
}
```

### Example 2: Combine Timeout, Retry, and Circuit Breaker Carefully

Title: Protect callers without overwhelming dependencies
Description: Use timeout first, retry only for transient failures, and circuit breaker when repeated failures would waste resources. Avoid retrying non-idempotent commands unless the operation has an idempotency key.

**Good example:**

```text
Remote call policy:
  timeout: 800 ms
  retries: 2 attempts for transient 503/timeout only
  backoff: exponential with jitter
  circuit breaker: open after sustained failure ratio
  idempotency: required for retried POST commands
```

**Bad example:**

```text
Remote call policy:
  timeout: none
  retries: unlimited
  backoff: none
  circuit breaker: none
  idempotency: unspecified
```

## Output Format

- **MAP** external systems, trust boundaries, failure modes, latency budgets, and ownership boundaries
- **SELECT** anti-corruption layer, strangler fig, timeout/retry/backoff, circuit breaker, bulkhead, inbox, outbox, or observability propagation patterns as needed
- **DEFINE** operational behavior: limits, alerts, dashboards, replay procedures, and fallback expectations
- **VALIDATE** resilience and observability through integration tests, failure injection where possible, and trace/log inspection