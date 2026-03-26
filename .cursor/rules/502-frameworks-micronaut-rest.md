---
name: 502-frameworks-micronaut-rest
description: Use when you need to design, review, or improve REST APIs with Micronaut — including @Controller routes, HTTP methods and status codes, request/response DTOs, Bean Validation at the boundary, exception handlers and RFC 7807-style problem responses, pagination and sorting, idempotency keys, ETag / If-Match, HTTP caching headers, API versioning, OpenAPI with Swagger, content negotiation, and security-aware boundaries.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Micronaut REST API Guidelines

## Role

You are a Senior software engineer with extensive experience in REST API design and Micronaut

## Goal

Micronaut REST endpoints should honor HTTP semantics, return accurate status codes, keep controllers thin, and expose stable DTO contracts. Use Bean Validation on incoming DTOs, centralize error mapping (including structured problem JSON when the `micronaut-problem` module is present), document APIs with OpenAPI, and apply pagination, idempotency, and concurrency controls where writes and lists matter.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Semantic HTTP**: Map `GET`/`POST`/`PUT`/`PATCH`/`DELETE` to safe vs unsafe operations; never use `GET` for state changes.
2. **Resource-oriented URLs**: Prefer noun-based paths (`/users`, `/users/{id}`) over RPC-style verb paths.
3. **Status codes**: Use `200`/`201`/`204`/`400`/`404`/`409`/`412`/`422` deliberately — not `200` for every outcome.
4. **DTO boundaries**: Do not leak persistence entities or arbitrary maps; version breaking changes explicitly.
5. **Validation**: Annotate request DTOs with Bean Validation and mark controller parameters with `@Valid` (or equivalent) so bad input becomes **400** with field errors.
6. **Errors**: Map domain failures to HTTP outcomes via `@Error` handlers or `ExceptionHandler` beans; prefer consistent JSON problem bodies for clients.
7. **Lists**: Bound page size, document sort keys, and return page metadata instead of unbounded arrays by default.
8. **Idempotency**: Support `Idempotency-Key` (or domain deduplication) for retried creates where appropriate.
9. **Concurrency**: Use `ETag` / `If-Match` or version columns for optimistic locking on updates.
10. **Caching**: Set `Cache-Control` and validators consciously for authenticated vs public resources.
11. **Versioning**: Choose URI, header, or media-type strategy and apply it consistently; signal deprecation with headers when phasing endpoints out.
12. **Time in JSON**: Prefer `Instant` or `OffsetDateTime` (ISO-8601) in API DTOs.

**Cross-references**: Micronaut core — `@501-frameworks-micronaut-core`. Micronaut Data — `@512-frameworks-micronaut-data`. Integration tests — `@522-frameworks-micronaut-testing-integration-tests`.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully before any REST API refactoring
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: HTTP methods
- Example 2: Validation at the boundary
- Example 3: Centralized error mapping
- Example 4: Pagination
- Example 5: OpenAPI documentation
- Example 6: Content negotiation
- Example 7: Security at the boundary
- Example 8: Idempotent creates
- Example 9: Optimistic concurrency
- Example 10: HTTP caching headers
- Example 11: API versioning
- Example 12: Time in DTOs

### Example 1: HTTP methods

Title: Controller routes with correct verbs
Description: Use Micronaut `@Get`, `@Post`, `@Put`, `@Patch`, `@Delete` on `@Controller` types. Return `HttpResponse` when you need status codes beyond defaults.

**Good example:**

```java
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import java.net.URI;

@Controller("/users")
public class UserController {

    private final UserService users;

    public UserController(UserService users) {
        this.users = users;
    }

    @Get("/{id}")
    public HttpResponse<UserDto> get(String id) {
        return users.find(id)
            .map(HttpResponse::ok)
            .orElseGet(HttpResponse::notFound);
    }

    @Post
    public HttpResponse<UserDto> create(@Body UserCreateDto body) {
        UserDto created = users.create(body);
        URI location = URI.create("/users/" + created.id());
        return HttpResponse.created(location).body(created);
    }

    @Delete("/{id}")
    public HttpResponse<Void> delete(String id) {
        users.delete(id);
        return HttpResponse.noContent();
    }
}
```

**Bad example:**

```java
import io.micronaut.http.annotation.*;

@Controller("/api")
public class RpcStyleController {

    @Get("/deleteUser/{id}") // Bad: unsafe state change via GET
    public String deleteUser(String id) {
        return "deleted";
    }
}
```

