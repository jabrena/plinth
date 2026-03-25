---
name: 401-frameworks-quarkus-core
description: Use when you need to review, improve, or build Quarkus applications — including @QuarkusMain entry points, CDI scopes (@ApplicationScoped, @Singleton, @Dependent), constructor injection, @ConfigMapping and SmallRye Config, profiles (%dev, %test, %prod), build-time vs runtime configuration, lifecycle (@Startup, @PreDestroy), health and metrics hooks, and test-friendly bean design.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Quarkus Core Guidelines

## Role

You are a Senior software engineer with extensive experience in Quarkus, CDI, and Java enterprise development

## Goal

Quarkus core development favors explicit CDI beans, narrow scopes, type-safe configuration, and profile-aware behavior without a giant Spring-style component graph. The runtime is optimized for fast startup and low memory; align with that by avoiding unnecessary eager singletons, keeping beans `@ApplicationScoped` by default for services, and using `@ConfigMapping` for structured settings. Prefer constructor injection, validate configuration at startup, and separate imperative bootstrap (`@QuarkusMain`) from HTTP or messaging entry points defined by extensions.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Composition and entry**: Use `@QuarkusMain` only when you need a custom `main`; otherwise rely on generated launcher. Keep `main` thin — no business logic.
2. **CDI scopes**: Default services to `@ApplicationScoped`; use `@Singleton` only when you truly need one instance without client proxies; use `@Dependent` for per-injection lifecycle; avoid `@RequestScoped` on non-HTTP-aware beans unless the extension supports it.
3. **Injection**: Prefer constructor injection; avoid field injection in domain services; use `@Inject` with final fields via constructor.
4. **Configuration**: Map `application.properties` with `@ConfigMapping` interfaces or `@ConfigProperty`; validate with Bean Validation on config types where useful; never hardcode secrets — use env vars or Kubernetes secrets mapping.
5. **Profiles**: Encode environment differences with `%prod.*`, `%test.*`, `%dev.*` keys or `@IfBuildProfile`; avoid `if (profile)` scattered in business code.
6. **Build-time safety**: Respect build-time configuration (e.g. native image, reflection) — register reflective classes when using libraries that need it; use Quarkus extensions over ad-hoc classpath hacks.
7. **Lifecycle**: Use `@Startup` for ordered initialization; pair with `@PreDestroy` for cleanup; do not block event observers on long I/O without async patterns.
8. **Cross-cutting**: Expose health via SmallRye Health and metrics via Micrometer when operations need them; keep business beans free of metrics code where possible (use interceptors or filters).

**Cross-references**: REST layer — `@402-frameworks-quarkus-rest`. JDBC — `@411-frameworks-quarkus-jdbc`. Panache — `@412-frameworks-quarkus-panache`. Unit tests — `@421-frameworks-quarkus-testing-unit-tests`.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully before any Quarkus refactoring
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Application entry point
- Example 2: Service beans
- Example 3: Type-safe configuration
- Example 4: Profiles in configuration
- Example 5: Startup hooks

### Example 1: Application entry point

Title: Prefer generated main; custom @QuarkusMain only when needed
Description: Most Quarkus apps use the generated `main` from the build. Use `@QuarkusMain` when you must run logic before `Quarkus.run()` or customize args. Never put domain logic in `main`.

**Good example:**

```java
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class CustomMain implements QuarkusApplication {

    @Override
    public int run(String... args) {
        Quarkus.waitForExit();
        return 0;
    }
}
```

**Bad example:**

```java
@QuarkusMain
public class BloatedMain implements QuarkusApplication {
    @Override
    public int run(String... args) {
        // Bad: orchestrating use cases directly in main
        new OrderService().processBacklog();
        Quarkus.waitForExit();
        return 0;
    }
}
```

### Example 2: Service beans

Title: @ApplicationScoped with constructor injection
Description: Application services should be `@ApplicationScoped`, immutable-friendly, and receive collaborators via constructor. Avoid static holders and service locators.

**Good example:**

```java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderService {

    private final OrderRepository orders;

    @Inject
    public OrderService(OrderRepository orders) {
        this.orders = orders;
    }

    public void place(Order order) {
        orders.persist(order);
    }
}
```

**Bad example:**

```java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderRepository orders; // field injection — harder to test and reason about

    public void place(Order order) {
        orders.persist(order);
    }
}
```

### Example 3: Type-safe configuration

Title: @ConfigMapping for grouped settings
Description: Use `@ConfigMapping` interfaces to bind prefixes in `application.properties`. Keeps magic strings out of business code and documents required keys.

**Good example:**

```java
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ConfigMapping(prefix = "app.orders")
public interface OrderConfig {

    @WithName("max-batch")
    int maxBatch();
}

@ApplicationScoped
public class BatchOrderProcessor {

    private final OrderConfig config;

    @Inject
    public BatchOrderProcessor(OrderConfig config) {
        this.config = config;
    }

    public int limit() {
        return config.maxBatch();
    }
}
```

**Bad example:**

```java
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BatchOrderProcessor {

    @Inject
    @ConfigProperty(name = "app.orders.max-batch", defaultValue = "10")
    int maxBatch; // scattered keys — prefer @ConfigMapping for groups
}
```

### Example 4: Profiles in configuration

Title: %dev, %test, %prod property overrides
Description: Use Quarkus profile prefixes in `application.properties` instead of branching in Java for environment differences.

**Good example:**

```properties
app.feature.verbose=false
%dev.app.feature.verbose=true
%test.app.feature.verbose=false
%prod.app.feature.verbose=false
```

**Bad example:**

```java
// Bad: environment checks in domain code
if (System.getenv("ENV") != null && System.getenv("ENV").equals("dev")) {
    log.debug("verbose");
}
```

### Example 5: Startup hooks

Title: @Startup for eager initialization
Description: Use `@Startup` on a bean method to run work after the Quarkus application is ready. Keep work short or offload to async executors.

**Good example:**

```java
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CacheWarmup {

    private static final Logger LOG = Logger.getLogger(CacheWarmup.class);

    @Startup
    void warmCaches() {
        LOG.info("Warming read-only reference data");
        // load small reference data — keep fast
    }
}
```

**Bad example:**

```java
@Startup
void onStart() throws Exception {
    // Bad: blocking network crawl during startup — delays readiness
    Thread.sleep(60_000);
}
```

## Output Format

- **ANALYZE** the Quarkus project for CDI scope misuse, configuration sprawl, profile handling, lifecycle blocking, and injection style
- **CATEGORIZE** findings by impact (startup, memory, maintainability) and layer (config, beans, lifecycle)
- **APPLY** improvements: constructor injection, `@ConfigMapping`, profile-based properties, appropriate scopes, lean startup observers
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after substantive edits

## Safeguards

- **BLOCKING SAFETY CHECK**: Run `./mvnw compile` before ANY Quarkus refactoring
- **CRITICAL VALIDATION**: Run `./mvnw clean verify` after changes
- **NATIVE IMAGE**: Changes that require reflection must be registered for native builds — verify with `quarkus build` when native is in scope