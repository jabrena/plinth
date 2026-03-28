title=What's new in Cursor rules for Java 0.13.0?
date=2026-03-30
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What are Cursor rules for Java?

A curated collection of `Rules`, `Skills`, and `Agents` for Java Enterprise development usable across **cross-functional stakeholders** (Product Owners, Business Analysts, Architects, Software Engineers) and **CI/CD pipelines** so each role (and automation) can apply consistent guidance through modern `SDLC` workflows.

Happy that good people from Singapore, Amsterdam, Madrid, Ashburn & Atlanta are using this project. 👋👋👋

## What's new in this release?

Little by little the project has increased the project audience not only putting focus on the implementation of features with Java because using AI Tooling, requirements require a good collaboration for other roles like `Product Owners`, `Business Analysts` & `Architects`.

[![](/cursor-rules-java/images/2026/3/sdlc.png)](https://en.wikipedia.org/wiki/Systems_development_life_cycle)


This release is about **spending less time hunting conventions** and **more time shipping correct Java**: Skills turn the project’s rules into focused, invokable workflows in Cursor, and new Agents give you specialists that already speak your stack.

**Skills — what changes for you**

- **Architecture and planning:** Easier, lighter ADR discovery when you document CLIs or HTTP APIs (`@031-architecture-adr-functional-requirements`); architecture diagrams that stay practical—C4 from context through component plus ER models when you need the data picture (`@033-architecture-diagrams`); Plan mode guidance that fits real design sessions (`@040-planning-plan-mode`).
- **Build and project docs:** Richer Maven plugin coverage (including container builds with Jib) and clearer “what to run and why” material derived from your build (`@112-java-maven-plugins`, `@113-java-maven-documentation`).
- **Testing:** Explicit help choosing *what* to test and *how* to think about risk, not only syntax (`@130-java-testing-strategies`).
- **Spring Boot, Quarkus, and Micronaut:** One coherent story from APIs and configuration through data access, migrations, and tests—so whether you ship on Spring (`@301`–`@323`), Quarkus (`@401`–`@423`), or Micronaut (`@501`–`@523`), the assistant can follow the same boundaries and idioms end to end.

**Agents — what changes for you**

- You can delegate by role: business analysis and coordination on one side, and framework-aware engineering agents for plain Java plus Spring Boot, Quarkus, and Micronaut—so prompts land on a model that already assumes your ecosystem.

**Project quality**

- Rule XML now validates against **PML Schema 0.7.0** using the [published schema](https://jabrena.github.io/pml/schemas/0.7.0/pml.xsd), which keeps generated rules and Skills more predictable as the library grows.

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
