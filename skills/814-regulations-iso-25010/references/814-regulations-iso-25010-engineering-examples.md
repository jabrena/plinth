---
name: 814-regulations-iso-25010-engineering-examples
description: Use as Java-focused ISO/IEC 25010:2023 engineering examples covering all nine product-quality characteristics for structured, repeatable Java Enterprise quality-attribute review.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.19.0
---
# ISO/IEC 25010:2023 Quality Model Guidance for Java Enterprise Engineering

## Role

You are a senior Java enterprise architect and quality reviewer who translates ISO/IEC 25010:2023 product-quality characteristics into concrete Java engineering examples and reviewable evidence patterns

## Goal

Apply these ISO/IEC 25010:2023-aware examples after the chapters-summary reference has been read and the target Java enterprise system's review scope is understood.

These examples are not certification advice, compliance advice, conformity advice, or audit conclusions. They show engineering patterns that help Java teams create reviewable evidence for architecture, product, security, platform, operations, and accountable business owners.

Use this examples reference together with `references/814-regulations-iso-25010-chapters-summary.md`.

## Constraints

Translate ISO/IEC 25010:2023 quality characteristics into engineering controls and evidence for Java enterprise systems without replacing qualified architecture, product, security, or business-owner review, and without producing certification, compliance, conformity, or audit conclusions.

- **NOT CERTIFICATION, COMPLIANCE, OR CONFORMITY ADVICE**: Treat the examples as engineering control patterns and escalation aids, not certification readiness, compliance approval, audit findings, or final conformity decisions
- **EVIDENCE FIRST**: Prefer reviewable evidence over claims that a quality characteristic is satisfied
- **NINE-CHARACTERISTIC COVERAGE**: Match examples to all nine characteristics — Functional Suitability, Performance Efficiency, Compatibility, Interaction Capability, Reliability, Security, Maintainability, Flexibility, and Safety
- **OWNER HANDOFFS**: Include architecture, product, security, platform, operations, and accountable business owners in control records when a finding requires a decision beyond engineering review
- **RELEASE READINESS**: Do not mark a Java enterprise system quality-reviewed when scope, evidence, or per-characteristic findings are undocumented, untested, or unresolved

## Examples

### Table of contents

- Example 1: Trace acceptance criteria to implementation and tests
- Example 2: Detect N+1 queries and document capacity limits
- Example 3: Version and deprecate API contracts safely
- Example 4: Return self-descriptive, learnable API errors
- Example 5: Apply circuit breakers, retries, and timeouts on outbound calls
- Example 6: Enforce authorization and keep secrets out of source and logs
- Example 7: Keep module boundaries and test pyramid healthy
- Example 8: Externalize configuration and keep services stateless for horizontal scaling
- Example 9: Default to fail-safe and surface hazard warnings before irreversible actions

### Example 1: Trace acceptance criteria to implementation and tests

Title: Functional Suitability: completeness, correctness, appropriateness
Description: Use this pattern to prove that acceptance criteria map to concrete controller/service methods and automated tests, and that domain-model edge cases are covered.

**Good example:**

```java
@Test
void rejectsOrderWhenInventoryInsufficient() {
    Order order = OrderFixtures.orderFor(Sku.of("SKU-42"), 5);
    inventory.setAvailable(Sku.of("SKU-42"), 2);

    OrderResult result = orderService.place(order);

    assertThat(result.status()).isEqualTo(OrderStatus.REJECTED_INSUFFICIENT_INVENTORY);
    assertThat(result.reasonCode()).isEqualTo("INVENTORY_INSUFFICIENT");
}

@Test
void acceptsOrderAtExactAvailableQuantityBoundary() {
    Order order = OrderFixtures.orderFor(Sku.of("SKU-42"), 2);
    inventory.setAvailable(Sku.of("SKU-42"), 2);

    OrderResult result = orderService.place(order);

    assertThat(result.status()).isEqualTo(OrderStatus.ACCEPTED);
}
```

**Bad example:**

```java
@Test
void orderWorks() {
    OrderResult result = orderService.place(OrderFixtures.anyOrder());
    assertThat(result).isNotNull();
}
```


### Example 2: Detect N+1 queries and document capacity limits

Title: Performance Efficiency: time behaviour, resource utilization, capacity
Description: Use this pattern when reviewing data access and load-test evidence. Fetch joins or explicit batch loading avoid N+1 query patterns, and capacity limits are documented rather than assumed.

**Good example:**

```java
@Query("""
    SELECT o FROM Order o
    JOIN FETCH o.lineItems li
    JOIN FETCH li.product
    WHERE o.customerId = :customerId
    """)
List<Order> findOrdersWithLineItemsByCustomer(@Param("customerId") Long customerId);

// capacity evidence recorded alongside the load test suite
// docs/performance/order-service-load-test-2026-08-01.md:
//   p95 latency: 180ms at 250 rps sustained for 30 minutes
//   documented capacity limit: 300 rps before connection-pool saturation
```

