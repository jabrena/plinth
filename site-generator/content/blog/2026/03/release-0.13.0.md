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

## What workflows were updated in this release?

### Local workflows

Now, `Product Owners` & `Business Analysts` have new tools to enhance these anemic one line User Stories and `Architects` could participate in the development adding `ADRs` in an asynchronous way. With that useful information, it is possible to validate those documents to find inconsistencies with the new Agent. If those documents are consistent, the `Software engineer` could elaborate a plan and be enhanced with the Plan mode skill (`@040-planning-plan-mode`) with the plan reviewed, you can delegate it with the new `Coordinator Agent` which it will be in charge of the whole development.

Take a look the following [Getting Started](https://github.com/jabrena/cursor-rules-java/blob/main/documentation/GETTING-STARTED-SKILLS.md) for further details.

### CI/CD Pipeline workflows

With the new improvements for architectural tasks, now it is easier to maintain the diagrams about Architecture with C4 model, Data Mapping, Business Glossary documentation and Database ER Diagrams.

Take a look the following [Getting Started](https://github.com/jabrena/cursor-rules-java/blob/main/documentation/GETTING-STARTED-PIPELINES.md) for further details.

---

Observing ideas from others, It is pretty impressive the new processes that [Juan Macias](https://x.com/juanmacias) is developing:

[![](/cursor-rules-java/images/2026/3/juan-macias.png)](https://x.com/juanmacias/status/2037973784674611648?s=20)

I recommend to read the following book [The broken telephone](https://thebrokentelephone.com/).

## Finally, the frameworks are arrived

In the past, It was added and initial Spring Boot support in another [repository](https://github.com/jabrena/cursor-rules-spring-boot) but in this release, that support was integrated here and evolved. Now, the project has added support for the main Java frameworks: Spring Boot, Quarkus & Micronaut over Maven.

All frameworks has the same Skill support:

- Core
- REST
- Jdbc
- ORM (Data JDBC, Panache, Data)
- DB Migrations with Flyway
- Unit Testing
- Integration Testing
- Acceptance Testing

Further details:

- https://skills.sh/jabrena/cursor-rules-java

## Why we need Agents?

PENDING

## Do you still have questions about the project?

If you feel stuck using this project or have questions, you can attend the following Workshop at `Codemotion Madrid 2026`:

[![](/cursor-rules-java/images/2026/3/codemotion-madrid-2026.jpg)](https://conferences.codemotion.com/madrid/)

https://conferences.codemotion.com/madrid/workshop/
