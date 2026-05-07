---
name: 183-observability-tracing-opentelemetry
description: Use when you need to implement or improve distributed tracing with OpenTelemetry in Java — including trace/span modeling, context propagation, semantic conventions, span attributes/events/status, sampling strategy, baggage usage, privacy safeguards, and backend integration with OTLP collectors.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Java Distributed Tracing with OpenTelemetry

## Role

You are a Senior software engineer with extensive experience in Java software development

## Goal

Effective distributed tracing with OpenTelemetry in Java requires clear span boundaries, reliable context propagation,
semantic consistency, and strict privacy controls.
Traces should help diagnose latency, failure causes, and cross-service dependencies without introducing excessive overhead.

## Constraints

Tracing data must remain safe and operationally useful. Incorrect propagation or sensitive attributes reduce trust in telemetry.

- **CONTEXT CONTINUITY**: Ensure trace context is propagated across HTTP/messaging/async boundaries
- **SEMANTIC CONSISTENCY**: Use stable span names and OpenTelemetry semantic conventions where applicable
- **DATA MINIMIZATION**: Avoid high-cardinality and sensitive attributes in spans/events
- **SAMPLING STRATEGY**: Configure sampling per environment to balance cost and diagnostic depth

## Examples

### Table of contents

- Example 1: Create Spans with Clear Boundaries
- Example 2: Propagate Context and Keep Attributes Safe

### Example 1: Create Spans with Clear Boundaries

Title: Model request and dependency calls as parent/child spans
Description: 

**Good example:**

```java
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

public final class CheckoutTracing {
    private static final Tracer TRACER = GlobalOpenTelemetry.getTracer("checkout-service");

    public void checkout(String orderId) {
        Span parent = TRACER.spanBuilder("checkout.process")
                .setSpanKind(SpanKind.INTERNAL)
                .startSpan();
        try (Scope ignored = parent.makeCurrent()) {
            parent.setAttribute("order.id.present", orderId != null);

            callInventory();
            callPayment();

            parent.setStatus(StatusCode.OK);
        } catch (RuntimeException ex) {
            parent.recordException(ex);
            parent.setStatus(StatusCode.ERROR, "Checkout failed");
            throw ex;
        } finally {
            parent.end();
        }
    }

    private void callInventory() {
        Span span = TRACER.spanBuilder("inventory.reserve").setSpanKind(SpanKind.CLIENT).startSpan();
        try (Scope ignored = span.makeCurrent()) {
            // HTTP call / client SDK call
            span.setStatus(StatusCode.OK);
        } catch (RuntimeException ex) {
            span.recordException(ex);
            span.setStatus(StatusCode.ERROR);
            throw ex;
        } finally {
            span.end();
        }
    }

    private void callPayment() {
        Span span = TRACER.spanBuilder("payment.charge").setSpanKind(SpanKind.CLIENT).startSpan();
        try (Scope ignored = span.makeCurrent()) {
            span.setStatus(StatusCode.OK);
        } finally {
            span.end();
        }
    }
}
```

**Bad example:**

```java
public final class CheckoutTracingBad {
    public void checkout(String userEmail, String token) {
        // BAD: no spans/context, impossible to diagnose flow latency
        // BAD: sensitive values often leak when teams later add naive attributes
        System.out.println("processing " + userEmail + " with token " + token);
    }
}
```

### Example 2: Propagate Context and Keep Attributes Safe

Title: Ensure continuity and avoid cardinality/privacy problems
Description: 

**Good example:**

```java
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import java.util.concurrent.Executor;

public final class AsyncFlow {
    private final Executor executor;

    public AsyncFlow(Executor executor) {
        this.executor = executor;
    }

    public void submitTask() {
        Context captured = Context.current();
        executor.execute(() -> {
            try (var ignored = captured.makeCurrent()) {
                Span.current().addEvent("async.task.started");
                // do work
            }
        });
    }
}
```

**Bad example:**

```java
import io.opentelemetry.api.trace.Span;

public final class AsyncFlowBad {
    public void submitTask(String userId, String jwtToken) {
        // BAD: no context propagation, detached spans
        // BAD: high cardinality + sensitive attributes
        Span.current().setAttribute("user.id", userId);
        Span.current().setAttribute("auth.token", jwtToken);
    }
}
```

## Output Format

- **ANALYZE** tracing gaps across span modeling, propagation, and semantic consistency
- **APPLY** OpenTelemetry instrumentation with clear parent/child relationships and stable names
- **HARDEN** attributes/events/status and remove sensitive or high-cardinality data
- **VALIDATE** end-to-end trace continuity in tests and runtime backends

## Safeguards

- **PRIVACY FIRST**: Never place secrets or PII in trace payloads
- **PROPAGATION CHECK**: Verify traceId/spanId continuity across service boundaries
- **CARDINALITY CONTROL**: Keep attribute values bounded and query-friendly
- **PERF AWARENESS**: Avoid unnecessary spans in hot loops and configure sampling appropriately