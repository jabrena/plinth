---
name: 182-java-observability-metrics-micrometer
description: Use when you need to implement or improve Java metrics observability with Micrometer — including meter design, naming/tag conventions, cardinality control, timers/counters/gauges/distribution summaries, percentiles/histograms, Actuator/Prometheus integration, and metrics validation through tests.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Java Metrics Observability with Micrometer

## Role

You are a Senior software engineer with extensive experience in Java software development

## Goal

Effective Java metrics observability with Micrometer starts by defining a clear metric contract for service-level signals:
throughput, latency, errors, and saturation.
Instrumentation must be stable over time, use semantic names, and avoid unbounded labels.
The right meter type (Counter, Timer, Gauge, DistributionSummary, LongTaskTimer) should match the behavior being measured.
Histograms and percentiles should be enabled selectively to control overhead and cost.
Finally, metrics should be validated through tests and runtime checks to ensure dashboards and alerts remain reliable.

## Constraints

Metrics instrumentation must avoid high-cardinality dimensions and dynamic meter churn. Uncontrolled tagging can overload telemetry backends and make observability unreliable.

- **CARDINALITY SAFETY**: Never use user-controlled, unique, or high-cardinality tag values (UUID, email, request id, full path with IDs)
- **METER SEMANTICS**: Select meter types based on behavior (Counter for events, Timer for latency, Gauge for state, DistributionSummary for value distributions)
- **INSTRUMENTATION SCOPE**: Prefer business and platform signals tied to SLOs over noisy low-value metrics
- **VALIDATION**: Verify expected metrics names, tags, and values through automated tests and runtime checks

## Examples

### Table of contents

- Example 1: Select the Right Meter Type
- Example 2: Use Stable Naming and Low-Cardinality Tags
- Example 3: Validate Instrumentation with Tests

### Example 1: Select the Right Meter Type

Title: Use Counters, Timers, Gauges and Summaries with Correct Semantics
Description: 

**Good example:**

```java
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class CheckoutMetrics {

    private final Counter checkoutSuccess;
    private final Counter checkoutFailure;
    private final Timer checkoutLatency;
    private final DistributionSummary payloadBytes;
    private final AtomicInteger queueDepth;

    public CheckoutMetrics(MeterRegistry registry) {
        this.checkoutSuccess = Counter.builder("checkout.requests")
                .description("Total successful checkout requests")
                .tag("outcome", "success")
                .register(registry);
        this.checkoutFailure = Counter.builder("checkout.requests")
                .description("Total failed checkout requests")
                .tag("outcome", "failure")
                .register(registry);
        this.checkoutLatency = Timer.builder("checkout.latency")
                .description("Checkout latency")
                .publishPercentileHistogram()
                .maximumExpectedValue(Duration.ofSeconds(5))
                .register(registry);
        this.payloadBytes = DistributionSummary.builder("checkout.payload.bytes")
                .description("Payload size of checkout requests")
                .baseUnit("bytes")
                .register(registry);
        this.queueDepth = registry.gauge("checkout.queue.depth", new AtomicInteger(0));
    }

    public void recordSuccess(long nanos, int bytes) {
        checkoutSuccess.increment();
        checkoutLatency.record(nanos, java.util.concurrent.TimeUnit.NANOSECONDS);
        payloadBytes.record(bytes);
    }

    public void recordFailure() {
        checkoutFailure.increment();
    }

    public void setQueueDepth(int depth) {
        queueDepth.set(Math.max(depth, 0));
    }
}
```

**Bad example:**

```java
import io.micrometer.core.instrument.MeterRegistry;

public final class BadMetrics {

    public void record(MeterRegistry registry, String userId, long latencyMillis) {
        // BAD: every signal is a gauge/counter without semantics
        registry.counter("checkout.metric", "type", "latency").increment(latencyMillis);

        // BAD: high cardinality tag
        registry.counter("checkout.requests", "userId", userId).increment();

        // BAD: timer should be used for latency, not counter with duration values
        registry.counter("checkout.duration.millis").increment(latencyMillis);
    }
}
```

### Example 2: Use Stable Naming and Low-Cardinality Tags

Title: Keep Metric Schema Predictable for Dashboards and Alerts
Description: 

**Good example:**

```java
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.Set;

public final class PaymentMetrics {

    private static final Set<String> ALLOWED_RESULTS = Set.of("approved", "declined", "error");
    private final MeterRegistry registry;

    public PaymentMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void recordResult(String provider, String result) {
        String safeProvider = normalizeProvider(provider); // bounded set
        String safeResult = ALLOWED_RESULTS.contains(result) ? result : "error";

        Counter.builder("payment.authorization.requests")
                .tag("provider", safeProvider)
                .tag("result", safeResult)
                .register(registry)
                .increment();
    }

    private String normalizeProvider(String provider) {
        return switch (provider) {
            case "adyen", "stripe", "paypal" -> provider;
            default -> "unknown";
        };
    }
}
```

**Bad example:**

```java
import io.micrometer.core.instrument.MeterRegistry;

public final class PaymentMetricsBad {
    public void record(MeterRegistry registry, String fullUrl, String requestId, String exceptionMessage) {
        // BAD: unbounded tags explode cardinality
        registry.counter("payment.requests",
                "url", fullUrl,
                "requestId", requestId,
                "error", exceptionMessage).increment();
    }
}
```

### Example 3: Validate Instrumentation with Tests

Title: Assert Metric Presence, Tags, and Values
Description: 

**Good example:**

```java
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class PaymentMetricsTest {

    @Test
    void shouldRecordApprovedPayment() {
        var registry = new SimpleMeterRegistry();
        var metrics = new PaymentMetrics(registry);

        metrics.recordResult("stripe", "approved");
        metrics.recordResult("stripe", "approved");

        double count = registry.get("payment.authorization.requests")
                .tag("provider", "stripe")
                .tag("result", "approved")
                .counter()
                .count();

        assertThat(count).isEqualTo(2.0d);
    }
}
```

**Bad example:**

```java
class PaymentMetricsTestBad {
    // BAD: no metric assertions; instrumentation can silently break.
}
```

## Output Format

- **ANALYZE** current instrumentation and classify issues by cardinality, semantic correctness, naming consistency, and operational value
- **DESIGN** a stable metric contract: names, units, bounded tags, meter types, and where each metric is emitted
- **APPLY** Micrometer best practices directly in code with minimal, explicit, and reusable instrumentation points
- **EXPLAIN** how each metric supports dashboards, alerts, and SLO/SLA reporting
- **VALIDATE** correctness through tests and runtime endpoint checks, including expected labels and values

## Safeguards

- **CARDINALITY GUARDRAIL**: Reject unbounded labels and normalize dynamic values into bounded buckets
- **NO METER CHURN**: Avoid building meters in tight loops with dynamic names/tags
- **BACKEND COMPATIBILITY**: Ensure names and tags remain compatible with Prometheus/OpenTelemetry conventions
- **OPERABILITY CHECK**: Confirm metrics are scrapeable/exported and usable in dashboards and alerts
- **REGRESSION SAFETY**: Keep existing metric names stable unless migration is explicitly planned