# Commands, Agents & Skills for Java

<a href="https://trendshift.io/repositories/15013" target="_blank"><img src="https://trendshift.io/api/badge/repositories/15013" alt="jabrena%2Fcursor-rules-java | Trendshift" style="width: 250px; height: 55px;" width="250" height="55"/></a>

[![Stargazers over time](https://starchart.cc/jabrena/cursor-rules-java.svg?variant=adaptive)](https://starchart.cc/jabrena/cursor-rules-java)

[![CI Builds](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml/badge.svg)](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml)

> **Languages:** [Español](./README_ES.md) · [中文](./README_ZH.md)
>
> **Website:** https://jabrena.github.io/cursor-rules-java/
>
> **Support the project:** [Sponsor to pay tokens](https://github.com/sponsors/jabrena)

## Goal

An opinionated AI-native workflow for evolving modern Java Enterprise `SDLC` practices through reusable `Skills`, `Agents`, `Commands` & `MCP servers`.

## Latest Updates

See how the project evolves through new skills, improvements, and fixes in the [CHANGELOG](./CHANGELOG.md).

## Start in 60 seconds

Install every skill for your preferred agent:

```bash
npx skills add jabrena/cursor-rules-java --skill '*' --agent cursor -y
```

Replace `cursor` with `claude-code`, `codex`, or `github-copilot` when needed.

### See it in action

Ask your agent:

```text
Use @110-java-maven-best-practices to review this Maven project.
Explain the findings, apply the approved improvements, and validate the build.
```

The skill guides the agent through a structured Maven review while keeping you in control of proposed changes.

## 5-Minute Onboarding

Learn to use this project following the quick guide [Getting Started in 5 minutes](./documentation/guides/GETTING-STARTED-IN-5-MINUTES.md).

### Migrating from legacy rules

Current `System prompts/rules` are deprecated and will be removed in `v0.16.0`. If you still use them, review the [release 0.14.0 article](https://jabrena.github.io/cursor-rules-java/blog/2026/04/release-0.14.0.html).

## Choose your path

### Plan

Turn an idea into an actionable change with user stories, GitHub Issues or Jira, ADRs, diagrams, AI plan mode, and OpenSpec.

| Resource | Available options |
| --- | --- |
| **Commands** | `/create-issue` · [`/update-issue`](./.cursor/commands/update-issue.md) · `/explore-design` · `/create-adr` · `/create-diagram` · `/create-plan` · `/create-spec` · `/review-alignment` |
| **Agents** | `@robot-business-analyst` · `@robot-architect` · `@robot-tech-lead` |
| **Skills** | [014-agile-user-story](https://www.skills.sh/jabrena/cursor-rules-java/014-agile-user-story) · [030-architecture-adr-general](https://www.skills.sh/jabrena/cursor-rules-java/030-architecture-adr-general) · [033-architecture-diagrams](https://www.skills.sh/jabrena/cursor-rules-java/033-architecture-diagrams) · [041-planning-plan-mode](https://www.skills.sh/jabrena/cursor-rules-java/041-planning-plan-mode) · [200-agents-md](https://www.skills.sh/jabrena/cursor-rules-java/200-agents-md) |
| **MCP Servers** | [Jbang-Quarkus-JDBC](https://github.com/quarkiverse/quarkus-mcp-servers/blob/main/jdbc/README.md) · [MongoDB](https://github.com/mongodb-js/mongodb-mcp-server) · [Serena-LSP](https://oraios.github.io/serena/01-about/000_intro.html) |

### Build

Implement and improve Java applications with Maven, design, coding, testing, security, documentation, Spring Boot, Quarkus, Micronaut, OpenAPI, and WireMock guidance.

| Resource | Available options |
| --- | --- |
| **Commands** | [`/create-feature-branch`](./.cursor/commands/create-feature-branch.md) · [`/create-worktree`](./.cursor/commands/create-worktree.md) · [`/implement-issue`](./.cursor/commands/implement-issue.md) · [`/kill-port`](./.cursor/commands/kill-port.md) |
| **Agents** | `@robot-tech-lead` · `@robot-no-java` · `@robot-java-coder` · `@robot-java-spring-boot-coder` · `@robot-java-quarkus-coder` · `@robot-java-micronaut-coder` |
| **Skills** | [110-java-maven-best-practices](https://www.skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices) · [111-java-maven-dependencies](https://www.skills.sh/jabrena/cursor-rules-java/111-java-maven-dependencies) · [121-java-object-oriented-design](https://www.skills.sh/jabrena/cursor-rules-java/121-java-object-oriented-design) · [124-java-secure-coding](https://www.skills.sh/jabrena/cursor-rules-java/124-java-secure-coding) · [143-java-functional-exception-handling](https://www.skills.sh/jabrena/cursor-rules-java/143-java-functional-exception-handling) |
| **MCP Servers** | [Jbang-Quarkus-JDBC](https://github.com/quarkiverse/quarkus-mcp-servers/blob/main/jdbc/README.md) · [MongoDB](https://github.com/mongodb-js/mongodb-mcp-server) · [JavaDocs](https://www.javadocs.dev/mcp) · [Serena-LSP](https://oraios.github.io/serena/01-about/000_intro.html) |

### Compliance

Review Java systems, AI models, and how GenAI tools are used across applications and delivery pipelines for regulation-aware engineering controls, evidence, and qualified owner handoffs spanning AI, data, security, product, platform, market, and governance. **<u>These skills support engineering awareness and do not provide legal advice.</u>**

| Regulation | Skill |
| --- | --- |
| [EU AI Act](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=OJ:L_202401689) | `801-regulations-eu-ai-act` |
| [DORA](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2554) | `802-regulations-dora` |
| [GDPR](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32016R0679) | `803-regulations-gdpr` |
| [NIS2](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022L2555) | `804-regulations-eu-nis2` |
| [Cyber Resilience Act](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847) | `805-regulations-eu-cyber-resilience-act` |
| [Data Act](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854) | `806-regulations-eu-data-act` |
| [Digital Services Act](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065) | `807-regulations-eu-digital-services-act` |
| [Digital Markets Act](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925) | `808-regulations-eu-digital-markets-act` |

**Note:** This set of skills could be a good complement for the future [OWASP EU Compliance MCP](https://genai.owasp.org/solution/eu-compliance-mcp/).

### Operate

Measure and improve production behavior through observability, profiling, benchmarking, and performance testing.

| Resource | Available options |
| --- | --- |
| **Commands** | [`/profile`](./.cursor/commands/profile.md) · [`/benchmark`](./.cursor/commands/benchmark.md) |
| **Agents** | `@robot-java-performance` |
| **Skills** | [151-java-performance-jmeter](https://www.skills.sh/jabrena/cursor-rules-java/151-java-performance-jmeter) · [161-java-profiling-detect](https://www.skills.sh/jabrena/cursor-rules-java/161-java-profiling-detect) · [162-java-profiling-analyze](https://www.skills.sh/jabrena/cursor-rules-java/162-java-profiling-analyze) · [163-java-profiling-refactor](https://www.skills.sh/jabrena/cursor-rules-java/163-java-profiling-refactor) · [164-java-profiling-verify](https://www.skills.sh/jabrena/cursor-rules-java/164-java-profiling-verify) |
| **MCP Servers** | [Jbang-Quarkus-JDBC](https://github.com/quarkiverse/quarkus-mcp-servers/blob/main/jdbc/README.md) · [MongoDB](https://github.com/mongodb-js/mongodb-mcp-server) · [Serena-LSP](https://oraios.github.io/serena/01-about/000_intro.html) · [Grafana](https://grafana.com/docs/grafana/latest/developer-resources/mcp/) |

Explore the complete [Commands](./documentation/guides/INVENTORY-COMMANDS-JAVA.md), [Agents](./documentation/guides/INVENTORY-AGENTS-JAVA.md), [Skills](./documentation/guides/INVENTORY-SKILLS-JAVA.md), and [MCP Servers](./documentation/guides/THIRD-PARTIES.md) inventories.

## Deliverables

The project generates a set of deliverables at the end of any iteration.

| Inventory     | Installation                                                                                    | Getting Started                                                                           |
| --------------- | -------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| 1. [Commands](./documentation/guides/INVENTORY-COMMANDS-JAVA.md) | `@004-commands-installation` Install Commands in project | [`Commands`](./documentation/guides/COMMANDS.md) |
| 2. [Agents](./documentation/guides/INVENTORY-AGENTS-JAVA.md) | `@005-agents-installation` Install Agents in Cursor/Claude | [`Agents`](./documentation/guides/GETTING-STARTED-AGENTS.md)     |
| 3. [Skills](./documentation/guides/INVENTORY-SKILLS-JAVA.md) | `npx skills add jabrena/cursor-rules-java --skill '*' --agent cursor -y` | [`Skills`](./documentation/guides/GETTING-STARTED-SKILLS.md)     |

### Compatibility

This project is compatible with any tool that supports `Commands`, `Agents`, `Skills`, `MCP Servers` and `AGENTS.md`.

## Skill Validations

Every push runs the following validation checks in [CI Builds](./.github/workflows/maven.yaml) to keep documentation and generated skills correct, consistent, and secure:

| Name | Purpose |
| --- | --- |
| 1. [MarkdownValidator](./.github/scripts/MarkdownValidator.java) | Protects the documentation layer by catching Markdown parsing drift and remote link failures before skill-specific checks run. |
| 2. [skill-check](https://github.com/thedaviddias/skill-check) | Confirms every generated skill follows the expected packaging contract, complementing scanners that focus on behavior or security risk. |
| 3. [cisco-ai-skill-scanner](https://github.com/cisco-ai-defense/skill-scanner) by Cisco | Adds behavior-oriented security coverage by looking for risky skill flows that structural validation cannot see. |
| 4. [SkillSpector](https://github.com/NVIDIA/SkillSpector) by NVIDIA | Provides an independent static quality and security review, useful for comparing findings against the other scanners. |
| 5. [Snyk Agent Scan](https://github.com/snyk/agent-scan) by SNYK | Focuses on agent-skill supply-chain and prompt-risk signals, adding another security perspective alongside Cisco and SkillSpector. |

## Limitations

### Lack of determinism

From the outset, be aware that results from interactions with these `Skills` and agents are not deterministic because of how the models behave, but you can mitigate that with clear goals and validation checkpoints.

### Not all models behave in the same way

Some interactive skills require `Premium` models for interactive use; otherwise they follow a fixed sequence of steps.

### Limits of interactions with models

Models can generate code, but they cannot execute it against your local data. To bridge that gap, some Skills include scripts you run locally.

### Software engineers must remain in the loop

This project supports software engineering work; it does not replace engineering judgment. A software engineer must review, guide, and validate AI-generated decisions, code, and outcomes before they are used.

### Access to corporate data

Use caution when a problem involves corporate databases or other sensitive organizational data. Before granting an AI-assisted workflow access, assess authorization, privacy, data leakage, retention, and unintended modification risks. Apply least-privilege access, human review, validation, and monitoring. See [OWASP GenAI Data Security Risks & Mitigations 2026](https://genai.owasp.org/resource/owasp-genai-data-security-risks-mitigations-2026/), [The EU Artificial Intelligence Act](https://artificialintelligenceact.eu/), and the new [regulation skills](#compliance).

## Contribute

- Follow the [5-minute guide](./documentation/guides/GETTING-STARTED-IN-5-MINUTES.md) and tell us where the experience can improve.
- [Browse the skill inventory](./documentation/guides/INVENTORY-SKILLS-JAVA.md) and propose a missing Java workflow.
- [Open an issue](https://github.com/jabrena/cursor-rules-java/issues) to report a problem or suggest an enhancement.
- Read [CONTRIBUTING.md](./CONTRIBUTING.md) to improve a skill, agent, command, or project guide.
- Star the repository if these workflows help your Java projects.

## Architecture Decision Records (ADR)

- Review the [ADR index](./documentation/adr/README.md) for the complete list.

## Java JEPs from Java 8 onward

Java uses JEPs (JDK Enhancement Proposals) to describe new language and platform features. This repository tracks which JEPs could improve the Skills and guidance here.

- [JEPs list](./documentation/jeps/All-JEPS.md)

## Further resources

Talks, articles, reference links, skill portals, and related projects live in [Project references](./documentation/guides/PROJECT-REFERENCES.md).

Developed by humans with support from [Cursor](https://www.cursor.com/) and [Codex](https://openai.com/codex/), with ❤️ from [Madrid](https://www.google.com/maps/place/Community+of+Madrid,+Madrid/@40.4983324,-6.3162283,8z/data=!3m1!4b1!4m6!3m5!1s0xd41817a40e033b9:0x10340f3be4bc880!8m2!3d40.4167088!4d-3.5812692!16zL20vMGo0eGc?entry=ttu&g_ep=EgoyMDI1MDgxOC4wIKXMDSoASAFQAw%3D%3D)