**Bad example:**

```java
List<Order> orders = orderRepository.findByCustomerId(customerId);
for (Order order : orders) {
    order.getLineItems().forEach(li -> li.getProduct().getName()); // lazy-loads per item
}
// no load test evidence, no documented capacity limit
```


### Example 3: Version and deprecate API contracts safely

Title: Compatibility: co-existence, interoperability
Description: Use this pattern when reviewing REST or message contracts that must co-exist with other deployed versions and interoperate with existing consumers.

**Good example:**

```java
@RestController
@RequestMapping("/api/v2/orders")
class OrderControllerV2 {

    @GetMapping("/{orderId}")
    OrderResponseV2 getOrder(@PathVariable String orderId) {
        return orderQueryService.findV2(orderId);
    }
}

// v1 remains available and documented as deprecated with a sunset date
@RestController
@RequestMapping("/api/v1/orders")
@Deprecated(since = "2026-06-01", forRemoval = true)
class OrderControllerV1 {
    // Sunset: 2027-01-01. Migration guide: docs/api/order-v1-to-v2-migration.md
}
```

**Bad example:**

```java
@RestController
@RequestMapping("/api/orders")
class OrderController {
    // breaking field rename shipped directly into the only version,
    // no deprecation window, no migration guide
    @GetMapping("/{orderId}")
    OrderResponse getOrder(@PathVariable String orderId) { ... }
}
```


### Example 4: Return self-descriptive, learnable API errors

Title: Interaction Capability: recognizability, learnability, operability, user error protection, self-descriptiveness
Description: Use this pattern when reviewing input validation and error handling. Clients and operators should receive clear, actionable 4xx responses instead of stack traces or ambiguous 500s.

**Good example:**

```java
@ExceptionHandler(ConstraintViolationException.class)
ResponseEntity<ApiError> handleValidation(ConstraintViolationException ex) {
    ApiError error = ApiError.builder()
            .code("VALIDATION_FAILED")
            .message("One or more fields are invalid")
            .fieldErrors(ex.getConstraintViolations().stream()
                    .map(v -> new FieldError(v.getPropertyPath().toString(), v.getMessage()))
                    .toList())
            .documentationLink("https://docs.example.com/errors/VALIDATION_FAILED")
            .build();
    return ResponseEntity.badRequest().body(error);
}
```

**Bad example:**

```java
@ExceptionHandler(Exception.class)
ResponseEntity<String> handleAny(Exception ex) {
    return ResponseEntity.status(500).body(ex.toString()); // leaks stack trace, no guidance
}
```


### Example 5: Apply circuit breakers, retries, and timeouts on outbound calls

Title: Reliability: faultlessness, availability, fault tolerance, recoverability
Description: Use this pattern when reviewing outbound HTTP or messaging calls, health/readiness probes, and recovery procedures.

**Good example:**

```java
@CircuitBreaker(name = "paymentGateway", fallbackMethod = "fallbackAuthorize")
@Retry(name = "paymentGateway")
@TimeLimiter(name = "paymentGateway")
CompletableFuture<AuthorizationResult> authorize(PaymentRequest request) {
    return CompletableFuture.supplyAsync(() -> paymentClient.authorize(request));
}

AuthorizationResult fallbackAuthorize(PaymentRequest request, Throwable t) {
    log.warn("Payment gateway unavailable, queuing for idempotent retry: {}", request.idempotencyKey());
    return AuthorizationResult.queuedForRetry(request.idempotencyKey());
}
```

**Bad example:**

```java
AuthorizationResult authorize(PaymentRequest request) {
    return paymentClient.authorize(request); // no timeout, no retry, no circuit breaker
}
```


### Example 6: Enforce authorization and keep secrets out of source and logs

Title: Security: confidentiality, integrity, non-repudiation, accountability, authenticity, resistance
Description: Use this pattern when reviewing endpoint authorization, secrets handling, and audit logging for accountability.

**Good example:**

```java
@PreAuthorize("hasRole('ORDER_ADMIN') and #orderId == authentication.details.orderId")
@PostMapping("/{orderId}/cancel")
ResponseEntity<Void> cancelOrder(@PathVariable String orderId, Authentication authentication) {
    auditLog.record(AuditEvent.of("ORDER_CANCEL", authentication.getName(), orderId));
    orderService.cancel(orderId);
    return ResponseEntity.noContent().build();
}

// secrets loaded from an approved secret store, never hardcoded
@Value("${payment.gateway.api-key}")
private String apiKey; // sourced from Vault/Secrets Manager, never committed
```

