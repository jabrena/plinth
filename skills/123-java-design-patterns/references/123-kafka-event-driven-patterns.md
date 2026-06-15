---
name: 123-kafka-event-driven-patterns
description: Use when designing or reviewing Kafka and event-driven patterns — event schemas, partitioning keys, consumer groups, idempotent consumers, retry topics, dead-letter topics, outbox, CDC, sagas, CQRS projections, and event sourcing.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.16.0
---
# Java Design and Integration Patterns

## Role

You are a Senior software engineer with extensive experience in Kafka, event-driven architecture, and Java messaging systems

## Goal

Design Kafka and event-driven systems around reliable delivery, stable event contracts, explicit ordering boundaries, replay safety, and observable failure handling.

### Pattern selection principles

1. **Event purpose first**: Distinguish event notification, event-carried state transfer, command messages, and audit/event-sourcing streams.
2. **Key for ordering**: Choose partition keys from business ordering needs, usually aggregate id, account id, customer id, or tenant id.
3. **At-least-once by default**: Design consumers as idempotent because duplicates are normal in reliable messaging.
4. **Schema evolution**: Version event contracts and maintain backward/forward compatibility.
5. **Failure isolation**: Use retries, dead-letter topics, and replay procedures instead of losing or blocking messages silently.

## Constraints

Kafka patterns must make delivery, ordering, schema evolution, and failure behavior explicit.

- **IDEMPOTENCY REQUIRED**: Consumers that change state must handle duplicate records safely
- **KEY INTENTIONALLY**: Select partition keys based on ordering and load distribution, not convenience
- **NO HIDDEN SCHEMAS**: Events must have explicit names, versions, and compatibility rules
- **FAILURE PATHS**: Define retry, DLT, alerting, and replay behavior for poison records

## Examples

### Table of contents

- Example 1: Use Event-Carried State Transfer for Local Read Models
- Example 2: Use Transactional Outbox for Reliable Publishing

### Example 1: Use Event-Carried State Transfer for Local Read Models

Title: Publish enough state for consumers to update their own views
Description: Use this pattern when consumers need autonomous local projections. Avoid publishing entire internal entities or fields without consumer value.

**Good example:**

```json
{
  "eventId": "evt-1001",
  "eventType": "OrderConfirmed",
  "eventVersion": 1,
  "occurredAt": "2026-06-01T10:15:30Z",
  "orderId": "ord-123",
  "customerId": "cus-456",
  "total": {
    "amount": "49.95",
    "currency": "EUR"
  }
}
```

**Bad example:**

```json
{
  "eventType": "OrderChanged",
  "payload": {
    "entityDump": "...all database columns, including internal flags..."
  }
}
```

### Example 2: Use Transactional Outbox for Reliable Publishing

Title: Persist domain state and outgoing events atomically
Description: Use outbox when a service updates a database and must publish an event without a dual-write race. A relay or CDC pipeline publishes outbox rows to Kafka.

**Good example:**

```text
Transaction:
  1. Update orders table: status = CONFIRMED
  2. Insert outbox row: event_type = OrderConfirmed, aggregate_id = ord-123
  3. Commit

Relay:
  4. Read unpublished outbox rows
  5. Publish to Kafka with key = aggregate_id
  6. Mark row published or let CDC advance the offset
```

**Bad example:**

```text
1. Update orders table and commit
2. Publish OrderConfirmed to Kafka
3. If the process crashes between steps, the database changed but no event exists
```

## Output Format

- **DESIGN** event name, payload, schema version, topic, key, headers, and consumer-group boundaries
- **DEFINE** idempotency, retries, DLT, replay, ordering, and schema evolution behavior
- **APPLY** outbox, inbox, saga, projection, or event-sourcing patterns only when their reliability or audit value is needed
- **TEST** with realistic broker behavior, duplicate delivery, poison messages, and schema compatibility checks