---
name: 302-frameworks-spring-boot-rest
description: Use when you need to design, review, or improve REST APIs with Spring Boot — including HTTP methods, resource URIs, status codes, DTOs, versioning, error handling, security, API documentation, controller advice, and problem details for errors.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
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
4. **Operational clarity**: Use `@ControllerAdvice` for consistent errors, Problem Details where appropriate, and OpenAPI (or equivalent) so clients do not rely on reading source code.
5. **Evolution without surprise**: Choose a versioning strategy (URI, header, or media type) and apply it consistently so breaking changes remain manageable.

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
- Example 7: Secure APIs
- Example 8: API documentation
- Example 9: Controller advice and ProblemDetail
- Example 10: RFC 7807 Problem Details quality

### Example 1: Use HTTP methods semantically

Title: GET for reads, POST for creates, PUT/PATCH for updates, DELETE for removal
Description: Map operations to the correct verb: `GET` must not change server state; `POST` typically creates subordinate resources; `PUT` replaces; `PATCH` partially updates; `DELETE` removes. Avoid RPC-style GET/POST misuse.

**Good example:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/users")
class UserController {

    @GetMapping("/{id}")
    ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        return ResponseEntity.ok(new UserDTO());
    }

    @PostMapping
    ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO body) {
        UserDTO created = new UserDTO();
        return ResponseEntity.created(URI.create("/users/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserUpdateDTO body) {
        return ResponseEntity.ok(new UserDTO());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable String id) {
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    ResponseEntity<UserDTO> patchUser(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(new UserDTO());
    }
}

class UserDTO {
    private String id;
    public String getId() { return id; }
}
class UserCreateDTO { }
class UserUpdateDTO { }
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

class StatusExamples {

    ResponseEntity<String> handle(boolean resourceNotFound, boolean forbidden, boolean validationFailed) {
        if (resourceNotFound) {
            return ResponseEntity.notFound().build();
        }
        if (forbidden) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        if (validationFailed) {
            return ResponseEntity.badRequest().body("Invalid input");
        }
        return ResponseEntity.ok("ok");
    }
}
```

**Bad example:**

```java
import org.springframework.http.ResponseEntity;
import java.util.Objects;

class BadStatusExamples {

    ResponseEntity<String> process(String input) {
        try {
            if (Objects.isNull(input)) {
                return ResponseEntity.ok("{\"error\":\"Input cannot be null\"}");
            }
            return ResponseEntity.ok("{\"data\":\"Success!\"}");
        } catch (Exception e) {
            return ResponseEntity.ok("{\"error\":\"Something went wrong on the server\"}");
        }
    }
}
```

### Example 4: DTOs for requests and responses

Title: Decouple API contracts from persistence entities
Description: Use dedicated request/response types so serialization matches what clients should see. Do not return JPA entities with internal fields (password hashes, audit columns) directly from controllers.

**Good example:**

```java
class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String email;
}

class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
}

class UserCreateRequestDTO {
    private String username;
    private String password;
    private String email;
}

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

Title: Version explicitly and consistently (URI, header, or media type)
Description: Introduce versioning early so breaking changes do not silently break all clients. URI versioning (e.g. `/api/v1/...`) is common; whatever strategy you pick, use it uniformly across controllers.

**Good example:**

```java
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
class ProductControllerV1 {
    @GetMapping("/{id}")
    ProductDTO get(@PathVariable String id) {
        return new ProductDTO();
    }
}

@RestController
@RequestMapping("/api/v2/products")
class ProductControllerV2 {
    @GetMapping("/{id}")
    ProductDTOv2 get(@PathVariable String id) {
        return new ProductDTOv2();
    }
}
class ProductDTO { }
class ProductDTOv2 { }
```

**Bad example:**

```java
import org.springframework.web.bind.annotation.*;

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
import java.util.List;
import java.util.stream.Collectors;

class ErrorResponse {
    private final String errorCode;
    private final String message;
    private final List<String> details;

    ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.details = List.of();
    }

    ErrorResponse(String errorCode, String message, List<String> details) {
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }
}

@ControllerAdvice
class GlobalExceptionHandler {

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
            .collect(Collectors.toList());
        return new ErrorResponse("VALIDATION_ERROR", "Input validation failed", errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handleGeneric(Exception ex) {
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

### Example 7: Secure APIs

Title: Authenticate, authorize, validate input, and avoid injection
Description: Terminate TLS for external traffic, use Spring Security (or equivalent) for authentication/authorization, validate and sanitize inputs, and use parameterized queries or repositories—never concatenate untrusted input into SQL.

**Good example:**

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> { }));
        return http.build();
    }
}

// @PreAuthorize("hasAuthority('SCOPE_read:users')") on sensitive controller methods
```

**Bad example:**

```java
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
class InsecureController {

    @PostMapping("/admin/deleteAllData")
    String deleteAllData() {
        return "All data wiped.";
    }

    @GetMapping("/products")
    List<ProductDTO> searchProducts(@RequestParam String category) {
        return List.of();
    }
}
class ProductDTO { }
```

### Example 8: API documentation

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

### Example 9: Controller advice and ProblemDetail

Title: Centralize exception mapping; keep controllers thin
Description: Use `@ControllerAdvice` to map exceptions to HTTP responses once. Prefer Spring’s `ProblemDetail` (RFC 7807) for a consistent shape: type, title, status, detail, instance, and optional extension properties like `errorId` for correlation.

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
import java.util.stream.Collectors;

@ControllerAdvice
class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
            .collect(Collectors.toList());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        pd.setType(URI.create("https://example.com/problems/validation-error"));
        pd.setProperty("violations", errors);
        pd.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.badRequest().body(pd);
    }

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

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

### Example 10: RFC 7807 Problem Details quality

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

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ProblemDetail> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        String errorId = UUID.randomUUID().toString();
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred while processing the request");
        pd.setType(URI.create("https://example.com/problems/internal-error"));
        pd.setTitle("Internal Server Error");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty("errorId", errorId);
        log.error("Internal error {}", errorId, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<ProblemDetail> handleValidation(ValidationException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setType(URI.create("https://example.com/problems/validation-error"));
        pd.setTitle("Validation Error");
        pd.setInstance(URI.create(request.getRequestURI()));
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@ControllerAdvice
class BadExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Map<String, Object> handleRuntime(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", true);
        error.put("msg", "Something went wrong");
        error.put("exception_type", ex.getClass().getSimpleName());
        error.put("stack_trace", Arrays.toString(ex.getStackTrace()));
        return error;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(404).body("Not found: " + ex.getMessage());
    }
}
class EntityNotFoundException extends RuntimeException {
    EntityNotFoundException(String m) { super(m); }
}
```

## Output Format

- **ANALYZE** controllers and supporting types for REST semantics: HTTP verbs, URI shape, status codes, DTO boundaries, versioning, error handling, security annotations/config, and documentation coverage
- **CATEGORIZE** findings by impact (CRITICAL for security/semantic violations, MAINTAINABILITY for DTO/versioning/docs, CONSISTENCY for errors) and by area (routing, responses, errors, security, docs)
- **APPLY** changes that align with these principles: fix unsafe GETs, normalize paths, return appropriate status codes, introduce or narrow DTOs, add versioning, centralize exception handling with Problem Details where suitable, tighten security configuration, and enrich OpenAPI metadata
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