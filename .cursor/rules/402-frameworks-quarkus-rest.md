---
name: 402-frameworks-quarkus-rest
description: Use when you need to design, review, or improve REST APIs with Quarkus REST (Jakarta REST) — including resource classes, HTTP methods, status codes, request/response DTOs, Bean Validation, exception mappers, OpenAPI with SmallRye, content negotiation, pagination, and security-aware boundaries.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Quarkus REST API Guidelines

## Role

You are a Senior software engineer with extensive experience in REST API design and Quarkus

## Goal

Quarkus REST builds on Jakarta REST (`jakarta.ws.rs`). Resources should express clear HTTP semantics, validate input at the boundary, return appropriate status codes, and map failures to stable JSON (or Problem Details) without leaking stack traces. Use CDI-scoped resource classes (`@Path` + `@ApplicationScoped` or `@RequestScoped` as appropriate), inject services, and document the API with SmallRye OpenAPI. Align with the same REST design principles as Spring (`@302-frameworks-spring-boot-rest`) while using JAX-RS annotations and Quarkus extensions.

### Implementing These Principles

These guidelines are built upon the following core principles:

1. **HTTP semantics**: Use `GET`/`POST`/`PUT`/`PATCH`/`DELETE` correctly; `GET` must be safe; return **201** with `Location` for creates; **204** for successful deletes without body.
2. **Resource modeling**: Nouns in paths (`/orders/{id}`); avoid RPC-style verbs in URLs unless documenting a deliberate exception.
3. **DTOs at the edge**: Expose records or DTOs from resources — not persistence entities — unless you intentionally accept tight coupling (discouraged).
4. **Validation**: Annotate request bodies and parameters with Bean Validation; use `@Valid` on method parameters so malformed input yields **400** with constraint violations.
5. **Errors**: Implement `jakarta.ws.rs.ext.ExceptionMapper` for domain and infrastructure exceptions; return consistent JSON bodies and correct status codes (**404**, **409**, **422** as appropriate).
6. **OpenAPI**: Annotate with MicroProfile OpenAPI (`@Operation`, `@APIResponse`) or rely on sensible defaults; keep schemas aligned with DTOs.
7. **Performance**: Prefer reactive types (`Uni`, `Multi`) for non-blocking stacks when extensions support it; do not block event loops on long JDBC without `@Blocking` or worker threads.
8. **Security**: Integrate Quarkus Security (`quarkus-oidc`, `quarkus-smallrye-jwt`, or basic auth) at the filter level; avoid ad-hoc header parsing in resources.

**Cross-references**: Quarkus core — `@401-frameworks-quarkus-core`. JDBC — `@411-frameworks-quarkus-jdbc`. Panache — `@412-frameworks-quarkus-panache`. Unit tests — `@421-frameworks-quarkus-testing-unit-tests`.

## Constraints

Before applying any recommendations, ensure the project is in a valid state by running Maven compilation. Compilation failure is a BLOCKING condition that prevents any further processing.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PREREQUISITE**: Project must compile successfully before any REST refactoring
- **CRITICAL SAFETY**: If compilation fails, IMMEDIATELY STOP and DO NOT CONTINUE
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements

## Examples

### Table of contents

- Example 1: CRUD resource
- Example 2: Request validation
- Example 3: Exception mapping

### Example 1: CRUD resource

Title: Jakarta REST with correct status codes
Description: Map operations to HTTP verbs and return `Response` when you need status codes and headers such as `Location`.

**Good example:**

```java
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    private final ItemService items;

    @Inject
    public ItemResource(ItemService items) {
        this.items = items;
    }

    @GET
    public List<ItemResponse> list() {
        return items.findAll();
    }

    @GET
    @Path("{id}")
    public ItemResponse get(@PathParam("id") long id) {
        return items.findById(id);
    }

    @POST
    public Response create(@Valid CreateItemRequest body) {
        ItemResponse created = items.create(body);
        return Response.created(URI.create("/items/" + created.id())).entity(created).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id) {
        items.delete(id);
        return Response.noContent().build();
    }
}
```

**Bad example:**

```java
@Path("/api")
public class BadApi {

    @GET
    @Path("/deleteItem")
    public String deleteViaGet(@QueryParam("id") String id) {
        return "deleted"; // unsafe semantics — GET must not mutate
    }
}
```

### Example 2: Request validation

Title: @Valid on request bodies
Description: Apply Bean Validation annotations on DTOs and `@Valid` on resource parameters so Quarkus returns **400** with violation details.

**Good example:**

```java
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

public record RegisterRequest(
    @NotBlank String name,
    @NotBlank @Email String email
) { }

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    public void register(@Valid RegisterRequest body) {
        // persist
    }
}
```

**Bad example:**

```java
@POST
public void register(RegisterRequest body) {
    if (body.email() == null || !body.email().contains("@")) {
        throw new IllegalArgumentException("bad email"); // ad-hoc validation — inconsistent 400 bodies
    }
}
```

### Example 3: Exception mapping

Title: Consistent error JSON
Description: Register `ExceptionMapper` beans to translate domain exceptions into HTTP responses without duplicating try/catch in every resource method.

**Good example:**

```java
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class NotFoundMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException ex) {
        return Response.status(404)
            .entity(Map.of("error", ex.getMessage()))
            .build();
    }
}
```

**Bad example:**

```java
@GET
@Path("{id}")
public ItemResponse get(@PathParam("id") long id) {
    try {
        return service.find(id);
    } catch (NotFoundException e) {
        return null; // wrong — yields 200 with empty body or NPE
    }
}
```

## Output Format

- **ANALYZE** resources for HTTP misuse, missing validation, leaky entities, inconsistent error responses, and blocking on reactive threads
- **APPLY** DTO boundaries, exception mappers, OpenAPI annotations, and correct status codes
- **VALIDATE** with `./mvnw compile` before and `./mvnw clean verify` after changes

## Safeguards

- **BLOCKING SAFETY CHECK**: Compile before refactoring REST code
- **REACTIVE THREADS**: Long blocking JDBC or HTTP client calls may need `@Blocking` or execution on worker pool — verify under load