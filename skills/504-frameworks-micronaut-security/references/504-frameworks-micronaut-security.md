---
name: 504-frameworks-micronaut-security
description: Use when you need to design, review, or improve security in Micronaut applications — including authentication, authorization with @Secured, route protection, secure defaults, and sensitive-data-safe logging/error handling.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Micronaut Security Guidelines

## Role

You are a Senior software engineer with extensive experience in Micronaut Security and secure API design

## Goal

Apply secure-by-default Micronaut security practices by enforcing route and method authorization policies consistently, using least-privilege roles, and preventing sensitive data exposure.

## Constraints

Before applying recommendations, ensure the project compiles. Compilation failure is a blocking condition.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Protect routes with @Secured

### Example 1: Protect routes with @Secured

Title: Keep authorization declarative in controllers
Description: Use Micronaut Security annotations to enforce access rules and avoid manual security checks in request handlers.

**Good example:**

```java
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller("/orders")
class OrderController {
    @Secured(IS_ANONYMOUS)
    @Get("/{id}")
    String read(@PathVariable String id) { return id; }

    @Secured("ROLE_ADMIN")
    @Delete("/{id}")
    void delete(@PathVariable String id) { }
}
```

**Bad example:**

```java
@Controller("/orders")
class BadOrderController {
    @Delete("/{id}")
    void delete(@PathVariable String id, @io.micronaut.http.annotation.Header("X-Admin-Token") String token) {
        if (!"secret".equals(token)) {
            throw new RuntimeException("forbidden");
        }
    }
}
```

## Output Format

- **ANALYZE** current authentication/authorization boundaries and risky defaults
- **APPLY** explicit route and method protections with least-privilege roles
- **HARDEN** logs and error responses to avoid leaking sensitive information
- **VALIDATE** with compile and full verification commands

## Safeguards

- Do not introduce permissive defaults to pass tests quickly
- Do not expose tokens, credentials, or secrets in logs/responses
- Prefer incremental security hardening with frequent verification