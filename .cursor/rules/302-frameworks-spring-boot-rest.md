---
name: 302-frameworks-spring-boot-rest
description: Use when you need to design, review, or improve REST APIs with Spring Boot — including HTTP methods, resource URIs, status codes, DTOs, versioning, deprecation and sunset headers, content negotiation (JSON and vendor media types), ISO-8601 instants in DTOs, pagination/sorting/filtering, Bean Validation at the boundary, idempotency, ETag concurrency, HTTP caching, error handling, security, API documentation, controller advice, and problem details for errors.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0
---
# Java REST API Design Principles

## Role

You are a Senior software engineer with extensive experience in REST API design and Spring Boot

## Goal

Well-designed REST APIs use HTTP semantics predictably: resources as nouns, methods for actions, status codes for outcomes, and stable contracts via DTOs and versioning. Production APIs centralize errors (structured JSON or RFC 7807 Problem Details), document behavior for consumers, and apply authentication, authorization, and input validation by default.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **Semantic consistency**: Align `GET`/`POST`/`PUT`/`PATCH`/`DELETE` with safe vs unsafe operations and return status codes that match the real outcome.
2. **Clear contracts**: Expose lean request/response DTOs, version APIs explicitly, and avoid leaking domain entities or stack traces to clients.
3. **Security by design**: Terminate TLS appropriately, authenticate and authorize requests, validate input, and avoid embedding secrets or raw SQL in controllers.
4. **Evolution without surprise**: Choose a versioning strategy (URI, header, or media type) and apply it consistently so breaking changes remain manageable.
5. **Bounded collections**: Prefer `Page`/`Pageable` (or explicit cursor/limit with caps) for list endpoints; use stable, documented sort keys; avoid unbounded JSON arrays as the default contract.
6. **Validation at the boundary**: Apply Bean Validation to request DTOs and `@Valid`/`@Validated` on controller inputs so malformed payloads fail fast with **400** and field-level errors.
7. **Safe retries for writes**: Support `Idempotency-Key` (or domain-level deduplication) for `POST` creates; use **409 Conflict** when a request collides with existing state; document idempotent vs non-idempotent operations in OpenAPI.
8. **Optimistic concurrency**: Use `ETag` with `If-Match`/`If-None-Match` (or version fields) for updates; return **412 Precondition Failed** or **304 Not Modified** appropriately; consider `ShallowEtagHeaderFilter` or explicit ETags for cacheable reads.
9. **Caching discipline**: Set `Cache-Control`/`ETag`/`Last-Modified` deliberately; do not mark personalized or authenticated responses as publicly cacheable by mistake.
10. **Deprecation signals**: Communicate API sunset with standard headers (`Deprecation`, `Sunset`) and `Link` (`rel="successor-version"`) so clients can migrate before hard removal—aligned with explicit versioning.
11. **Content negotiation**: Default to JSON with explicit `produces`/`consumes`; add vendor media types only when they carry real meaning (often alongside version) and keep them consistent across the surface area.
12. **Time and locale in contracts**: Prefer ISO-8601 instants with offset (`OffsetDateTime`, `Instant`) in JSON; use `Accept-Language` for localized problem details only with stable machine codes and consistent semantics across locales.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully and pass basic validation checks before any REST API refactoring
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE with any recommendations
- **BLOCKING CONDITION**: Compilation errors must be resolved by the user before proceeding with REST API improvements
- **NO EXCEPTIONS**: Under no circumstances should REST API recommendations be applied to a project that fails to compile
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: Use HTTP methods semantically
- Example 2: Resource URIs and naming
- Example 3: HTTP status codes
- Example 4: DTOs for requests and responses
- Example 5: API versioning
- Example 6: Graceful, structured error responses
- Example 7: API documentation
- Example 8: Controller advice and ProblemDetail
- Example 9: RFC 7807 Problem Details quality
- Example 10: Pagination, sorting, and filtering
- Example 11: Validation at the boundary
- Example 12: Idempotency and safe retries
- Example 13: Concurrency control
- Example 14: Caching semantics
- Example 15: Deprecation and sunset
- Example 16: Content negotiation
- Example 17: Time and locale in contracts

### Example 1: Use HTTP methods semantically