### Example 2: Validation at the boundary

Title: @Valid @Body on DTOs
Description: Apply Bean Validation annotations to DTOs and enable validation on the controller parameter with `@Valid`.

**Good example:**

```java
import io.micronaut.http.annotation.*;
import io.micronaut.http.HttpResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateDto(
    @NotBlank String name,
    @Email String email
) {}

@Controller("/users")
public class UserController {

    private final UserService users;

    public UserController(UserService users) {
        this.users = users;
    }

    @Post
    public HttpResponse<UserDto> create(@Body @Valid UserCreateDto body) {
        return HttpResponse.created(java.net.URI.create("/users/x")).body(users.create(body));
    }
}
```

**Bad example:**

```java
import io.micronaut.http.annotation.*;

@Controller("/users")
public class UserController {

    @Post
    public UserDto create(@Body UserCreateDto body) {
        // Bad: no @Valid — invalid emails reach the service layer
        return null;
    }
}
record UserCreateDto(String name, String email) {}
```

### Example 3: Centralized error mapping

Title: @Error or dedicated exception handlers
Description: Map domain exceptions to HTTP responses in one place. When using `micronaut-problem`, emit RFC 7807-compatible JSON for machine-readable client handling.

**Good example:**

```java
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Singleton
public class NotFoundHandler implements ExceptionHandler<EntityNotFoundException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, EntityNotFoundException exception) {
        return HttpResponse.notFound();
    }
}
```

**Bad example:**

```java
import io.micronaut.http.annotation.*;

@Controller("/orders")
public class OrderController {

    @Get("/{id}")
    public OrderDto get(String id) {
        try {
            return load(id);
        } catch (EntityNotFoundException ex) {
            // Bad: returning error payload with 200 OK
            return new OrderDto("ERROR", null);
        }
    }

    OrderDto load(String id) { throw new EntityNotFoundException(); }
}
```

### Example 4: Pagination

Title: Pageable request and bounded page size
Description: Accept `page` and `size` (or cursor) query parameters with upper bounds. Return total or next-cursor metadata instead of dumping entire tables.

**Good example:**

```java
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.*;

@Controller("/users")
public class UserController {

    private final UserService users;

    public UserController(UserService users) {
        this.users = users;
    }

    @Get
    public Page<UserDto> list(@Valid Pageable pageable) {
        // Service layer should cap size and validate sort properties
        return users.list(pageable);
    }
}
```

**Bad example:**

```java
import io.micronaut.http.annotation.*;
import java.util.List;

@Controller("/users")
public class UserController {

    @Get
    public List<UserDto> all() {
        // Bad: unbounded list — OOM and slow clients at scale
        return List.of();
    }
}
```

### Example 5: OpenAPI documentation

Title: @Operation and @Tag on controllers
Description: Document public endpoints with Swagger annotations so generated OpenAPI stays aligned with behavior.

**Good example:**

```java
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller("/users")
@Tag(name = "users")
public class UserController {

    @Get("/{id}")
    @Operation(summary = "Get user by id")
    public UserDto get(String id) {
        return null;
    }
}
```

**Bad example:**

```java
import io.micronaut.http.annotation.*;

@Controller("/users")
public class UndocumentedController {

    @Get("/{id}")
    // Bad: no OpenAPI metadata — consumers rely on guesswork
    public UserDto get(String id) {
        return null;
    }
}
```

### Example 6: Content negotiation

Title: Prefer JSON with explicit produces/consumes when needed
Description: Default JSON is typical; when versioning via media types, keep `produces`/`consumes` consistent across the controller surface.

**Good example:**

```java
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

@Controller("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Get("/{id}")
    public UserDto get(String id) {
        return null;
    }
}
```

**Bad example:**

```java
import io.micronaut.http.annotation.*;

@Controller("/users")
public class UserController {

    @Get(value = "/{id}", produces = {"application/json", "application/xml"})
    // Bad: multiple representations without tests or client contract
    public UserDto get(String id) {
        return null;
    }
}
```

### Example 7: Security at the boundary

Title: @Secured or security rules on routes
Description: Declare authentication/authorization on sensitive routes. Do not rely on obscurity or client-side-only checks.

**Good example:**

```java
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/admin")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AdminController {

    @Get("/metrics")
    public String metrics() {
        return "ok";
    }
}
```

**Bad example:**

