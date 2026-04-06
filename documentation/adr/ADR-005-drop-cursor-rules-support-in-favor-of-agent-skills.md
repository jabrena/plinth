---
status: "accepted"
date: 2026-04-06
decision-makers: Juan Antonio Breña Moral
consulted: N/A
informed: Repository contributors and downstream consumers of generated guidance
---

# Drop support for Generated `.cursor/rules/` in Favor of Agent Skills

## Context and Problem Statement

This repository maintains Java enterprise guidance as **generated artifacts** from XML sources in the unified `skills-generator` module (historically a separate `system-prompts-generator` produced Markdown under `.cursor/rules/`) and, since [ADR-004](./ADR-004-skill-generation.md), **Agent Skills** under `skills/`. Major assistants now converge on the [Agent Skills specification](https://agentskills.io/specification), so keeping **two** parallel generated surfaces (Cursor rules markdown and skills) duplicates delivery, documentation, and mental load without adding distinct capabilities for most consumers.

Before implementing pipeline and layout changes for a **rules-to-skills** migration, we need a single recorded decision that states why generated `.cursor/rules/` are retired, what breaks for dependents, and how the repository’s source-of-truth and outputs are expected to look afterward. That record is the primary deliverable for OpenSpec change **phase-0-decision-compatibility** (`documentation/openspec/changes/phase-0-decision-compatibility/`).

This decision **does not** question XML as the authoritative rule source; it concerns **which generated outputs** we publish and support for end users.

## Decision Drivers

* **Industry direction**: Agent Skills are supported across major coding agents (portable, documented standard).
* **Maintenance cost**: Two markdown outputs from the same semantic content increase drift risk, duplicate CI checks, and complicate contributor docs.
* **Consumer clarity**: One supported “agent guidance” artifact family reduces ambiguity for forks and downstream projects.
* **Alignment with prior choice**: [ADR-004](./ADR-004-skill-generation.md) already committed to skills generation in-repo; this ADR completes the direction by choosing skills as the **primary** supported generated guidance surface.
* **Explicit compatibility**: Breaking-change expectations for anyone who relied only on checked-in `.cursor/rules/` must be documented up front.

## Considered Options

* **Option 1**: **Status quo** — Continue generating and shipping both `.cursor/rules/` and `skills/` from the same XML sources.
* **Option 2**: **Drop generated `.cursor/rules/`** — Keep XML sources and the generation pipeline for skills; stop treating `.cursor/rules/` as a supported published output (remove generation/install of those files from the default workflow as implemented in follow-up work).
* **Option 3**: **Skills-only repository** — Split skills into a separate repository (revisits the trade-off already rejected in [ADR-004](./ADR-004-skill-generation.md)).

## Decision Outcome

Chosen option: **Option 2 — Drop generated `.cursor/rules/` in favor of Agent Skills as the supported generated guidance**, because it matches market direction, reduces duplicate artifacts, and keeps a single maintenance path while preserving XML as the one source of truth ([ADR-001](./ADR-001-generate-cursor-rules-from-xml-files.md)) for *content*.

Implementation details (exact Maven goals, module boundaries, and git tracking of `.cursor/rules/`) are intentionally left to subsequent phases; this ADR fixes **what** we decide and **why**.

### Consequences

* **Good**, because consumers have one primary, portable format (skills) aligned with [ADR-004](./ADR-004-skill-generation.md).
* **Good**, because contributors maintain guidance in one generated output family, reducing duplication and doc sprawl.
* **Good**, because the architectural rationale and trade-offs live in this ADR and the linked OpenSpec change for future readers.
* **Bad**, because it is a **breaking change** for workflows that copy or depend on this repository’s generated `.cursor/rules/` without using skills or running local generation.
* **Bad**, because migration work (pipeline, docs, CI, consumer communication) is required after this decision is accepted.
* **Neutral**, because teams that still want Cursor rules–shaped markdown can generate it from the same XML sources in a fork or custom build if they accept unsupported maintenance.

### Confirmation

Confirmation for this decision is **documentation and stakeholder alignment** for Phase 0. Technical confirmation will follow when:

* The build no longer relies on publishing `.cursor/rules/` as the canonical agent guidance (per implementation tasks outside this ADR).
* `README.md`, contributor guides, and CI reflect skills-first delivery.
* `./mvnw clean verify` and skill validation (`npx skill-check skills`) continue to pass for the chosen pipeline.

## Pros and Cons of the Options

### Option 1: Status quo (both outputs)

* Good, because existing consumers of `.cursor/rules/` keep working without migration.
* Bad, because dual outputs perpetuate duplication and unclear “source of truth” for *delivery*.
* Bad, because long-term cost grows with every rule and framework slice.

### Option 2: Retire generated `.cursor/rules/`

* Good, because one supported generated guidance surface (skills) matches ecosystem direction.
* Good, because XML remains the structured source; only the deprecated output changes.
* Bad, because explicit migration messaging and possible one-time churn for dependents.

### Option 3: Separate skills-only repository

* Good, because it could narrow the clone surface for skills-only users.
* Bad, because it splits community and duplicates infrastructure (already argued against in [ADR-004](./ADR-004-skill-generation.md)).

## More Information

* OpenSpec change (Phase 0): `documentation/openspec/changes/phase-0-decision-compatibility/`
* Prior decisions: [ADR-001](./ADR-001-generate-cursor-rules-from-xml-files.md), [ADR-004](./ADR-004-skill-generation.md)
* Agent Skills specification: https://agentskills.io/specification
