title=What's new in Cursor rules for Java 0.13.0?
date=2026-03-30
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What are Cursor rules for Java?

A curated collection of `Rules`, `Skills`, and `Agents` for Java Enterprise development—**to be used in** modern `SDLC` workflows.

With appreciation for our community in Singapore, Amsterdam, Madrid, Ashburn, and Atlanta. 👋👋👋

## What's new in this release?

### Improvements in the Analysis & Design phase

The project needed to provide solutions for roles not necessarily tied to the implementation phase of the SDLC, because in this kind of project, requirements and architectural documents are essential for solid implementation with AI tooling. In this release, new capabilities were added for `Product Owners`, `Business Analysts`, and `Architects`, extending support for producing stronger requirements and architectural documents.

**Skills:**

- Help with authoring user stories and acceptance criteria (`@014-agile-user-story`);
- Discover ADRs when you document CLIs or HTTP APIs (`@031-architecture-adr-functional-requirements`), and when you capture non-functional requirements and quality attributes (`@032-architecture-adr-non-functional-requirements`);
- Support for generating ER models when you need the data picture (`@033-architecture-diagrams`);
- Plan mode guidance suited to real design sessions (`@040-planning-plan-mode`).

**Agent:**

- A **Business Analyst** agent was added to assess consistency between requirements and architectural documents.

### Improvements in the Implementation phase

The big feature in this version is new support for the main Java frameworks.

**Skills**

- **Build and project docs:** Richer Maven plugin coverage and clearer “what to run and why” material derived from your build (`@112-java-maven-plugins`, `@113-java-maven-documentation`).
- **Testing:** Explicit help choosing *what* to test and *how* to think about risk, not only syntax (`@130-java-testing-strategies`).
- **Frameworks:** Initial support for the main Java frameworks like `Spring Boot`, `Quarkus` & `Micronaut`.
- **Container images:** Dockerize your application with the **Jib** Maven plugin (build OCI images from your Maven project) (`@112-java-maven-plugins`).

**Agents**

- You can delegate a development plan to a `Coordinator Agent`, which reviews the requirements and the project and, depending on the nature of the project, delegates to the right agent to implement them. Currently, the coordinator assigns work to specialized agents for `Pure Java` development or to specialized framework agents for `Spring Boot`, `Quarkus`, or `Micronaut`.

### Skill inventory

In the version `v0.13.0`, the project releases `58 Skills`:

**Analysis & Design:**

- `@012-agile-epic`
- `@013-agile-feature`
- `@014-agile-user-story`
- `@030-architecture-adr-general`
- `@031-architecture-adr-functional-requirements`
- `@032-architecture-adr-non-functional-requirements`
- `@033-architecture-diagrams`

**Planning:**

- `@040-planning-plan-mode`

**Implementation:**

- `@110-java-maven-best-practices`
- `@111-java-maven-dependencies`
- `@112-java-maven-plugins`
- `@113-java-maven-documentation`
- `@121-java-object-oriented-design`
- `@122-java-type-design`
- `@124-java-secure-coding`
- `@125-java-concurrency`
- `@126-java-exception-handling`
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

Let's walk through each feature. You can also review the [CHANGELOG](https://github.com/jabrena/plinth/blob/main/CHANGELOG.md#0130-2026-03-30).

## What workflows were updated in this release?

### Local workflows

Now, `Product Owners` and `Business Analysts` have new tools to improve thin, one-line user stories, and `Architects` can take part in development by adding `ADRs` asynchronously. With that context, you can validate those documents for inconsistencies using the new agent. When the documents are aligned, the `Software Engineer` can draft a plan and refine it with the Plan mode skill (`@040-planning-plan-mode`). After the plan is reviewed, you can hand it off to the new `Coordinator Agent`, which owns end-to-end development.