```java
import io.micronaut.http.annotation.*;

@Controller("/admin")
public class OpenAdminController {

    @Get("/metrics")
    // Bad: sensitive endpoint without Micronaut Security annotations
    public String metrics() {
        return "secrets";
    }
}
```

### Example 8: Idempotent creates

Title: Idempotency-Key header handling
Description: For POST that must survive retries, accept an idempotency key, persist it with the outcome, and return the same response for duplicates.

**Good example:**

```java
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

@Controller("/payments")
public class PaymentController {

    private final PaymentService payments;

    public PaymentController(PaymentService payments) {
        this.payments = payments;
    }

    @Post
    public HttpResponse<PaymentDto> pay(
            @Header("Idempotency-Key") String key,
            @Body @jakarta.validation.Valid PaymentRequest body) {
        return payments.payOnce(key, body);
    }
}
```

**Bad example:**

```java
import io.micronaut.http.annotation.*;

@Controller("/payments")
public class PaymentController {

    @Post
    public PaymentDto pay(@Body PaymentRequest body) {
        // Bad: duplicate POST on retry may double-charge
        return null;
    }
}
```

### Example 9: Optimistic concurrency

Title: ETag and If-Match
Description: Return weak or strong ETags for versioned resources; require `If-Match` on mutating requests when clients must detect stale writes.

**Good example:**

```java
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

@Controller("/items")
public class ItemController {

    private final ItemService items;

    public ItemController(ItemService items) {
        this.items = items;
    }

    @Put("/{id}")
    public HttpResponse<ItemDto> update(
            String id,
            @Header(HttpHeaders.IF_MATCH) String ifMatch,
            @Body ItemUpdateDto body) {
        return items.updateIfMatch(id, ifMatch, body);
    }
}
```

**Bad example:**

```java
import io.micronaut.http.annotation.*;

@Controller("/items")
public class ItemController {

    @Put("/{id}")
    public ItemDto update(String id, @Body ItemUpdateDto body) {
        // Bad: last writer wins — lost updates under concurrency
        return null;
    }
}
```

### Example 10: HTTP caching headers

Title: Cache-Control for public vs private responses
Description: Set caching headers deliberately. Personalized or authorized responses usually need `private` or `no-store`.

**Good example:**

```java
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;

@Controller("/catalog")
public class CatalogController {

    @Get("/products/{id}")
    public MutableHttpResponse<ProductDto> get(String id) {
        return HttpResponse.ok(load(id))
            .header("Cache-Control", "public, max-age=60");
    }

    ProductDto load(String id) { return null; }
}
```

**Bad example:**

```java
import io.micronaut.http.annotation.*;

@Controller("/me")
public class ProfileController {

    @Get
    public UserProfileDto me() {
        // Bad: default caching might cache authenticated profile at shared proxies
        return null;
    }
}
```

### Example 11: API versioning

Title: Stable /v1 prefix
Description: Place version in the path or negotiate via headers/media types consistently. Avoid silent breaking changes on unversioned URLs used in production.

**Good example:**

```text
GET /api/v1/users/{id}
POST /api/v1/users
```

**Bad example:**

```text
GET /users/{id}   (breaking field renames without version)
GET /getUserV2/{id} (inconsistent versioning style)
```

### Example 12: Time in DTOs

Title: Instant or OffsetDateTime in JSON
Description: Prefer ISO-8601-friendly types in API records; avoid `java.util.Date` in new contracts.

**Good example:**

```java
import java.time.Instant;

public record EventDto(String id, Instant occurredAt) {}
```

**Bad example:**

```java
import java.util.Date;

public record EventDto(String id, Date occurredAt) {} // Bad: legacy type, timezone pitfalls
```

## Output Format

- **ANALYZE** controllers for HTTP semantics, validation, error mapping, pagination, security annotations, caching, versioning, and OpenAPI coverage
- **CATEGORIZE** findings by client impact (breaking vs safe) and by concern (validation, errors, performance)
- **APPLY** improvements: correct status codes, `@Valid` DTOs, centralized exception handling, bounded list endpoints, idempotency for retried writes, ETag/If-Match where updates collide, explicit security rules, cache headers, documented operations
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after substantive edits

## Safeguards

- **BLOCKING SAFETY CHECK**: Run `./mvnw compile` before ANY REST refactoring
- **SECURITY**: Never return stack traces or secrets in JSON error bodies in production
- **COMPATIBILITY**: Changing status codes or DTO shapes breaks clients — use versioning or deprecation headers
- **INCREMENTAL SAFETY**: Ship error-handler and validation changes with integration tests covering representative failures