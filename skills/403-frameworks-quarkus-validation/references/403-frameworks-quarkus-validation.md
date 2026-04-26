---
name: 403-frameworks-quarkus-validation
description: Use when you need to design, review, or improve validation in Quarkus applications — including Bean Validation, @Valid at Jakarta REST boundaries, custom constraints, and consistent validation error mapping.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Quarkus Validation Guidelines

## Role

You are a Senior software engineer with extensive experience in Quarkus, Jakarta REST, and Bean Validation

## Goal

Ensure Quarkus services validate inputs at boundaries, keep validation rules declarative and testable, and return consistent client-safe validation error responses.

## Constraints

Before applying recommendations, ensure the project compiles. Compilation failure is a blocking condition.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Validate REST input with @Valid

### Example 1: Validate REST input with @Valid

Title: Use Jakarta Bean Validation annotations on request models
Description: Keep validation at REST boundaries to reject malformed data early and reduce downstream defensive code.

**Good example:**

```java
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @POST
    public void create(@Valid CreateUserRequest request) { }
}

record CreateUserRequest(@NotBlank String username, @Email String email) { }
```

**Bad example:**

```java
@Path("/users")
public class BadUserResource {
    @POST
    public void create(CreateUserRequest request) {
        if (request.email() == null || !request.email().contains("@")) {
            throw new IllegalArgumentException("invalid email");
        }
    }
}
record CreateUserRequest(String username, String email) { }
```

## Output Format

- **ANALYZE** REST and service boundaries for missing validation
- **APPLY** Bean Validation rules and custom constraints where needed
- **STANDARDIZE** validation failure responses
- **VALIDATE** with compile and full verification commands

## Safeguards

- Do not rely on ad-hoc per-endpoint validation logic
- Do not leak internal exception details in validation responses
- Prefer small and incremental refactors