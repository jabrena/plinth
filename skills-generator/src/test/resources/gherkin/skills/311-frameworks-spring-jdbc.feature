Feature: Validate changes from usage of Spring JDBC skill

Background:
  Given the skill "311-frameworks-spring-jdbc"

@acceptance-test
Scenario: Recommend production-dialect Testcontainers for Spring JDBC slice tests
  Given the example project "examples/frameworks/spring-boot"
  And the user request is "Review Spring JDBC repository code and test guidance for a PostgreSQL-backed service"
  And the local generated skill path ".agents/skills/311-frameworks-spring-jdbc"
  And the folder "examples/frameworks/spring-boot" has no git changes
  When the skill ".agents/skills/311-frameworks-spring-jdbc" is applied to the example project
  Then the skill reads "references/311-frameworks-spring-jdbc.md"
  And "./mvnw compile" is run from "examples/frameworks/spring-boot" before applying Spring JDBC changes
  And the skill recommends `JdbcClient` as the default Spring JDBC API for queries and updates
  And the skill keeps repository tests as `@JdbcTest` slices with `@Import` for the repository under test
  And the skill recommends `@Sql` fixtures for deterministic setup and cleanup
  And the skill recommends Testcontainers with the production database dialect for JDBC persistence tests
  And the skill includes `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)` so Spring Boot does not replace the datasource with embedded H2
  And the skill includes a static `@Container` database with `@ServiceConnection` for Spring Boot datasource wiring
  And the skill does not recommend embedded H2 as an acceptable substitute for production-dialect JDBC verification
  And "./mvnw clean verify" is run from "examples/frameworks/spring-boot" after improvements when code or build files change
  And any git changes produced during skill execution and verification are reset
