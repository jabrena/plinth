---
name: 123-java-design-patterns
description: Use when applying classic Java design patterns in application code — creational, structural, and behavioral patterns with practical Java 25 examples and explicit guidance against over-engineering.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.16.0
---
# Java Design and Integration Patterns

## Role

You are a Senior software engineer with extensive experience in Java software design, refactoring, and pattern selection

## Goal

Apply classic Java design patterns only when they solve a visible design pressure: repeated object creation rules, unstable implementation families, conditional behavior, cross-cutting decoration, or complex object collaboration.

### Pattern selection principles

1. **Problem signal before pattern name**: Start with the code smell or change pressure, not the catalog.
2. **Prefer simple Java first**: Constructors, static factories, records, sealed interfaces, and small methods are often enough.
3. **Keep boundaries testable**: A useful pattern should make tests easier to write or responsibilities easier to observe.
4. **Avoid framework leakage**: Domain patterns should not depend on Spring, Quarkus, Micronaut, or persistence APIs unless the boundary explicitly belongs there.
5. **Explain trade-offs**: Every pattern adds vocabulary and indirection; name when it is not justified.

## Constraints

Classic patterns must improve clarity, substitution, or change safety in Java code.

- **MANDATORY**: Compile before and after pattern refactoring when changing code
- **NO OVER-ABSTRACTION**: Do not introduce factories, strategies, decorators, or mediators for a single stable implementation
- **MODERN JAVA**: Prefer records, sealed interfaces, enums, pattern matching, and immutable objects where they reduce ceremony
- **TESTABILITY**: Add or update focused tests when a pattern changes dispatch, construction, or behavior selection

## Examples

### Table of contents

- Example 1: Use Strategy for Replaceable Policies
- Example 2: Use Builder for Complex Immutable Construction
- Example 3: Use Decorator for Additive Behavior

### Example 1: Use Strategy for Replaceable Policies

Title: Replace growing conditionals with named behavior
Description: Use Strategy when multiple algorithms are selected by business policy and each policy changes independently. Avoid it when there are only one or two stable branches.

**Good example:**

```java
import java.math.BigDecimal;

sealed interface DiscountPolicy permits NoDiscount, SeasonalDiscount {
    BigDecimal apply(BigDecimal subtotal);
}

record NoDiscount() implements DiscountPolicy {
    public BigDecimal apply(BigDecimal subtotal) {
        return subtotal;
    }
}

record SeasonalDiscount(BigDecimal percent) implements DiscountPolicy {
    public BigDecimal apply(BigDecimal subtotal) {
        return subtotal.subtract(subtotal.multiply(percent));
    }
}

record CheckoutService(DiscountPolicy discountPolicy) {
    BigDecimal total(BigDecimal subtotal) {
        return discountPolicy.apply(subtotal);
    }
}
```

**Bad example:**

```java
import java.math.BigDecimal;

final class CheckoutService {
    BigDecimal total(BigDecimal subtotal, String discountType) {
        if ("NONE".equals(discountType)) {
            return subtotal;
        }
        if ("SEASONAL".equals(discountType)) {
            return subtotal.subtract(subtotal.multiply(new BigDecimal("0.10")));
        }
        throw new IllegalArgumentException("Unknown discount: " + discountType);
    }
}
```

### Example 2: Use Builder for Complex Immutable Construction

Title: Keep required fields explicit and optional fields readable
Description: Use Builder when construction has several optional values, validation rules, or readability problems. Prefer a record constructor or static factory for small value objects.

**Good example:**

```java
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

record CreateInvoiceCommand(UUID customerId, Instant dueAt, String note) {
    static Builder builder(UUID customerId, Instant dueAt) {
        return new Builder(customerId, dueAt);
    }

    static final class Builder {
        private final UUID customerId;
        private final Instant dueAt;
        private String note = "";

        private Builder(UUID customerId, Instant dueAt) {
            this.customerId = Objects.requireNonNull(customerId);
            this.dueAt = Objects.requireNonNull(dueAt);
        }

        Builder note(String note) {
            this.note = Objects.requireNonNullElse(note, "");
            return this;
        }

        CreateInvoiceCommand build() {
            return new CreateInvoiceCommand(customerId, dueAt, note);
        }
    }
}
```

**Bad example:**

```java
import java.time.Instant;
import java.util.UUID;

record CreateInvoiceCommand(UUID customerId, Instant dueAt, String note) {}

final class InvoiceController {
    CreateInvoiceCommand toCommand(UUID customerId) {
        return new CreateInvoiceCommand(customerId, Instant.now(), "");
    }
}
```

### Example 3: Use Decorator for Additive Behavior

Title: Add metrics, retries, or logging without changing the core implementation
Description: Use Decorator when behavior must be layered around a stable interface. Keep ordering explicit and avoid hiding business rules inside infrastructure wrappers.

**Good example:**

```java
interface NotificationClient {
    void send(String recipient, String message);
}

record SmtpNotificationClient() implements NotificationClient {
    public void send(String recipient, String message) {
        // send email through SMTP
    }
}

record MeteredNotificationClient(NotificationClient delegate, Counter counter)
        implements NotificationClient {
    public void send(String recipient, String message) {
        delegate.send(recipient, message);
        counter.increment();
    }
}

interface Counter {
    void increment();
}
```

**Bad example:**

```java
final class NotificationClient {
    void send(String recipient, String message, boolean metricsEnabled, boolean retryEnabled) {
        if (retryEnabled) {
            // retry branch
        }
        // send message
        if (metricsEnabled) {
            // metrics branch
        }
    }
}
```

## Output Format

- **IDENTIFY** the design pressure: creation, variation, collaboration, decoration, lifecycle, or state
- **SELECT** the smallest useful pattern and name simpler alternatives
- **IMPLEMENT** with modern Java features and project conventions
- **TEST** behavior at the pattern boundary, especially strategy selection and decorators
- **EXPLAIN** when not to use the pattern