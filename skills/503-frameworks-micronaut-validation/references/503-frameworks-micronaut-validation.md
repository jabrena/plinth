---
name: 503-frameworks-micronaut-validation
description: Use when you need to design, review, or improve validation in Micronaut applications — including Bean Validation on @Controller methods, @Body @Valid, query/path parameter validation, @ConfigurationProperties validation, custom constraints, factory-based method validation, nested DTO validation, and ExceptionHandler mapping for constraint violations.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Micronaut Validation Guidelines

## Role

You are a Senior software engineer with extensive experience in Micronaut and Jakarta Bean Validation

## Goal

Ensure Micronaut HTTP APIs validate inputs at boundaries, keep constraint definitions on DTOs and command models, and return stable validation error responses. Align with `@502-frameworks-micronaut-rest` for status codes and error bodies.

## Constraints

Before applying recommendations, ensure the project compiles. Compilation failure is a blocking condition.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **SAFETY**: If compilation fails, stop immediately
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Request body validation
- Example 2: Query and path parameters
- Example 3: @ConfigurationProperties validation
- Example 4: Nested and collection validation
- Example 5: Service method validation
- Example 6: ConstraintViolationException handler
- Example 7: Class-level custom constraint

### Example 1: Request body validation

Title: @Body @Valid with constrained records
Description: Always pair constrained request types with `@Valid` (or `@Validated` for groups) on `@Body` parameters so Micronaut triggers Bean Validation before the controller method runs.

**Good example:**

```java
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Controller("/users")
class UserController {
    @Post
    HttpResponse<Void> create(@Body @Valid CreateUserRequest request) {
        return HttpResponse.noContent();
    }
}

record CreateUserRequest(@NotBlank String username, @Email String email) { }
```

**Bad example:**

```java
@Post
HttpResponse<Void> create(@Body CreateUserRequest request) {
    return HttpResponse.noContent();
}
// Constraints on CreateUserRequest are not enforced without @Valid
```

### Example 2: Query and path parameters

Title: @Min, @Max, @Pattern on @QueryValue and URI variables
Description: Validate simple parameters on the controller method. Prefer typed query values with constraints instead of parsing raw strings manually.

**Good example:**

```java
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

@Controller("/items")
class ItemController {
    @Get("/{id}")
    String get(@PathVariable @Pattern(regexp = "^[a-z0-9-]+$") String id) {
        return id;
    }

    @Get
    String list(@QueryValue @Min(0) int page, @QueryValue @Min(1) @Max(100) int size) {
        return page + ":" + size;
    }
}
```

**Bad example:**

```java
@Get("/{id}")
String get(String id) {
    if (id.contains("..")) throw new IllegalArgumentException();
    return id;
}
```

### Example 3: @ConfigurationProperties validation

Title: Fail-fast binding with @Valid and constraints
Description: Annotate `@ConfigurationProperties` beans with `@Valid` and Jakarta constraints so invalid `application.yml` fails at startup. Pairs with `@501-frameworks-micronaut-core`.

**Good example:**

```java
import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;

@ConfigurationProperties("app.api")
@Valid
record ApiSettings(
    @NotBlank String baseUrl,
    @Min(1) @Max(600) int timeoutSeconds
) { }
```

**Bad example:**

```java
@ConfigurationProperties("app.api")
record ApiSettings(String baseUrl, int timeoutSeconds) { }
// No startup validation
```

### Example 4: Nested and collection validation

Title: @Valid on nested types and list elements
Description: Cascade validation with `@Valid` on nested fields and `List<@NotNull @Valid LineItem>` patterns.

**Good example:**

```java
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record OrderRequest(
    @NotNull @Valid Address shipping,
    @NotEmpty List<@NotNull @Valid LineItem> lines
) { }

public record Address(@NotNull @Size(max = 200) String street) { }

public record LineItem(
    @NotNull @Size(min = 1, max = 64) String sku,
    int qty
) { }
```

**Bad example:**

```java
public record OrderRequest(Address shipping, List<LineItem> lines) { }
```

### Example 5: Service method validation

Title: @Validated singleton with constrained methods
Description: Apply `io.micronaut.validation.annotation.Validated` to a bean class so method-parameter constraints are enforced when other beans call through the Micronaut proxy (avoid self-invocation bypassing validation).

**Good example:**

```java
import io.micronaut.validation.annotation.Validated;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Singleton
@Validated
class InventoryService {
    void reserve(@NotBlank String sku, @Positive int quantity) {
    }
}
```

**Bad example:**

```java
void reserve(String sku, int qty) {
    if (qty <= 0) throw new IllegalArgumentException();
}
```

### Example 6: ConstraintViolationException handler

Title: Map validation failures to HTTP 400
Description: Provide an `ExceptionHandler<ConstraintViolationException>` singleton to return consistent JSON for validation errors across controllers.

**Good example:**

```java
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Singleton
public class ConstraintViolationHandler
        implements ExceptionHandler<ConstraintViolationException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, ConstraintViolationException ex) {
        var details = ex.getConstraintViolations().stream()
            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
            .collect(Collectors.toList());
        return HttpResponse.status(HttpStatus.BAD_REQUEST)
            .body(java.util.Map.of("error", "VALIDATION_ERROR", "details", details));
    }
}
```

**Bad example:**

```java
return HttpResponse.badRequest(ex.getMessage());
// Unstable message; may expose internal paths
```

### Example 7: Class-level custom constraint

Title: Reuse cross-field rules
Description: Implement `@Constraint(validatedBy = ...)` for DTO-level rules shared across endpoints.

**Good example:**

```java
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SamePasswords.Validator.class)
@interface SamePasswords {
    String message() default "passwords must match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<SamePasswords, PasswordForm> {
        @Override
        public boolean isValid(PasswordForm v, ConstraintValidatorContext ctx) {
            if (v == null) return true;
            return java.util.Objects.equals(v.password(), v.confirmPassword());
        }
    }
}

@SamePasswords
public record PasswordForm(String password, String confirmPassword) { }
```

**Bad example:**

```java
if (!p1.equals(p2)) throw new IllegalArgumentException("mismatch");
```

## Output Format

- **ANALYZE** controllers, factories, and configuration beans for missing `@Valid`, missing cascades, and inconsistent 400 bodies
- **APPLY** Bean Validation at HTTP boundaries and on `@ConfigurationProperties`; centralize `ConstraintViolationException` handling
- **ALIGN** error responses with Problem Details or project-standard JSON from `@502-frameworks-micronaut-rest`
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after changes

## Safeguards

- Do not omit `@Valid` on constrained `@Body` parameters
- Do not return stack traces or raw validator messages to clients
- Verify validation behavior under compile-time HTTP clients and GraalVM native if applicable