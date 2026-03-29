title=What's new in Cursor rules for Java 0.13.0?
date=2026-03-30
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What are Cursor rules for Java?

A curated collection of `Rules`, `Skills`, and `Agents` for Java Enterprise development—**to be used in** modern `SDLC` workflows—spanning **cross-functional stakeholders** (Product Owners, Business Analysts, Architects, Software Engineers) and **CI/CD** pipelines, so that each role (and automation) applies consistent guidance.

Happy that good people from Singapore, Amsterdam, Madrid, Ashburn & Atlanta are using this project. 👋👋👋

## What's new in this release?

### Improvements in the Analysis & Design phase

The project needed to provide solutions for roles not necessarily tied to the implementation phase of the SDLC, because in this kind of project, requirements and architectural documents are essential for solid implementation with AI tooling. In this release, new capabilities were added for `Product Owners`, `Business Analysts`, and `Architects`. With that in mind, this release adds further support to produce stronger requirements and architectural documents.

**Skills:**

- Help with authoring user stories and acceptance criteria (`@014-agile-user-story`);
- Discover ADRs when you document CLIs or HTTP APIs (`@031-architecture-adr-functional-requirements`), and when you capture non-functional requirements and quality attributes (`@032-architecture-adr-non-functional-requirements`);
- Support for generating ER models when you need the data picture (`@033-architecture-diagrams`);
- Plan mode guidance suited to real design sessions (`@040-planning-plan-mode`).

**Agent:**

- A **Business Analyst** agent was added to analyze consistency between requirements and architectural documents.

### Improvements in the Implementation phase

**Skills**

- **Build and project docs:** Richer Maven plugin coverage (including container builds with Jib) and clearer “what to run and why” material derived from your build (`@112-java-maven-plugins`, `@113-java-maven-documentation`).
- **Testing:** Explicit help choosing *what* to test and *how* to think about risk, not only syntax (`@130-java-testing-strategies`).
- **Frameworks:** Initial support for the main Java frameworks like `Spring Boot`, `Quarkus` & `Micronaut`.
- **Container images:** Dockerize your application with the **Jib** Maven plugin (build OCI images from your Maven project) (`@112-java-maven-plugins`).

**Agents**

- You can delegate a development plan to a `Coordinator Agent`, which reviews the requirements and project and, depending on the nature of the project, delegates to the right agent to implement them. Currently the coordinator assigns work to specialized agents for `Pure Java` development or to specialized framework agents for `Spring Boot`, `Quarkus`, or `Micronaut`.

Let's walk through each feature. You can also review the [CHANGELOG](https://github.com/jabrena/cursor-rules-java/blob/main/CHANGELOG.md#0130-2026-03-30).

## What workflows were updated in this release?

### Local workflows

Now, `Product Owners` and `Business Analysts` have new tools to improve thin, one-line user stories, and `Architects` can take part in development by adding `ADRs` asynchronously. With that context, you can validate those documents for inconsistencies using the new agent. When the documents are aligned, the `Software engineer` can draft a plan and refine it with the Plan mode skill (`@040-planning-plan-mode`). After the plan is reviewed, you can hand it off to the new `Coordinator Agent`, which owns end-to-end development.

Take a look at the following [Getting Started](https://github.com/jabrena/cursor-rules-java/blob/main/documentation/GETTING-STARTED-SKILLS.md) guide for further details.

### CI/CD Pipeline workflows

With the new improvements for architectural tasks, it is now easier to keep architecture diagrams current—including C4 views, data mapping, business glossary documentation, and database ER diagrams.

Take a look at the following [Getting Started](https://github.com/jabrena/cursor-rules-java/blob/main/documentation/GETTING-STARTED-PIPELINES.md) guide for further details.

---

Drawing on ideas from others, it is impressive to see the new processes that [Juan Macias](https://x.com/juanmacias) is developing:

[![](/cursor-rules-java/images/2026/3/juan-macias.png)](https://x.com/juanmacias/status/2037973784674611648?s=20)

I recommend reading [The broken telephone](https://thebrokentelephone.com/) and the associated [GitHub repository](https://github.com/darkspock/thebrokentelephone_sample).

## Finally, the frameworks have arrived

Initial Spring Boot support was added in another [repository](https://github.com/jabrena/cursor-rules-spring-boot); in this release, that support was merged here and extended. The project now covers the main Java frameworks—Spring Boot, Quarkus, and Micronaut—on Maven.

All frameworks have the same skill coverage:

- Core
- REST
- JDBC
- ORM (Data JDBC, Panache, Data)
- DB Migrations with Flyway
- Unit Testing
- Integration Testing
- Acceptance Testing

### Where can you find the skills?

You can find several registries online for these skills and others. Here are a few:

- https://skills.sh/jabrena/cursor-rules-java
- https://tessl.io/registry/skills/github/jabrena/cursor-rules-java
- https://agent-skills.cc/zh/skills/jabrena-cursor-rules-java
- https://skillsmp.com/skills/jabrena-cursor-rules-java-skills-014-agile-user-story-skill-md

## Why do we need agents?

If you have followed the evolution of this project, we have moved from `Rules` and `System prompts` to `Skills` that shape model behaviour—but how do you increase execution control? The answer is agents.

In this release, we shipped a few agents for two phases:

- `Analysis & Design phase` -> Business Analyst Agent
- `Java implementation` -> Coder Coordinator Agent

```
------------- Design phase ----------- | Planning  | ----- Development -----

Jira / GitHub / Azure DevOps / Redmine > Plan Mode > Agent Mode to deliver

           EA, PO, BA, SA, TL          |  TL, SWE  |       TL, SWE, SE

----------- Business Analyst --------- | --------  | --- Code Coordinator --
                Agent                                         Agent
```


## What is the next step?

For the next iteration, I believe it is necessary to provide an `Execution Workflow` that covers the whole process from a user story.

Some interesting skills to review from [`Eduardo Ferro`](https://www.eferro.net/):

- https://github.com/eferro/skill-factory/blob/main/output_skills/practices/code-simplifier
- https://github.com/eferro/skill-factory/blob/main/output_skills/practices/story-splitting
- https://github.com/eferro/skill-factory/blob/main/output_skills/practices/hamburger-method

And others like:

- https://github.com/Envy-7z/mobile-agent-skillpack/tree/main/skills/thinkies
- https://skills.sh/bsene/skills/mikado-method
- https://github.com/bsene/skills/tree/main/tcrdd
- https://github.com/bsene/skills/tree/main/cupid-checker

## Do you still have questions about the project?

If you feel stuck using this project or have questions, you can attend the following workshop at `Codemotion Madrid 2026`:

[![](/cursor-rules-java/images/2026/3/codemotion-madrid-2026.jpg)](https://conferences.codemotion.com/madrid/)

https://conferences.codemotion.com/madrid/workshop/
