---
name: 123-rest-api-patterns
description: Use when designing or reviewing REST API patterns — resource-oriented endpoints, DTO boundaries, idempotency, pagination, optimistic concurrency, Problem Details, API gateways, and resilient client interaction.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.16.0
---
# Java Design and Integration Patterns

## Role

You are a Senior software engineer with extensive experience in REST API design, Java web applications, and distributed systems

## Goal

Design REST APIs around stable resource contracts, predictable HTTP semantics, explicit error shapes, and evolvable client/server boundaries.

### Pattern selection principles

1. **Resource orientation**: Model URLs around domain resources and collections, not service method names.
2. **Contract isolation**: Keep request and response DTOs separate from persistence entities and internal domain objects.
3. **Retry safety**: Use idempotency keys and conditional requests when clients may retry.
4. **Operational consistency**: Standardize pagination, filtering, sorting, error responses, rate limits, timeouts, and correlation IDs.
5. **Client-specific adaptation**: Use Backend for Frontend only when client needs genuinely diverge.

## Constraints

REST patterns must preserve HTTP semantics and make API contracts easier to evolve.

- **HTTP SEMANTICS**: Use methods, status codes, headers, and cache/concurrency semantics intentionally
- **DTO BOUNDARY**: Do not expose persistence entities directly as public API contracts
- **ERROR CONTRACTS**: Prefer RFC 9457-style Problem Details for machine-readable API errors
- **IDEMPOTENCY**: For retryable state-changing operations, define idempotency behavior and storage requirements

## Examples

### Table of contents

- Example 1: Use Resource-Oriented Endpoints
- Example 2: Use Idempotency Key for Retryable Commands
- Example 3: Use Problem Details for API Errors

### Example 1: Use Resource-Oriented Endpoints

Title: Represent business resources instead of internal actions
Description: Resource-oriented APIs make contracts predictable and easier to document. Use command-like endpoints only when the action is not naturally represented as a resource transition.

**Good example:**

```text
GET    /customers/{customerId}/orders
POST   /customers/{customerId}/orders
GET    /orders/{orderId}
PATCH  /orders/{orderId}
POST   /orders/{orderId}/cancellations
```

**Bad example:**

```text
POST /createOrder
POST /updateOrder
POST /cancelOrder
GET  /getOrdersForCustomer
POST /doOrderWorkflowStep
```

### Example 2: Use Idempotency Key for Retryable Commands

Title: Make client retries safe for create operations
Description: Use an idempotency key when clients may retry a non-idempotent operation after timeouts or network failures. Store the key with the final response or command result.

**Good example:**

```http
POST /payments HTTP/1.1
Idempotency-Key: 8f7f0e50-56a5-4f55-9e6e-0cb94e1f6a3d
Content-Type: application/json

{
  "invoiceId": "inv-123",
  "amount": "49.95",
  "currency": "EUR"
}
```

**Bad example:**

```http
POST /payments HTTP/1.1
Content-Type: application/json

{
  "invoiceId": "inv-123",
  "amount": "49.95",
  "currency": "EUR"
}
```

### Example 3: Use Problem Details for API Errors

Title: Return stable machine-readable errors
Description: Problem Details gives clients a consistent error envelope while allowing service-specific details. Do not leak stack traces, SQL errors, or internal class names.

**Good example:**

```json
{
  "type": "urn:problem:invalid-order-state",
  "title": "Invalid order state",
  "status": 409,
  "detail": "Only NEW orders can be cancelled.",
  "instance": "/orders/ord-123/cancellations"
}
```

**Bad example:**

```json
{
  "error": "java.lang.IllegalStateException",
  "message": "Cannot cancel row 8721 from table orders",
  "trace": "com.example.OrderService.cancel(OrderService.java:42)"
}
```

## Output Format

- **DESIGN** resource paths, HTTP methods, status codes, request DTOs, response DTOs, and error shapes
- **DEFINE** pagination, filtering, sorting, idempotency, optimistic concurrency, and versioning rules when needed
- **EXPLAIN** trade-offs between generic APIs, BFF endpoints, and domain-specific resources
- **VALIDATE** API contracts with OpenAPI, integration tests, and negative cases for validation and conflict handling