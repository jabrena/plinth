# Greek Gods API (US-001)

## Run locally

Set PostgreSQL and start the app (defaults in `application.yml` use `localhost:5432`, database `greek_gods`, user/password `greek` unless overridden with `SPRING_DATASOURCE_*`).

```bash
./mvnw spring-boot:run
```

## Verify

Integration tests require Docker (Testcontainers). From this directory:

```bash
./mvnw clean verify
```

OpenAPI UI is available at `/swagger-ui.html` when `springdoc` is enabled (disabled under the `test` profile).
