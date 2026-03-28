title=What's new in Cursor rules for Java 0.13.0?
date=2026-03-30
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What are Cursor rules for Java?

A curated collection of `Rules`, `Skills`, and `Agents` for Java Enterprise development, designed to streamline modern `SDLC` workflows.

In this release, we are happy to add support for the good people from Singapore, Amsterdam, Madrid, Ashburn & Atlanta. 👋👋👋

## What's new in this release?

In this release, the project introduces several updates and improvements:

- **Added Skills support:**
- ER diagram support in `@033-architecture-diagrams`
- Plan mode enhancements in `@040-planning-plan-mode`
- Jib coverage and improved skill summaries in `@112-java-maven-plugins`
- `@130-java-testing-strategies` for testing strategy guidance
- Spring Boot: `@301-frameworks-spring-boot-core`, `@302-frameworks-spring-boot-rest`, `@311-frameworks-spring-jdbc`, `@312-frameworks-spring-data-jdbc`, `@313-frameworks-spring-db-migrations-flyway`, `@321-frameworks-spring-boot-testing-unit-tests`, `@322-frameworks-spring-boot-testing-integration-tests`, `@323-frameworks-spring-boot-testing-acceptance-tests`
- Quarkus: `@401-frameworks-quarkus-core`, `@402-frameworks-quarkus-rest`, `@411-frameworks-quarkus-jdbc`, `@412-frameworks-quarkus-panache`, `@413-frameworks-quarkus-db-migrations-flyway`, `@421-frameworks-quarkus-testing-unit-tests`, `@422-frameworks-quarkus-testing-integration-tests`, `@423-frameworks-quarkus-testing-acceptance-tests`
- Micronaut: `@501-frameworks-micronaut-core`, `@502-frameworks-micronaut-rest`, `@511-frameworks-micronaut-jdbc`, `@512-frameworks-micronaut-data`, `@513-frameworks-micronaut-db-migrations-flyway`, `@521-frameworks-micronaut-testing-unit-tests`, `@522-frameworks-micronaut-testing-integration-tests`, `@523-frameworks-micronaut-testing-acceptance-tests`
- Reduced complexity for ADR discovery on functional requirements in `@031-architecture-adr-functional-requirements`
- C4 model guidance in `@033-architecture-diagrams` limited to levels 1–3
- Improvements to `@113-java-maven-documentation`
- **Added Agents support:**
- Business Analyst, Coordinator, and Java, Spring Boot, Quarkus, and Micronaut framework agents
- **Project improvements:**
- Added initial support for Agents
- Migrated XML rule sources to **PML Schema 0.7.0**; the generator validates against the [published schema](https://jabrena.github.io/pml/schemas/0.7.0/pml.xsd)

Let's walk through each feature. You can also review the [CHANGELOG.md](https://github.com/jabrena/cursor-rules-java/blob/main/CHANGELOG.md#0130-2026-03-30)

## Adding support for Spring Boot, Quarkus & Micronaut over Maven

- https://skills.sh/?q=spring-boot
- https://skills.sh/?q=quarkus
- https://skills.sh/?q=micronaut
- https://skills.sh/?q=maven

### What Skills were generated in this release?

This release includes 58 Skills:

- `@012-agile-epic`
- `@013-agile-feature`
- `@014-agile-user-story`
- `@030-architecture-adr-general`
- `@031-architecture-adr-functional-requirements`
- `@032-architecture-adr-non-functional-requirements`
- `@033-architecture-diagrams`
- `@040-planning-plan-mode`
- `@110-java-maven-best-practices`
- `@111-java-maven-dependencies`
- `@112-java-maven-plugins`
- `@113-java-maven-documentation`
- `@121-java-object-oriented-design`
- `@122-java-type-design`
- `@123-java-exception-handling`
- `@124-java-secure-coding`
- `@125-java-concurrency`
- `@128-java-generics`
- `@130-java-testing-strategies`
- `@131-java-testing-unit-testing`
- `@132-java-testing-integration-testing`
- `@133-java-testing-acceptance-tests`
- `@141-java-refactoring-with-modern-features`
- `@142-java-functional-programming`
- `@143-java-functional-exception-handling`
- `@144-java-data-oriented-programming`
- `@151-java-performance-jmeter`
- `@161-java-profiling-detect`
- `@162-java-profiling-analyze`
- `@163-java-profiling-refactor`
- `@164-java-profiling-verify`
- `@170-java-documentation`
- `@180-java-observability-logging`
- `@200-agents-md`
- `@301-frameworks-spring-boot-core`
- `@302-frameworks-spring-boot-rest`
- `@311-frameworks-spring-jdbc`
- `@312-frameworks-spring-data-jdbc`
- `@313-frameworks-spring-db-migrations-flyway`
- `@321-frameworks-spring-boot-testing-unit-tests`
- `@322-frameworks-spring-boot-testing-integration-tests`
- `@323-frameworks-spring-boot-testing-acceptance-tests`
- `@401-frameworks-quarkus-core`
- `@402-frameworks-quarkus-rest`
- `@411-frameworks-quarkus-jdbc`
- `@412-frameworks-quarkus-panache`
- `@413-frameworks-quarkus-db-migrations-flyway`
- `@421-frameworks-quarkus-testing-unit-tests`
- `@422-frameworks-quarkus-testing-integration-tests`
- `@423-frameworks-quarkus-testing-acceptance-tests`
- `@501-frameworks-micronaut-core`
- `@502-frameworks-micronaut-rest`
- `@511-frameworks-micronaut-jdbc`
- `@512-frameworks-micronaut-data`
- `@513-frameworks-micronaut-db-migrations-flyway`
- `@521-frameworks-micronaut-testing-unit-tests`
- `@522-frameworks-micronaut-testing-integration-tests`
- `@523-frameworks-micronaut-testing-acceptance-tests`

- https://skills.sh/jabrena/cursor-rules-java

## Why Agents?

PENDING

## How to review a requirement?

PENDING

## How the software engineer can use this project?

PENDING

## Do you still have questions about the project?

If you feel stuck using this project or have questions, you can attend the following Workshop at `Codemotion Madrid 2026`:

[![](/cursor-rules-java/images/2026/3/codemotion-madrid-2026.jpg)](https://conferences.codemotion.com/madrid/)

https://conferences.codemotion.com/madrid/workshop/
