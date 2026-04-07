# Skills & Agents for Java

## Stargazers over time

[![Stargazers over time](https://starchart.cc/jabrena/cursor-rules-java.svg?variant=adaptive)](https://starchart.cc/jabrena/cursor-rules-java)

[![CI Builds](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml/badge.svg)](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml)

## Goal

A curated collection of `Skills`, and `Agents` for Java Enterprise development, designed to streamline modern `SDLC` workflows.

| QUESTION   | ROLE               | AREA             | SUPPORT                                                                                                                                                                                                             |
| ---------- | ------------------ | ---------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| WHAT       | PO, BA, EA, SA, TL | Agile            | `User Stories`, `Github Issues` & `Jira`                                                                                                                                                                                    |
| WHY        | EA, SL, TL         | Architecture     | `ADRs` & `UML` / `C4` / `ER` Diagrams                                                                                                                                                                               |
| WHEN / HOW | SA, TL, SWE        | Spec-Driven      | `AGENTS.md`, `AI Plan mode` & `OpenSpec`                                                                                                                                                                            |
| HOW        | TL, SWE            | Java development | `Build system based on Maven`, `Design`, `Coding`, `Testing`, `Observability`, `Refactoring & JMH Benchmarking`, `Performance testing with JMeter`, `Profiling with Async profiler/OpenJDK tools` & `Documentation` |
| HOW        | TL, SWE            | Java frameworks  | `Spring Boot`, `Quarkus` & `Micronaut`                                                                                                                                                                              |


## Deliverables

The project generates a set of deliverables at the end of any iteration.


| Deliverable             | Location                                                                                   |
| ----------------------- | ------------------------------------------------------------------------------------------ |
| Skills for Java         | [https://skills.sh/jabrena/cursor-rules-java](https://skills.sh/jabrena/cursor-rules-java) |
| Agents for Java         | [Catalog](.cursor/agents) (`.cursor/agents`)                                               |


### Compatibility

This project is compatible with any Tool compatible with `Skills`, `Agents` or `AGENTS.md`.

## Getting started

Read the following comprehensive guides to use this project today.

- [Getting started with `Skills for Java`](./documentation/GETTING-STARTED-SKILLS.md)
- [Getting started with `Agents for Java`](./documentation/GETTING-STARTED-AGENTS.md)
- [Getting started with `CI/CD Pipelines and AI Capabilities`](./documentation/GETTING-STARTED-PIPELINES.md)

## How to use them?

The SDLC has evolved with the arrival of this new set of AI tooling, enhancing the Software Engineering process. In the development of this project, it was identified 3 different workflows: `Prompting Enginering Workflow`, `Pipelines Workflow` & `Agentic Workflow`.

### Prompting Enginering Workflow

In this workflow, the Software engineer interact with models using `User prompts` and in an incremental way you delegate a delegate completely a task or ask help in certain moments. You could use this project to refactor the code generated or delegate the task and associate a System prompt / Skills to that task.

![](./documentation/images/workflow-prompts.png)

### Agent Workflow

`Agents for Java Enterprise development` were designed to help the Software engineer in the implementation phase. The software engineer define good `Specs` and that Specifications are delegated to Agents.

![](./documentation/images/workflow-agents.png)

### Pipelines Workflow

Adding AI tools to your pipeline can provide new opportunities to deliver more value (examples: automatic coding, code refactoring, continuous profiling, and others).

![](./documentation/images/workflow-pipelines.png)

## Limitations

### Lack of determinism

From the outset, be aware that the results provided by interactions with the different `Cursor rules` are not deterministic due to the nature of the models, but this can be mitigated with clear goals and validation checkpoints.

### Not all models behave in the same way

In the project exist some Interactive skills which require to use `Premium` models to run in an interactive way in other case, they will work like in sequence of steps.

### Limits of interactions with models

Models are able to generate code, but they cannot run code with your local data. To address this limitation, some prompts provide scripts to bridge this gap on the model side.

## Contribute

See [CONTRIBUTING.md](./CONTRIBUTING.md) for conventions, generator workflows, tests, and how to open a pull request.

## Examples

The repository includes [a collection of examples](./examples/) where you can explore the possibilities of these system prompts designed for Java.

## Architectural decision records, ADR

| Date       | ID      | Name                                                                                                                                    |
| ---------- | ------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| 2026-04-06 | ADR-005 | [Drop Cursor rules support in favor of Agent Skills](./documentation/adr/ADR-005-drop-cursor-rules-support-in-favor-of-agent-skills.md) |
| 2026-03-01 | ADR-004 | [Skill Generation](./documentation/adr/ADR-004-skill-generation.md)                                                                     |
| 2025-09-16 | ADR-003 | [Website Generation](./documentation/adr/ADR-003-website-generation.md)                                                                 |
| 2025-07-10 | ADR-002 | [Cursor Rules scope configuration](./documentation/adr/ADR-002-configure-cursor-rules-manual-scope.md)                                  |
| 2025-07-08 | ADR-001 | [Cursor Rules generation from XML Files](./documentation/adr/ADR-001-generate-cursor-rules-from-xml-files.md)                           |

## Changelog

- Review the [CHANGELOG](./CHANGELOG.md) for further details

## Java JEPS from Java 8

Java uses JEPs as the vehicle to describe new features to be added to the language. The repository continuously reviews which JEPs could improve any of the cursor rules present in this repository.

- [JEPS List](./documentation/jeps/All-JEPS.md)

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