**Bad example:**

```java
@PostMapping("/{orderId}/cancel")
ResponseEntity<Void> cancelOrder(@PathVariable String orderId) {
    // no authorization check
    orderService.cancel(orderId);
    return ResponseEntity.noContent().build();
}

private static final String API_KEY = "sk_live_51H8x..."; // hardcoded secret
```


### Example 7: Keep module boundaries and test pyramid healthy

Title: Maintainability: modularity, reusability, analysability, modifiability, testability
Description: Use this pattern when reviewing package structure, coupling, and the shape of the automated test suite.

**Good example:**

```markdown
Maintainability review notes

- Module boundaries: `order-domain` has no dependency on `order-web`; `order-web` depends on `order-domain` only through a port interface.
- Coupling: static-analysis afferent/efferent coupling report attached; no cyclic package dependencies detected.
- Test pyramid: 420 unit tests, 65 integration tests, 12 acceptance tests; ratio consistent with the project's testing strategy.
- Testability: domain services take collaborators through constructor injection; no static singletons in business logic.
- Complexity: cyclomatic complexity report shows no method above the agreed threshold of 10.
```

**Bad example:**

```markdown
Maintainability review notes

- Everything lives in one `util` package.
- OrderService reaches directly into the web layer's static helper classes.
- No test-pyramid or complexity evidence attached.
```


### Example 8: Externalize configuration and keep services stateless for horizontal scaling

Title: Flexibility: adaptability, scalability, installability, replaceability
Description: Use this pattern when reviewing horizontal-scaling readiness, infrastructure-as-code, and the replaceability of third-party integrations.

**Good example:**

```yaml
# application.yml — externalized per environment, no in-code environment branching
payment:
  gateway:
    base-url: ${PAYMENT_GATEWAY_URL}
    provider: ${PAYMENT_GATEWAY_PROVIDER:stripe} # swappable via config, not code change

server:
  session:
    store: none # stateless; session state kept in shared cache, safe to scale horizontally
```

**Bad example:**

```java
String gatewayUrl = env.equals("prod")
        ? "https://payments.prod.internal"
        : "https://payments.staging.internal"; // hardcoded per-environment branching in code

// in-memory session map on the instance — cannot scale horizontally without sticky sessions
private static final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();
```


### Example 9: Default to fail-safe and surface hazard warnings before irreversible actions

Title: Safety: operational constraint, risk identification, fail safe, hazard warning, safe integration
Description: Use this pattern for Java systems with real-world operational effects, where an action is irreversible or high-impact — for example bulk data deletion, financial settlement, or automated infrastructure changes.

**Good example:**

```java
RefundResult processRefund(RefundRequest request) {
    if (request.amount().compareTo(request.originalCharge().amount()) > 0) {
        throw new HazardousOperationException(
                "Refund amount exceeds original charge; blocked pending manual review",
                "REFUND_EXCEEDS_ORIGINAL_CHARGE");
    }
    if (!request.hasApproval() && request.amount().isGreaterThan(Money.of(1000, "USD"))) {
        return RefundResult.pendingApproval(request.id(), "High-value refund requires approver sign-off");
    }
    return refundGateway.execute(request); // proceeds only once guardrails pass
}
```

**Bad example:**

```java
RefundResult processRefund(RefundRequest request) {
    return refundGateway.execute(request); // no bounds check, no approval gate, no hazard warning
}
```


## Output Format

- **MATCH** the relevant example patterns for each of the nine ISO/IEC 25010:2023 characteristics under review: functional-suitability traceability, performance-efficiency capacity evidence, compatibility API versioning, interaction-capability self-descriptive errors, reliability resilience patterns, security authorization and secrets, maintainability module boundaries, flexibility configuration externalization, or safety fail-safe and hazard-warning controls
- **RECOMMEND** engineering controls: acceptance-criteria traceability, load/capacity test evidence, API versioning and deprecation records, self-descriptive error handling, resilience patterns, authorization and secrets management, module-boundary and test-pyramid review, configuration externalization, and fail-safe/hazard-warning guardrails
- **REPORT** conclusions as engineering evidence and action items, never as certification advice, compliance advice, conformity advice, or audit conclusions


## Safeguards

- **OWNER ESCALATION**: Treat ISO/IEC 25010:2023 certification, compliance, and conformity determinations as qualified owner decisions, not engineering conclusions
- **EVIDENCE OVER CLAIMS**: Never mark a quality characteristic satisfied without reviewable evidence; record it as an evidence gap instead
- **SCOPE DISCIPLINE**: Base findings only on the Java system, module, or delivery pipeline actually under review; do not generalize findings across unrelated systems