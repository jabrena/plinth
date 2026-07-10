# Framework-Agnostic Acceptance Demo

Plain Java fixture used by the `133-java-testing-acceptance-tests` acceptance prompt.

The service intentionally avoids Spring Boot, Quarkus, and Micronaut. It starts a JDK
`HttpServer`, stores customers through JDBC, and calls a configurable third-party
`customer-risk` HTTP service.

The acceptance fixture uses PostgreSQL through Testcontainers as the project-local
database fixture adapter, so `./mvnw clean verify` requires a running Docker-compatible
container runtime.
