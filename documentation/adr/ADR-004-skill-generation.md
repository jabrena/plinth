---
status: "implemented"
date: 2026-03-01
decision-makers: Juan Antonio Breña Moral
consulted: N/A
informed: N/A
---

# Skill Generation in the Existing Repository

## Context and Problem Statement

The project has a stable system prompts (cursor rules) generation pipeline based on an XML → XInclude → XSLT → Markdown workflow. The AI tooling market has converged on an open standard called Agent Skills (https://agentskills.io/specification), adopted by all major AI platforms: Claude Code, Cursor, and OpenAI Codex. This standard represents the natural evolution of system prompts, and the project needs a way to generate Agent Skills alongside the existing cursor rules.

## Decision Drivers

* The Agent Skills standard is supported by all major AI coding assistants: Claude Code, Cursor, and OpenAI Codex
* Maintaining a single source of truth for Java AI guidance reduces fragmentation
* The existing community trust is concentrated in this repository
* Avoiding code duplication across multiple repositories reduces long-term maintenance cost

## Considered Options

* Option 1: Create a new dedicated repository for skills generation
* Option 2: Reuse the existing repository and extend it to support skills generation

## Decision Outcome

Chosen option: "Option 2: Reuse the existing repository and extend it to support skills generation", because maintaining two repositories is expensive, the community already trusts this repository, and skills generation can be treated as a natural evolution of the existing cursor rules pipeline.

### Consequences

* Good, because it enables easy maintenance through the current dual automated workflow (cursor rules + skills)
* Good, because it avoids code duplication between two separate repositories
* Good, because it preserves community continuity — contributors and users stay in one place
* Bad, because it increases the complexity of the generation pipeline

### Confirmation

The implementation will be validated through automated tests and CI pipelines that verify:
- The skills generator module produces valid `SKILL.md` files conforming to the Agent Skills specification
- The `npx skill-check skills` command passes for all generated skills
- Existing cursor rules generation continues to pass all existing tests
- The Maven multi-module build (`./mvnw clean verify`) succeeds end-to-end

## Pros and Cons of the Options

### Option 1: Create a new dedicated repository

* Good, because it provides a clean, isolated development environment
* Bad, because it duplicates XML source content and generation infrastructure
* Bad, because it splits the community across two repositories, reducing contribution momentum

### Option 2: Reuse the existing repository and extend it

* Good, because it maximises reusability of existing XML sources, XSLT stylesheets, and Maven build tooling
* Good, because it treats skills as a natural evolution of cursor rules within the same project lifecycle
* Neutral, because the existing `skills-generator` Maven module already exists as a foundation
* Bad, because it makes the generation pipeline more complex to maintain and understand

## More Information

* Agent Skills open specification: https://agentskills.io/specification
* Claude Code Skills documentation: https://code.claude.com/docs/en/skills
* Cursor Agent Skills documentation: https://cursor.com/docs/context/skills
* OpenAI Codex Skills documentation: https://developers.openai.com/codex/skills/
