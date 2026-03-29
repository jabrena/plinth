title=What's new in Cursor rules for Java 0.13.0?
date=2026-03-30
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What are Cursor rules for Java?

A curated collection of `Rules`, `Skills`, and `Agents` for Java Enterprise development—**to be used in** modern `SDLC` workflows—spanning **cross-functional stakeholders** (Product Owners, Business Analysts, Architects, Software Engineers) and **CI/CD** pipelines, so each role (and automation) applies consistent guidance.

Happy that good people from Singapore, Amsterdam, Madrid, Ashburn & Atlanta are using this project. 👋👋👋

## What's new in this release?

### Improvements in Analysis & Design phase:

The project required to provide solutions to other roles not necessary related to the implementation phase in SDLC because in this kind of projects, Requirements and Architectural documents are essential for good implementation by AI Tooling so in this release, it was added new capabilitites to be used by `Product Owners`, `Business Analysts` & `Architects`. With this idea in mind, in the release it was added new capabilities to elaborate better requirements and to elaborate better architectural documents.

**Skills:**

- Help to author user stories and acceptance criteria (`@014-agile-user-story`);
- Add ADR discovery when you document CLIs or HTTP APIs (`@031-architecture-adr-functional-requirements`); and for non-functional requirements and quality attributes (`@032-architecture-adr-non-functional-requirements`);
- Added capacity to generate ER models when you need the data picture (`@033-architecture-diagrams`);
- Added Plan mode guidance that fits real design sessions (`@040-planning-plan-mode`).

**Agent:**

- A `Business analyst` Agent was added to analyze consistency between requirements and architectural documents.

### Improvements in Implementation phase:

**Skills**

- **Build and project docs:** Richer Maven plugin coverage (including container builds with Jib) and clearer “what to run and why” material derived from your build (`@112-java-maven-plugins`, `@113-java-maven-documentation`).
- **Testing:** Explicit help choosing *what* to test and *how* to think about risk, not only syntax (`@130-java-testing-strategies`).
- **Frameworks:** Initial support for the main Java frameworks like `Spring Boot`, `Quarkus` & `Micronaut`.
- **Container images:** Dockerize your application with the **Jib** Maven plugin (build OCI images from your Maven project) (`@112-java-maven-plugins`).

**Agents**

- Now you can delegate a development plan to a `Coordinator Agent` which review the requirement and project and depending of the project nature, will delegate to the right Agent to implement the requirements. Currently the coordinator delegate workload to specialized agents for `Pure Java` development or Specialized framework agents for `Spring Boot`, `Quarkus` or `Micronaut`.

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
