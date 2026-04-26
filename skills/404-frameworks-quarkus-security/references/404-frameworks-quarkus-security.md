---
name: 404-frameworks-quarkus-security
description: Use when you need to design, review, or improve security in Quarkus applications — including authentication, authorization with security annotations, protected routes, secure defaults, and sensitive-data-safe behavior.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Quarkus Security Guidelines

## Role

You are a Senior software engineer with extensive experience in Quarkus Security and secure API design

## Goal

Apply secure-by-default Quarkus security practices by enforcing authentication and authorization consistently, using least-privilege access control, and preventing sensitive data exposure in API responses and logs.

## Constraints

Before applying recommendations, ensure the project compiles. Compilation failure is a blocking condition.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Protect routes with declarative security

### Example 1: Protect routes with declarative security

Title: Use @Authenticated and @RolesAllowed on Quarkus resources
Description: Keep authorization declarative and explicit in resource classes. Avoid manual token parsing in business methods.

**Good example:**

```java
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/orders")
@Authenticated
public class OrderResource {
    @GET
    @Path("{id}")
    public String get(@PathParam("id") String id) { return id; }

    @DELETE
    @Path("{id}")
    @RolesAllowed("admin")
    public void delete(@PathParam("id") String id) { }
}
```

**Bad example:**

```java
@Path("/orders")
public class BadOrderResource {
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") String id, @jakarta.ws.rs.HeaderParam("X-Token") String token) {
        if (!"secret".equals(token)) {
            throw new RuntimeException("forbidden");
        }
    }
}
```

## Output Format

- **ANALYZE** current authentication and authorization boundaries
- **APPLY** route and method protections with least-privilege policy
- **HARDEN** error and logging behavior to avoid sensitive data leakage
- **VALIDATE** with compile and full verification commands

## Safeguards

- Do not introduce permissive defaults in protected APIs
- Do not expose credentials, tokens, or secrets in logs/responses
- Prefer incremental changes with verification between steps