# Skills & Agents for Java

## Stargazers over time

[![Stargazers over time](https://starchart.cc/jabrena/cursor-rules-java.svg?variant=adaptive)](https://starchart.cc/jabrena/cursor-rules-java)

[![CI Builds](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml/badge.svg)](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml)

## Goal

A curated and opinionated collection of `Skills` and `Agents` to be used in modern `SDLC` workflows for Java Enterprise development with your favorite AI Agent harness.

| QUESTION   | ROLE               | AREA             | SUPPORT                                                                                                                                                                                                             |
| ---------- | ------------------ | ---------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| WHAT / WHEN | PO, BA, EA, SA, TL | Agile & Planning | `User Stories`, `GitHub Issues` & `Jira` |
| WHY        | EA, SL, TL         | Architecture     | `ADRs` & `UML` / `C4` / `ER` Diagrams |
| HOW | SA, TL, SWE | Spec-Driven      | `AI Plan mode` & `OpenSpec` |
| HOW        | TL, SWE            | Java development | `Build system based on Maven`, `Design`, `Coding`, `Testing`, `Observability`, `Refactoring & JMH Benchmarking`, `Performance testing with JMeter`, `Profiling with Async profiler/OpenJDK tools`, `Documentation`, `Spring Boot`, `Quarkus`, `Micronaut`, `OpenAPI`, `WireMock` & `AGENTS.md` |

## Deliverables

The project generates a set of deliverables at the end of any iteration.


| Inventory     | Installation                                                                                    | Getting Started                                                                           |
| --------------- | -------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| 1. [Skills for Java](./documentation/INVENTORY-SKILLS-JAVA.md) | `npx skills add jabrena/cursor-rules-java --all --agent cursor` | [`Skills for Java`](./documentation/GETTING-STARTED-SKILLS.md)     |
| 2. [Agents for Java](./documentation/INVENTORY-AGENTS-JAVA.md) | `@003-agents-installation` Install Agents in Cursor/Claude | [`Agents for Java`](./documentation/GETTING-STARTED-AGENTS.md)     |

**⚠️ Note:** If you continue using the System prompts/rules from this project, please review [the article](https://jabrena.github.io/cursor-rules-java/blog/2026/04/release-0.14.0.html). Current `System prompts/rules` will be removed in the coming months.

### Compatibility

This project is compatible with any tool that supports `Skills`, `Agents`, `AGENTS.md`, and `MCP` servers.

## How to use them

The SDLC has evolved with this new wave of AI tooling, which enhances the software engineering process. While building this project, we identified three workflows: `Prompting Engineering Workflow`, `Agent-driven Engineering Workflow`, `Pipelines Workflow`.

### Prompting Engineering Workflow

In this workflow, the software engineer interacts with models using `User prompts`. In an incremental way, you delegate a whole task or ask for help at specific points. You can use this project to refactor generated code, or delegate the task and attach a system prompt or Skills to it.

![](./documentation/images/workflow-prompts.png)

### Agent-driven Engineering Workflow

`Agents for Java Enterprise development` were designed to help the software engineer in the implementation phase. The engineer defines solid `Specs`, and those specifications are delegated to `Agents`.

![](./documentation/images/workflow-agents.png)

### Pipelines Workflow

Adding AI tools to your pipeline can provide new opportunities to deliver more value (examples: automatic coding, code refactoring, continuous profiling, and others).

![](./documentation/images/workflow-pipelines.png)

Further information [here](./documentation/GETTING-STARTED-PIPELINES.md).

## Limitations

### Lack of determinism

From the outset, be aware that results from interactions with these `Skills` and agents are not deterministic because of how the models behave, but you can mitigate that with clear goals and validation checkpoints.

### Not all models behave in the same way

Some interactive skills require `Premium` models for interactive use; otherwise they follow a fixed sequence of steps.

### Limits of interactions with models

Models can generate code, but they cannot execute it against your local data. To bridge that gap, some Skills include scripts you run locally.

## Contribute

See [CONTRIBUTING.md](./CONTRIBUTING.md) for conventions, generator workflows, tests, and how to open a pull request.

## Examples

The repository includes [a collection of examples](./examples/) where you can explore what these Skills and workflows enable for Java.

## Architecture Decision Records (ADR)

- Review the [ADR index](./documentation/adr/README.md) for the complete list.

## Changelog

- Review the [CHANGELOG](./CHANGELOG.md) for further details.

## Java JEPs from Java 8 onward

Java uses JEPs (JDK Enhancement Proposals) to describe new language and platform features. This repository tracks which JEPs could improve the Skills and guidance here.

- [JEPs list](./documentation/jeps/All-JEPS.md)

## Meetups, Conferences, Workshops & Articles

### Codemotion / Madrid (2026/04/20 - 11:00 - 12:30)

- [Taller técnico sobre Cursor para el desarrollo con Java](https://conferences.codemotion.com/madrid/speakers/)

### W-JAX / Munich (2025/11/06 - 10:30 - 11:30)

- [https://jax.de/generative-ai-ecosystem/cursor-ai-101-java-enterprise/](https://jax.de/generative-ai-ecosystem/cursor-ai-101-java-enterprise/)

### Devoxx BE / Antwerp (2025/10/07 - 18:20 - 18:50)

- [https://m.devoxx.com/events/dvbe25/talks/4715/the-power-of-cursor-rules-in-java-enterprise-development](https://m.devoxx.com/events/dvbe25/talks/4715/the-power-of-cursor-rules-in-java-enterprise-development)

### Madrid Jug / Madrid (2025/05/06 - 19:00)

- [https://www.meetup.com/es-ES/madridjug/events/307458529/](https://www.meetup.com/es-ES/madridjug/events/307458529/)

### Blogs

- [Delegating Java tasks to Supervised AI Dev Pipelines](https://www.javaadvent.com/2025/12/delegating-java-tasks-to-supervised-ai-dev-pipelines.html)
- [https://vibekode.it/blog/cursor-ai-developer-cloud-platform/](https://vibekode.it/blog/cursor-ai-developer-cloud-platform/)
- [https://www.linkedin.com/pulse/september-rest-story-jvm-weekly-vol-146-artur-skowro%C5%84ski-82lif/?trackingId=wbWPSL65TpCCbdg5ksAWjw%3D%3D](https://www.linkedin.com/pulse/september-rest-story-jvm-weekly-vol-146-artur-skowro%C5%84ski-82lif/?trackingId=wbWPSL65TpCCbdg5ksAWjw%3D%3D)
- [https://virtuslab.com/blog/ai/providing-library-documentation/](https://virtuslab.com/blog/ai/providing-library-documentation/)

## References

- [https://www.cursor.com/](https://www.cursor.com/)
- [https://cursor.com/cli](https://cursor.com/cli)
- [https://www.anthropic.com/claude-code](https://www.anthropic.com/claude-code)
- [https://github.com/features/copilot](https://github.com/features/copilot)
- [https://cursor.com/docs/cli/github-actions](https://cursor.com/docs/cli/github-actions)
- [https://code.claude.com/docs/en/github-actions](https://code.claude.com/docs/en/github-actions)
- [https://agents.md/](https://agents.md/)
- [https://agentskills.io/home](https://agentskills.io/home)
- [https://microsoft.github.io/language-server-protocol/](https://microsoft.github.io/language-server-protocol/)
- [https://openspec.dev/](https://openspec.dev/)
- [https://skills.sh/jabrena/cursor-rules-java](https://skills.sh/jabrena/cursor-rules-java)
- [https://tessl.io/registry/skills/github/jabrena/cursor-rules-java](https://tessl.io/registry/skills/github/jabrena/cursor-rules-java)
- [https://github.com/vercel-labs/skills/issues](https://github.com/vercel-labs/skills/issues)
- [https://openjdk.org/jeps/0](https://openjdk.org/jeps/0)
- [https://jbake.org/docs/latest/](https://jbake.org/docs/latest/)

## Other developments

- [https://github.com/jabrena/pml](https://github.com/jabrena/pml)
- [https://github.com/jabrena/cursor-rules-java](https://github.com/jabrena/cursor-rules-java)
- [https://github.com/jabrena/setup-cli](https://github.com/jabrena/setup-cli)

Powered by [Cursor](https://www.cursor.com/) with ❤️ from [Madrid](https://www.google.com/maps/place/Community+of+Madrid,+Madrid/@40.4983324,-6.3162283,8z/data=!3m1!4b1!4m6!3m5!1s0xd41817a40e033b9:0x10340f3be4bc880!8m2!3d40.4167088!4d-3.5812692!16zL20vMGo0eGc?entry=ttu&g_ep=EgoyMDI1MDgxOC4wIKXMDSoASAFQAw%3D%3D)
