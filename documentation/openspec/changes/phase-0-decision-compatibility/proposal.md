# Phase 0: Decision and Compatibility Planning

## Problem Statement

Before implementing the rules-to-skills migration, we need a recorded architectural decision that explains why generated `.cursor/rules/` are being retired in favor of agent skills, what that implies for consumers, and how the repository layout and delivery model change. Without that decision, future contributors and downstream projects lack a single source of truth for rationale, trade-offs, and consequences.

Supporting context for this phase still includes confirming breaking-change expectations, inventorying dependents where useful, and aligning on naming and module boundaries—but the **primary deliverable** for this change is the ADR described in [tasks.md](./tasks.md).

## Proposed Solution

Author a new **Architecture Decision Record** under the project ADR directory that documents the rules-to-skills migration: context, decision drivers, options considered, chosen outcome, and consequences for consumers and repository structure.

**Process:** Follow the **`030-architecture-adr-general`** agent skill ([`skills/030-architecture-adr-general/SKILL.md`](../../../../skills/030-architecture-adr-general/SKILL.md)) for the interactive ADR workflow, steps, and constraints (including running Maven validation before generation, as that skill requires).

**ADR location (author new files here):** [`documentation/adr/`](../../../adr/)

Existing ADRs in that folder (for example `ADR-001`–`ADR-004`) illustrate format and numbering; add the next sequential ADR for this migration.

## Success Criteria

- A new ADR in [`documentation/adr/`](../../../adr/) documents the rules-to-skills migration using the **`030-architecture-adr-general`** agent skill: context, decision drivers, considered options, decision outcome, and consequences for consumers and project structure ([tasks.md](./tasks.md)).

## Implementation Details

This phase is **documentation-first**: no generator or pipeline code changes until the ADR is in place. Implementation work for this OpenSpec change is: run the ADR process via the **`030-architecture-adr-general`** agent skill ([`skills/030-architecture-adr-general/SKILL.md`](../../../../skills/030-architecture-adr-general/SKILL.md)), draft, review, and merge the ADR under [`documentation/adr/`](../../../adr/), then tick off items in [tasks.md](./tasks.md).