Title: GET for reads, POST for creates, PUT/PATCH for updates, DELETE for removal
Description: Map operations to the correct verb: `GET` must not change server state; `POST` typically creates subordinate resources; `PUT` replaces; `PATCH` partially updates; `DELETE` removes. Avoid RPC-style GET/POST misuse.

**Good example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/users")
class UserController {

    @GetMapping("/{id}")
    ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        return ResponseEntity.ok(new UserDTO("user-1"));
    }

    @PostMapping
    ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO body) {
        UserDTO created = new UserDTO("user-2");
        return ResponseEntity.created(URI.create("/users/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserUpdateDTO body) {
        return ResponseEntity.ok(new UserDTO(id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable String id) {
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    ResponseEntity<UserDTO> patchUser(@PathVariable String id, @RequestBody UserPatchDTO patch) {
        return ResponseEntity.ok(new UserDTO(id));
    }
}

record UserDTO(String id) { }
record UserCreateDTO(String username, String email) { }
record UserUpdateDTO(String username, String email) { }
record UserPatchDTO(String username, String email) { }
```

**Bad example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
class BadUserController {

    @GetMapping("/deleteUser")
    ResponseEntity<String> deleteUserViaGet(@RequestParam String id) {
        return ResponseEntity.ok("User deleted via GET");
    }

    @PostMapping("/getUser")
    ResponseEntity<UserDTO> getUserViaPost(@RequestBody String idPayload) {
        return ResponseEntity.ok(new UserDTO());
    }
}
class UserDTO { }
```

### Example 2: Resource URIs and naming

Title: Prefer noun-based paths and hierarchical resources
Description: Model resources as nouns with stable paths (`/users`, `/users/{id}/orders`). Avoid verbs in paths (`/getAllUsers`) and inconsistent mixing of query-only versus path-based identification without reason.

**Good example:**

```text
GET    /users
GET    /users/{userId}
GET    /users/{userId}/orders
GET    /users/{userId}/orders/{orderId}
POST   /users
```

**Bad example:**

```text
GET  /getAllUsers
GET  /fetchUserById?id={userId}
POST /createNewUser
GET  /userOrders?userId={userId}
POST /processUserOrderCreation
```

### Example 3: HTTP status codes

Title: Reflect outcomes with standard codes, not 200 for every case
Description: Use `200` for successful bodies, `201` + `Location` for creation, `204` for success with no body, `400` for validation/syntax, `401`/`403` for authz failures, `404` for missing resources, and `500` only for unexpected server faults—without stuffing errors into a 200 OK body.

**Good example:**

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/orders")
class OrderController {

    @PostMapping
    ResponseEntity<OrderDTO> create(@RequestBody OrderCreateDTO body) {
        OrderDTO created = new OrderDTO("order-123");
        return ResponseEntity.created(URI.create("/api/v1/orders/" + created.id())).body(created); // 201
    }

    @GetMapping("/{id}")
    ResponseEntity<OrderDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(new OrderDTO(id)); // 200; 404 handled by @ControllerAdvice
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> cancel(@PathVariable String id) {
        return ResponseEntity.noContent().build(); // 204
    }
}

record OrderDTO(String id) { }
record OrderCreateDTO(String productId, int quantity) { }
```

**Bad example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
class BadStatusController {

    @PostMapping
    ResponseEntity<String> createOrder(@RequestBody OrderCreateDTO body) {
        // Created, but returns 200 instead of 201 — no Location header
        return ResponseEntity.ok("Order created: order-123");
    }

    @GetMapping("/{id}")
    ResponseEntity<String> getOrder(@PathVariable String id) {
        // Stuffs error into 200 OK body instead of returning 404
        if ("unknown".equals(id)) {
            return ResponseEntity.ok("{\"error\":\"Order not found\"}");
        }
        return ResponseEntity.ok("{\"id\":\"" + id + "\"}");
    }
}
record OrderCreateDTO(String productId, int quantity) { }
```

### Example 4: DTOs for requests and responses

Title: Decouple API contracts from persistence entities
Description: Use dedicated request/response types so serialization matches what clients should see. Do not return JPA entities with internal fields (password hashes, audit columns) directly from controllers.

**Good example:**

```java
// Domain entity — internal state never exposed directly
class User {
    Long id;
    String username;
    String passwordHash; // never sent to clients
    String email;
}

// Lean response: no passwordHash, no audit columns
record UserResponseDTO(Long id, String username, String email) { }

// Inbound: password accepted once, never echoed back
record UserCreateRequestDTO(String username, String password, String email) { }

// Controller returns UserResponseDTO / accepts UserCreateRequestDTO — not User entity
```

**Bad example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class AnotherUserController {

    @GetMapping("/rawusers/{id}")
    ResponseEntity<User> getRawUser(@PathVariable String id) {
        return ResponseEntity.ok(findUserById(id));
    }

    private User findUserById(String id) {
        return new User();
    }
}

class User {
    private String passwordHash;
}
```

### Example 5: API versioning

Title: Version explicitly and consistently — URI path, custom request header, or vendor media type
Description: Introduce versioning early so breaking changes do not silently break existing clients. Three common strategies are shown below. Pick one and apply it **uniformly** across every controller in the API surface; mixing strategies creates confusion and unpredictable routing. | Strategy | Mechanism | Trade-offs | |---|---|---| | **URI path** | `/api/v1/…` | Easiest to browse, cache, and test; version visible in logs and firewalls | | **Custom header** | `X-API-Version: 2` | Clean URIs; version invisible in browser address bar and CDN cache keys | | **Vendor media type** | `Accept: application/vnd.example.product+json;version=2` | Fully REST-compliant; higher client complexity |

**Good example:**

```java
// ── Strategy 1: URI path versioning ──────────────────────────────────────────
import org.springframework.web.bind.annotation.*;

// V1 route: /api/v1/products/{id}
@RestController
@RequestMapping("/api/v1/products")
class ProductControllerV1 {

    @GetMapping("/{id}")
    ProductDTOv1 get(@PathVariable String id) {
        return new ProductDTOv1("widget", 9.99);
    }
}

// V2 route: /api/v2/products/{id} — breaking shape change isolated behind the new path
@RestController
@RequestMapping("/api/v2/products")
class ProductControllerV2 {

    @GetMapping("/{id}")
    ProductDTOv2 get(@PathVariable String id) {
        return new ProductDTOv2("widget", new Money(999, "USD"));
    }
}

record ProductDTOv1(String name, double price) { }
record ProductDTOv2(String name, Money price) { }
record Money(int amountCents, String currency) { }
```

**Bad example:**

```java
import org.springframework.web.bind.annotation.*;

// No versioning at all — any breaking change silently breaks every client
@RestController
@RequestMapping("/products")
class UnversionedProductController {

    @GetMapping("/{id}")
    ProductDTO getProduct(@PathVariable String id) {
        return new ProductDTO();
    }
}
class ProductDTO { }
```

### Example 6: Graceful, structured error responses

Title: Machine-readable errors; no stack traces in production
Description: Return JSON (or Problem Details) with stable fields: code/type, message, and optional field-level details for validation. Log full exceptions server-side; do not return stack traces to API clients in production.

**Good example:**

```java
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

record ErrorResponse(String errorCode, String message, List<String> details) {
    ErrorResponse(String errorCode, String message) {
        this(errorCode, message, List.of());
    }
}

@ControllerAdvice
class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handleNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse("RESOURCE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
            .toList();
        return new ErrorResponse("VALIDATION_ERROR", "Input validation failed", errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        return new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred.");
    }
}

class ResourceNotFoundException extends RuntimeException {
    ResourceNotFoundException(String m) { super(m); }
}
```

**Bad example:**

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class BadErrorHandlingController {

    @GetMapping("/items/{id}")
    ResponseEntity<String> getItem(@PathVariable String id) {
        if ("unknown".equals(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found!");
        }
        try {
            if ("fail".equals(id)) {
                throw new NullPointerException("Simulated internal error");
            }
            return ResponseEntity.ok("Item data");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }
}
```

### Example 7: API documentation

Title: Use OpenAPI annotations for operations, parameters, and responses
Description: Document resources, methods, schemas, status codes, and auth requirements using springdoc OpenAPI or Spring REST Docs so consumers integrate without reverse-engineering controllers.

**Good example:**

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/widgets")
@Tag(name = "Widget API", description = "Manage widgets")
class WidgetController {

    @Operation(
        summary = "Get widget by ID",
        responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = WidgetDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found")
        })
    @GetMapping("/{widgetId}")
    ResponseEntity<WidgetDTO> getWidget(
            @Parameter(description = "Widget id", required = true) @PathVariable String widgetId) {
        if ("unknown".equals(widgetId)) {
            throw new ResourceNotFoundException("not found");
        }
        return ResponseEntity.ok(new WidgetDTO());
    }
}
class WidgetDTO { }
class ResourceNotFoundException extends RuntimeException {
    ResourceNotFoundException(String m) { super(m); }
}
```

**Bad example:**

```java
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/legacy/things")
class LegacyThingController {

    @GetMapping("/{id}")
    Object getAThing(@PathVariable String id, @RequestParam(required = false) String type) {
        return new Object();
    }
}
```

### Example 8: Controller advice and ProblemDetail

Title: Centralize exception mapping; keep controllers thin
Description: Use `@ControllerAdvice` to map exceptions to HTTP responses once. Prefer Spring’s `ProblemDetail` (RFC 7807) for a consistent shape: type, title, status, detail, instance, and optional extension properties like `errorId` for correlation. **The `Exception.class` handler is the last frontier**: place it at the end of your `@ControllerAdvice` as a catch-all safety net. Its sole purpose is to ensure that any throwable not matched by a more specific handler still returns an opaque `500 Internal Server Error`—with a correlation `errorId` and a full server-side log—without leaking stack traces or internal details to the caller. **Do not abuse the last frontier by routing domain exceptions through it.** Every named domain exception (`OrderNotFoundException`, `PaymentDeclinedException`, `InsufficientStockException`, …) must have its **own** `@ExceptionHandler` method with the precise HTTP status and `ProblemDetail` type that reflects its meaning. Funneling domain exceptions into the generic `Exception.class` handler causes three problems: - The caller always receives `500` instead of the semantically correct status (`404`, `409`, `422`, …). - The `ProblemDetail` type URI is generic and useless for automated error handling. - The intent of the exception is hidden, making debugging and API contracts harder to reason about.

**Good example:**

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ControllerAdvice
class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Each domain exception gets its own handler with the correct HTTP status and problem type.
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setType(URI.create("https://example.com/problems/invalid-argument"));
        pd.setTitle("Invalid Argument");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("timestamp", Instant.now());
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ProblemDetail> handleNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("https://example.com/problems/entity-not-found"));
        pd.setTitle("Entity Not Found");
        pd.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .toList();
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        pd.setType(URI.create("https://example.com/problems/validation-error"));
        pd.setProperty("violations", errors);
        pd.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.badRequest().body(pd);
    }

    // Last frontier: catches anything not handled above.
    // Keep this handler intentionally thin — log with a correlation ID and return an opaque 500.
    // Never add domain exceptions here; give them their own @ExceptionHandler instead.
    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> handleGeneric(Exception ex, HttpServletRequest request) {
        String errorId = UUID.randomUUID().toString();
        log.error("Unhandled error {}", errorId, ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        pd.setType(URI.create("https://example.com/problems/internal-error"));
        pd.setProperty("errorId", errorId);
        pd.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }
}

class EntityNotFoundException extends RuntimeException {
    EntityNotFoundException(String m) { super(m); }
}
```

**Bad example:**

```java
// BAD: per-controller try/catch — exception handling duplicated across every endpoint
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class BadUserController {

    @GetMapping("/users/{id}")
    ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            if ("invalid".equals(id)) {
                throw new IllegalArgumentException("Invalid user ID");
            }
            if ("notfound".equals(id)) {
                throw new EntityNotFoundException("User not found");
            }
            return ResponseEntity.ok(new UserDTO());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e);
        }
    }
}
class UserDTO { }
class EntityNotFoundException extends RuntimeException {
    EntityNotFoundException(String m) { super(m); }
}
```

### Example 9: RFC 7807 Problem Details quality

Title: Consistent problem bodies; never leak stack traces to clients
Description: When using Problem Details, populate `type`, `title`, `status`, `detail`, and `instance` predictably. Log exceptions with correlation IDs server-side; do not attach stack traces or arbitrary `Map` shapes that change per endpoint.

**Good example:**

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ControllerAdvice
class ProblemDetailsExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ProblemDetailsExceptionHandler.class);

    // Helper ensures type, title, instance, and timestamp are always populated — never about:blank
    private ProblemDetail build(HttpStatus status, String typeSlug, String title,
                                String detail, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setType(URI.create("https://example.com/problems/" + typeSlug));
        pd.setTitle(title);
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ProblemDetail> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        String errorId = UUID.randomUUID().toString();
        ProblemDetail pd = build(HttpStatus.INTERNAL_SERVER_ERROR, "internal-error",
            "Internal Server Error", "An unexpected error occurred.", request);
        pd.setProperty("errorId", errorId);
        log.error("Internal error {}", errorId, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<ProblemDetail> handleValidation(ValidationException ex, HttpServletRequest request) {
        ProblemDetail pd = build(HttpStatus.BAD_REQUEST, "validation-error",
            "Validation Error", ex.getMessage(), request);
        pd.setProperty("violations", ex.getViolations());
        return ResponseEntity.badRequest().body(pd);
    }
}

class ValidationException extends RuntimeException {
    private final List<String> violations;
    ValidationException(String message, List<String> violations) {
        super(message);
        this.violations = violations;
    }
    List<String> getViolations() { return violations; }
}
```

**Bad example:**

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@ControllerAdvice
class BadExceptionHandler {

    // Raw Map — no stable shape; clients cannot rely on field names being consistent
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Map<String, Object> handleRuntime(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("msg", "Something went wrong");
        error.put("stack_trace", Arrays.toString(ex.getStackTrace())); // leaks internals
        return error;
    }

    // ProblemDetail with type never set — defaults to about:blank, useless for clients
    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ProblemDetail> handleNotFound(EntityNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        // missing: pd.setType(...), pd.setTitle(...), pd.setInstance(...)
        return ResponseEntity.status(404).body(pd);
    }
}
class EntityNotFoundException extends RuntimeException {
    EntityNotFoundException(String m) { super(m); }
}
```

### Example 10: Pagination, sorting, and filtering

Title: Bounded pages, stable sorts, optional Link headers (RFC 8288)
Description: Exposing “all rows” as a single JSON array does not scale and becomes a brittle contract. Prefer Spring Data’s `Page`/`Pageable` (with `spring-data-commons` / `PageableHandlerMethodArgumentResolver`) or explicit `limit`/`cursor` parameters with **server-enforced maximums**. Whitelist sort properties to avoid sort-by-arbitrary-field injection. Optionally emit `Link` headers (`rel="next"` / `rel="prev"`) for discoverable navigation (RFC 8288).

**Good example:**

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/products")
class ProductQueryController {

    private static final int MAX_PAGE_SIZE = 100;
    private static final Set<String> ALLOWED_SORT = Set.of("createdAt", "name", "price");

    @GetMapping
    ResponseEntity<Page<ProductDTO>> list(
            @RequestParam(required = false) String category,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("page size exceeds maximum of " + MAX_PAGE_SIZE);
        }
        pageable.getSort().forEach(order -> {
            if (!ALLOWED_SORT.contains(order.getProperty())) {
                throw new IllegalArgumentException("sort by '" + order.getProperty() + "' is not allowed");
            }
        });
        Page<ProductDTO> page = Page.empty(pageable);
        return ResponseEntity.ok()
            .header(HttpHeaders.LINK, "</api/v1/products?page=1&size=20>; rel=\"next\"")
            .body(page);
    }
}
class ProductDTO { }
```

**Bad example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
class UnboundedProductController {

    @GetMapping
    ResponseEntity<List<ProductDTO>> listAll(@RequestParam(required = false) String sort) {
        // No result cap, no sort whitelist — client can sort by any column, including internal ones
        return ResponseEntity.ok(List.of());
    }
}
class ProductDTO { }
```

### Example 11: Validation at the boundary

Title: `@Valid` / `@Validated` on inputs; Bean Validation on DTOs
Description: Validate request bodies at the controller boundary with Jakarta Bean Validation annotations on DTOs and `@Valid` (or `@Validated` with groups) on parameters. Pair this with a `@ControllerAdvice` handler for `MethodArgumentNotValidException` so clients receive **400** with field-level violations instead of inconsistent ad hoc strings.

**Good example:**

```java
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
class UserRegistrationController {

    @PostMapping
    ResponseEntity<UserDTO> register(@Valid @RequestBody UserCreateRequest body) {
        UserDTO created = new UserDTO("user-123");
        return ResponseEntity.created(URI.create("/api/v1/users/" + created.id())).body(created);
    }
}

record UserCreateRequest(
    @NotBlank String username,
    @Email String email,
    @Size(min = 8, max = 128) String password) { }

record UserDTO(String id) { }
```

**Bad example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
class WeakValidationController {

    @PostMapping
    ResponseEntity<UserDTO> register(@RequestBody UserCreateRequest body) {
        if (body.getEmail() == null || !body.getEmail().contains("@")) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new UserDTO());
    }
}
class UserCreateRequest {
    String email;
    String getEmail() { return email; }
}
class UserDTO { }
```

### Example 12: Idempotency and safe retries

Title: `Idempotency-Key`, duplicate detection, **409 Conflict**, OpenAPI clarity
Description: Network clients retry `POST`; without idempotency keys or server-side deduplication, creates can duplicate. Accept an `Idempotency-Key` header (or equivalent) and return the same response when the key replays a completed operation. Use **409 Conflict** when the request conflicts with current resource state (e.g., duplicate business key). Document which operations are idempotent and how retries behave in OpenAPI descriptions and parameters.

**Good example:**

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/payments")
class PaymentController {

    private final Map<String, PaymentDTO> completed = new ConcurrentHashMap<>();

    @PostMapping
    @Operation(
        summary = "Create payment",
        description = "Idempotent: repeating the same Idempotency-Key returns the original outcome without a double charge.")
    ResponseEntity<PaymentDTO> create(
            @Parameter(description = "Client-generated deduplication key", required = true)
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody PaymentCreateRequest body) {
        PaymentDTO prior = completed.get(idempotencyKey);
        if (prior != null) {
            return ResponseEntity.ok(prior); // replay — same result, no duplicate charge
        }
        PaymentDTO created = new PaymentDTO("pay-" + idempotencyKey.hashCode());
        completed.put(idempotencyKey, created);
        // duplicate business-key conflict → return HttpStatus.CONFLICT (409)
        return ResponseEntity.created(URI.create("/api/v1/payments/" + created.id())).body(created);
    }
}
record PaymentCreateRequest(String accountId, int amountCents) { }
record PaymentDTO(String id) { }
```

**Bad example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
class NonIdempotentPaymentController {

    @PostMapping
    ResponseEntity<PaymentDTO> charge(@RequestBody PaymentCreateRequest body) {
        return ResponseEntity.ok(new PaymentDTO());
    }
}
class PaymentCreateRequest { }
class PaymentDTO { }
```

### Example 13: Concurrency control

Title: ETag, If-Match / If-None-Match, **412** / **304**
Description: Prevent lost updates with optimistic concurrency: expose a strong or weak `ETag` (or version) on reads; require `If-Match` on mutating writes and return **412 Precondition Failed** when the version does not match. For reads, `If-None-Match` can yield **304 Not Modified**. Spring can add shallow ETags for some responses via `ShallowEtagHeaderFilter`, or you can set `eTag(...)` explicitly on `ResponseEntity`.

**Good example:**

```java
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/api/v1/items")
class ItemConcurrencyController {

    @GetMapping("/{id}")
    ResponseEntity<ItemDTO> get(@PathVariable String id, WebRequest request) {
        ItemDTO dto = new ItemDTO();
        String etag = "\"v3-" + id + "\"";
        if (request.checkNotModified(etag)) {
            return null; // Spring MVC writes the 304 Not Modified response; returning null here is correct
        }
        return ResponseEntity.ok().eTag(etag).body(dto);
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> put(
            @PathVariable String id,
            @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
            @RequestBody ItemDTO body) {
        if (!ifMatch.equals("\"v3-" + id + "\"")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
        return ResponseEntity.noContent().eTag("\"v4-" + id + "\"").build();
    }
}
class ItemDTO { }
```

**Bad example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
class BlindOverwriteController {

    @PutMapping("/{id}")
    ResponseEntity<ItemDTO> put(@PathVariable String id, @RequestBody ItemDTO body) {
        return ResponseEntity.ok(body);
    }
}
class ItemDTO { }
```

### Example 14: Caching semantics

Title: `Cache-Control`, ETag/Last-Modified, private vs public data
Description: Use `CacheControl` (or raw `Cache-Control` values) so intermediaries and browsers behave predictably: public, max-age for anonymous catalog data; `no-store` / `private` for tokens, sessions, or personalized “/me” payloads. Pair with `ETag`/`Last-Modified` for conditional GETs where caching helps. Mis-caching authenticated responses can leak data across users.

**Good example:**

```java
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/public/products")
class PublicCatalogController {

    @GetMapping("/{id}")
    ResponseEntity<ProductDTO> get(@PathVariable String id) {
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
            .body(new ProductDTO());
    }
}

@RestController
class MeController {

    @GetMapping("/api/me")
    ResponseEntity<UserDTO> me() {
        return ResponseEntity.ok()
            .cacheControl(CacheControl.noStore())
            .body(new UserDTO());
    }
}
class ProductDTO { }
class UserDTO { }
```

**Bad example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class RiskyCachingController {

    @GetMapping("/api/me")
    ResponseEntity<UserDTO> profile() {
        return ResponseEntity.ok().header("Cache-Control", "public, max-age=3600").body(new UserDTO());
    }
}
class UserDTO { }
```

### Example 15: Deprecation and sunset

Title: `Deprecation`, `Sunset`, `Link` (`rel="successor-version"`)
Description: When an endpoint or version is headed for removal, advertise it with RFC-style headers so automation and humans can plan: `Deprecation` (`true` for simplicity, or a timestamp in the form `@1735689599`), `Sunset` with an HTTP-date for expected shutdown, and `Link` to the replacement (`rel="successor-version"`). Pair this with your URI or media-type versioning strategy; document the timeline in OpenAPI as well. In production, prefer emitting these headers uniformly from a `HandlerInterceptor` or `Filter` rather than repeating them on every controller method.

**Good example:**

```java
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/legacy-products")
class LegacyProductController {

    @GetMapping("/{id}")
    ResponseEntity<ProductDTO> get(@PathVariable String id) {
        return ResponseEntity.ok()
            .header("Deprecation", "true") // or use a timestamp: @1735689599
            .header("Sunset", "Sat, 31 Dec 2026 23:59:59 GMT")
            .header(HttpHeaders.LINK, "</api/v2/products>; rel=\"successor-version\"")
            .body(new ProductDTO());
        // Production tip: emit Deprecation/Sunset from a HandlerInterceptor so all deprecated
        // controllers are flagged uniformly without repeating headers on every method.
    }
}
class ProductDTO { }
```

**Bad example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
class SilentBreakController {

    @GetMapping("/{id}")
    ResponseEntity<ProductDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(new ProductDTO());
    }
}
class ProductDTO { }
```

### Example 16: Content negotiation

Title: `produces` / `consumes`, JSON default, vendor media types
Description: Declare what controllers emit and accept: default to `application/json` unless you truly need more. Vendor media types (e.g. `application/vnd.example.v1+json`) can align with versioning—then require matching `Accept`/`Content-Type` and keep the same convention everywhere. Avoid ambiguous `*/*` handlers unless you intentionally support multiple representations with clear rules.

**Good example:**

```java
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// GET has no request body — consumes belongs only on write methods
@RestController
@RequestMapping(path = "/api/widgets", produces = MediaType.APPLICATION_JSON_VALUE)
class WidgetJsonController {

    @GetMapping("/{id}")
    ResponseEntity<WidgetDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(new WidgetDTO());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<WidgetDTO> create(@RequestBody WidgetDTO body) {
        return ResponseEntity.ok(body);
    }
}

@RestController
@RequestMapping("/api/products")
class ProductVersionedMediaController {

    static final String V1 = "application/vnd.example.product+json;version=1";

    @GetMapping(value = "/{id}", produces = V1)
    ResponseEntity<ProductDTO> getV1(@PathVariable String id) {
        return ResponseEntity.ok(new ProductDTO());
    }
}
class WidgetDTO { }
class ProductDTO { }
```

**Bad example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class LooseContentController {

    @GetMapping(value = "/data", produces = "*/*")
    ResponseEntity<String> get() {
        return ResponseEntity.ok("maybe json maybe not");
    }
}
```

### Example 17: Time and locale in contracts

Title: ISO-8601 with offset; optional `Accept-Language` for errors
Description: Model timestamps as `Instant` or `OffsetDateTime` (ISO-8601 with offset) so clients interpret wall-clock unambiguously—avoid legacy `Date` and ambiguous zone-less `LocalDateTime` in public JSON unless the domain is strictly calendar-local. For localized `ProblemDetail` text, honor `Accept-Language` only if you keep stable problem `type` URIs/codes identical across locales and avoid leaking sensitive details through translated messages.

**Good example:**

```java
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/events")
class EventController {

    @GetMapping("/{id}")
    ResponseEntity<EventDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(new EventDTO(
            Instant.parse("2026-03-23T12:00:00Z"),
            OffsetDateTime.of(2026, 3, 23, 13, 0, 0, 0, ZoneOffset.ofHours(1))));
    }
}

record EventDTO(Instant occurredAt, OffsetDateTime windowStart) { }

@ControllerAdvice
class LocalizedProblemAdvice {

    private final MessageSource messages;

    LocalizedProblemAdvice(MessageSource messages) {
        this.messages = messages;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetail> badRequest(IllegalArgumentException ex, Locale locale) {
        // Both title and detail come from the message source — ex.getMessage() is never sent to clients
        String detail = messages.getMessage("problem.invalid_argument.detail", null, locale);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        pd.setType(URI.create("https://example.com/problems/invalid-argument"));
        pd.setTitle(messages.getMessage("problem.invalid_argument.title", null, locale));
        return ResponseEntity.badRequest().body(pd);
    }
}
```

**Bad example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController
@RequestMapping("/events")
class AmbiguousTimeController {

    @GetMapping("/{id}")
    ResponseEntity<EventDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(new EventDTO(new Date()));
    }
}

class EventDTO {
    private Date createdAt;
    EventDTO(Date createdAt) { this.createdAt = createdAt; }
}
```

## Output Format

- **ANALYZE** controllers and supporting types for REST semantics: HTTP verbs, URI shape, status codes, DTO boundaries, versioning, deprecation/sunset/`Link` headers, `produces`/`consumes` and vendor media types, ISO-8601 time fields, pagination/sort/filter bounds, Bean Validation on request DTOs, idempotency headers and conflict behavior, ETag/preconditions, cache headers for public vs private data, error handling, security annotations/config, and documentation coverage
- **CATEGORIZE** findings by impact (CRITICAL for security/semantic violations, MAINTAINABILITY for DTO/versioning/docs, CONSISTENCY for errors) and by area (routing, responses, errors, security, docs)
- **APPLY** changes that align with these principles: fix unsafe GETs, normalize paths, return appropriate status codes, introduce or narrow DTOs, add versioning, emit deprecation/sunset/`Link` when phasing out endpoints, tighten `produces`/`consumes` (JSON default, vendor types only when versioning warrants it), use `OffsetDateTime`/`Instant` in API DTOs, bound list endpoints (`Page`/`Pageable` or capped cursors), add `@Valid` on request bodies with Bean Validation, add idempotency and conflict (**409**) patterns where writes retry, add ETag/`If-Match` or version checks for updates, set `Cache-Control` appropriately, centralize exception handling with Problem Details where suitable, tighten security configuration, and enrich OpenAPI metadata
- **IMPLEMENT** incrementally: preserve public API contracts when possible; use deprecation and versioning for breaking changes; keep error shapes backward compatible unless versioning allows a break
- **EXPLAIN** trade-offs (e.g., URI vs header versioning, custom ErrorResponse vs ProblemDetail) when multiple valid options exist
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after substantive edits; exercise critical endpoints in integration tests where available

## Safeguards

- **BLOCKING SAFETY CHECK**: ALWAYS run `./mvnw compile` or `mvn compile` before ANY REST API refactoring — compilation failure is a HARD STOP
- **CRITICAL VALIDATION**: Run `./mvnw clean verify` after changes that touch controllers, security, or DTOs
- **SECURITY**: Never log or return secrets, tokens, or passwords in API responses; validate that error payloads do not include stack traces in production
- **COMPATIBILITY**: Changing status codes, DTO field names, or error JSON shapes can break clients — coordinate versioning or deprecation
- **SAFETY PROTOCOL**: Stop and involve the user if compilation or tests fail after edits
- **INCREMENTAL SAFETY**: Prefer small, reviewable controller and advice changes over large sweeping rewrites