Take a look at the following [Getting Started](https://github.com/jabrena/plinth/blob/main/documentation/guides/GETTING-STARTED-SKILLS.md) guide for further details.

### CI/CD Pipeline workflows

With the new improvements for architectural tasks, it is now easier to keep architecture diagrams current—including C4 views, data mapping, business glossary documentation, and database ER diagrams.

Take a look at the following [Getting Started](https://github.com/jabrena/plinth/blob/main/documentation/guides/GETTING-STARTED-PIPELINES.md) guide for further details.

---

Drawing on ideas from others, it is impressive to see the new processes that [Juan Macias](https://x.com/juanmacias) is developing:

[![](/plintch/images/2026/3/juan-macias.png)](https://x.com/juanmacias/status/2037973784674611648?s=20)

I recommend reading [The broken telephone](https://thebrokentelephone.com/) and the associated [GitHub repository](https://github.com/darkspock/thebrokentelephone_sample).

## Finally, the Java frameworks have arrived!

Initial Spring Boot support was added in another [repository](https://github.com/jabrena/cursor-rules-spring-boot); but in this release, that support was merged here and extended. The project now covers the main Java frameworks—Spring Boot, Quarkus, and Micronaut—on Maven.

All frameworks have the same skill coverage:

- Core
- REST (API-first style)
- JDBC
- ORM (Data JDBC, Panache, Data)
- DB Migrations with Flyway
- Unit Testing
- Integration Testing
- Acceptance Testing

**Note:** In the future, Kafka support and other capabilities will be added.

### Where can you find the skills?

Currently, there is lively debate about which online Skill registry is best.
You can find the Skills from this project on several sites.

- https://skills.sh/jabrena/plinth
- https://tessl.io/registry/skills/github/jabrena/plinth
- https://agent-skills.cc/zh/skills/jabrena-cursor-rules-java
- https://skillsmp.com/skills/jabrena-cursor-rules-java-skills-014-agile-user-story-skill-md

## Why do we need Agents?

If you have followed the evolution of this project, you know we have moved from `Rules` and `System prompts` to `Skills` that shape the model's behaviour—but how do you increase execution control? The answer is agents.

In this release, we shipped a few agents for two phases:

- `Analysis & Design phase` -> Business Analyst Agent
- `Java implementation` -> Coder Coordinator Agent & Specialized Coder Agents

```
------------- Design phase ----------- | Planning  | ----- Development -----

Jira / GitHub / Azure DevOps / Redmine > Plan Mode > Agent Mode to deliver

           EA, PO, BA, SA, TL          |  TL, SWE  |       TL, SWE, SE

----------- Business Analyst --------- | --------  | --- Code Coordinator --
                Agent                                         Agent
```

Take a look at the following examples:

**Example 1:**

You have picked up a user story like this:

```markdown
## User Story Statement

**As an** API consumer/developer
**I want to** retrieve a complete list of Greek god names
through a REST API endpoint
**So that** I can integrate mythology content into
my educational application with fast,
reliable access to curated deity information.
```

After you review the technical specification and the architectural ADRs for the functional and non-functional requirements, you ask your favorite AI tool to draft a plan.


With the plan written to disk as a starting point, you enhance it with `@040-planning-plan-mode` and update it. The new task list generated to achieve the goal defined in the user story adds important information such as:

- TDD phase
- Milestone
- Parallel/Group (grouped in the London Outside-In style)
- Status


```markdown
## Task List

| # | Task | Phase | TDD | Milestone | Parallel | Status |
|---|------|-------|-----|-----------|----------|--------|
| 1 | Extend **implementation2** `pom.xml`: `quarkus-rest`, JDBC PostgreSQL, Flyway, scheduler, SmallRye OpenAPI, outbound HTTP client dep (or JDK client only), `rest-assured`, Testcontainers, Failsafe for `*IT`; plan `%test` datasource | Setup | | | A1 | Done |
| 2 | **RED:** `GreekGodsApiIT` — REST Assured `GET /api/v1/gods/greek` expects **200**, JSON array, 20 canonical names (set equality), no duplicates (fails until slice exists) | RED | Test | | A1 | Done |
| 3 | **GREEN:** Flyway migration matching [schema.sql](../design/schema.sql); seed 20 names (Flyway test data / IT setup); JDBC repository `findAllNamesOrdered()`; Jakarta REST resource; no external call on read path | GREEN | Impl | | A1 | Done |
| 4 | **Refactor:** Structured logging (read path): request or repo boundaries per team standard | Refactor | | | A1 | Done |
| 5 | **Refactor:** `ExceptionMapper` for persistence failures → 500 `application/problem+json`; empty DB IT → 200 `[]`; invalid JDBC / container stop for 500 shape; external base URL property (no hard-coded URL) | Refactor | | | A1 | Done |
| 6 | **Verify:** `./mvnw clean verify` in **implementation2**; fix failures before M2 | Verify | | milestone | A1 | Done |
```

**Note:** Remember to have `AGENTS.md`/`CLAUDE.md` in your repository to close the loop to verify your development. If you don´t know how to create that file, you can use `@200-agents-md`.

When the plan is revised and you agree with it, you can invoke the new Agent: `/robot-coordinator` to implement the full plan or the first milestone.

This agent will review the plan and repository and, depending on the context, it will delegate the coding tasks to a specific agent. Specialized agents are currently available for Pure Java, Spring Boot, Quarkus, and Micronaut.

![](/plintch/images/2026/3/robot-coordinator-example.jpg)

When you achieve the first milestone, you can review whether the implementation matches the coding standards and continue until the end of all tasks in the task list.

![](/plintch/images/2026/3/robot-quarkus-coder-example.jpg)

Using incremental review, you can minimize potential deviations and commit small parts.

![](/plintch/images/2026/3/robot-coordinator-example2.png)

If you have good ADRs and you invested time in a plan, the time spent on refactorings should be minimized. Indeed, I recommend making small commits per milestone.

## What is the next step?

### Internal changes in the generators

In the next iteration, it is necessary to make some changes in the project.

**Skills:**
- Deprecate Rules in favor of Skills. Currently, each skill has a 1:1 relationship to a reference file that is a Cursor rule; in the future, a rule could have multiple 1:1 references, so this change will improve the flexibility of the whole solution.

On the other hand, it is possible to improve architectural operations with a new Agent.

### More agents and a new workflow

**Agents:**
- Define an Agent dedicated to Architecture

**Workflows:**
- It is necessary to provide an `Execution Workflow` that covers the whole process from a user story or spec.

### How to combine user stories with spec documents?

- The project already covers user stories and ADRs, but how should those combine with spec documents? — [Mica](https://x.com/micael_gallego) raises this question.

## Interesting projects from the ecosystem

Some interesting skills to review:

- https://github.com/eferro/skill-factory/blob/main/output_skills/practices/code-simplifier
- https://github.com/eferro/skill-factory/blob/main/output_skills/practices/story-splitting
- https://github.com/eferro/skill-factory/blob/main/output_skills/practices/hamburger-method
- https://github.com/Envy-7z/mobile-agent-skillpack/tree/main/skills/thinkies
- https://github.com/bsene/skills/tree/main/mikado-method
- https://github.com/bsene/skills/tree/main/cupid-checker

## Do you still have questions about the project?

If you feel stuck using this project or have questions, you can attend the following workshop at `Codemotion Madrid 2026`:

[![](/plintch/images/2026/3/codemotion-madrid-2026.jpg)](https://conferences.codemotion.com/madrid/)

https://conferences.codemotion.com/madrid/workshop/
