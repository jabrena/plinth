# Getting started for Agents for Java

If you want to use the **Agents for Java** definitions shipped with this repository, read this document. It covers the curated agents under [`.cursor/agents`](../.cursor/agents) used for Java Enterprise delivery workflows (excluding the diagramming specialist; see note below).

## Concepts related to this project

### What is a Cursor Agent?

In Cursor, an **agent** is a reusable persona defined as a Markdown file with YAML frontmatter (`name`, `model`, `description`, and optional flags such as `readonly`). Cursor loads these files from `.cursor/agents/`. Each agent file spells out role, responsibilities, constraints, and which Skills or rules to apply—so the model behaves consistently for that kind of task.

### How these agents fit together

- **Requirements quality:** [robot-business-analyst](../.cursor/agents/robot-business-analyst.md) is **read-only**: it reviews user stories, plans, and ADRs for alignment, gaps, and traceability—useful before sign-off or when documents disagree.
- **Orchestration:** [robot-coordinator](../.cursor/agents/robot-coordinator.md) reads implementation plans (especially tables with a **Parallel** column), identifies the stack (Spring Boot, Quarkus, Micronaut, or plain Java), and **delegates** work to exactly one implementation agent for the engagement. It does **not** implement code itself.
- **Implementation (delegation only):** [robot-java-coder](../.cursor/agents/robot-java-coder.md), [robot-spring-boot-coder](../.cursor/agents/robot-spring-boot-coder.md), [robot-quarkus-coder](../.cursor/agents/robot-quarkus-coder.md), and [robot-micronaut-coder](../.cursor/agents/robot-micronaut-coder.md) are the specialists the **coordinator** delegates to. You should **not** `@`-mention them directly for plan-driven implementation—start with [@robot-coordinator](../.cursor/agents/robot-coordinator.md) so it picks the right stack and hands off work (including **Parallel** groups). They reference the **Skills for Java** catalog (for example `@301`–`@323` for Spring Boot) when relevant.

**Note:** The repository may include other agent definitions in `.cursor/agents` (for example architecture or diagramming specialists). This guide covers only the **Java delivery and BA** agents listed below.

### Dependencies on Skills and System prompts

Agents assume you can attach **Skills** (see [Getting started for Skills for Java](GETTING-STARTED-SKILLS.md)) and/or **Cursor rules** (see [Getting started for System prompts for Java](GETTING-STARTED-SYSTEM-PROMPTS.md)) so that `@…` references resolve in chat. The implementation agents name concrete skill prefixes (for example `@322-frameworks-spring-boot-testing-integration-tests`).

## How to install the Agents?

Agents live next to your other Cursor project config. Install them the same way you install **Cursor rules**: copy the whole `.cursor` directory into the root of your Java repository (or use the same workflow you already use for `.cursor/rules`).

- **From a Git clone or ZIP:** Copy the folder [`.cursor/agents`](../.cursor/agents) (or the entire `.cursor` tree) into your project.
- **JBang setup (rules + Cursor layout):** If you use the setup described in [Getting started for System prompts for Java](GETTING-STARTED-SYSTEM-PROMPTS.md), ensure your process also preserves or copies `.cursor/agents` when you sync from this repository.

After installation you should see files such as `robot-coordinator.md` under `.cursor/agents/` in your project.

## Agent catalog (Java delivery and BA)

For **implementation from a plan**, your entry point is **[@robot-coordinator](../.cursor/agents/robot-coordinator.md)** only. It identifies the stack and delegates to the correct `robot-*-coder`; do **not** `@`-mention the coder agents yourself for that workflow.

| Agent | Role | How you use it |
|-------|------|----------------|
| [robot-business-analyst](../.cursor/agents/robot-business-analyst.md) | BA review: stories ↔ plan ↔ ADRs; **readonly**. | **@ this agent directly** for requirements review (not part of the coordinator’s implementation delegation). |
| [robot-coordinator](../.cursor/agents/robot-coordinator.md) | Coordinates work from `*.plan.md`; routes to one framework-specific implementer; splits handoffs by **Parallel** group. | **@ this agent** when you have a structured plan (with **Parallel** / execution order when applicable) and want implementation delegated in order. |
| [robot-java-coder](../.cursor/agents/robot-java-coder.md) | Plain Java / Maven implementation specialist. | **Delegation target only**—the coordinator assigns this agent when the stack is plain Java or framework-neutral. |
| [robot-spring-boot-coder](../.cursor/agents/robot-spring-boot-coder.md) | Spring Boot implementation (REST, Data JDBC, test slices, etc.). | **Delegation target only**—the coordinator assigns this agent when the stack is Spring Boot. |
| [robot-quarkus-coder](../.cursor/agents/robot-quarkus-coder.md) | Quarkus implementation (Jakarta REST, CDI, Panache/JDBC, Quarkus tests). | **Delegation target only**—the coordinator assigns this agent when the stack is Quarkus. |
| [robot-micronaut-coder](../.cursor/agents/robot-micronaut-coder.md) | Micronaut implementation (`@Controller`, Micronaut Data, `HttpClient` tests). | **Delegation target only**—the coordinator assigns this agent when the stack is Micronaut. |

## Using your first Agent in Cursor

1. Open your Java repository that already has `.cursor/agents` (and preferably Skills / rules as above).
2. For **implementation**, **mention only @robot-coordinator** and attach the plan; describe milestones or constraints in the message. The coordinator’s job is to delegate to the right `robot-*-coder` (you do not `@` those coders yourself for plan execution).
3. For **requirements review**, **mention @robot-business-analyst** directly and attach or paste stories, plans, and ADRs.

**Examples:**

- *“Using @robot-coordinator, execute the plan at `path/to/PLAN-*.plan.md` and delegate implementation per Parallel groups.”*
- *“Using @robot-business-analyst, review these files for traceability: [paste or attach stories, plan, ADRs].”*
