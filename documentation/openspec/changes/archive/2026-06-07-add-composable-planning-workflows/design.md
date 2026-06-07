## Context

Skill `041` creates implementation plans. Skill `042` currently requires a `*.plan.md` and treats it as the source for OpenSpec. Issue #806 requires plan-only, spec-first, plan-to-spec, and existing-OpenSpec-to-plan paths.

The work also needs an explicit design-discovery activity before plans or specifications derive technical choices.

## Goals / Non-Goals

**Goals:**

- Add design discovery as a separate skill from ADR recording.
- Let plan and OpenSpec creation start independently.
- Record artifact sources and derivation direction.
- Prevent silent synchronization and source rewriting.
- Split broad specification input only on outcome boundaries.

**Non-Goals:**

- Define slash command Markdown.
- Define agent files.
- Mirror OpenSpec tasks into issue trackers.
- Publish generated public skills.

## Decisions

### Add `034-architecture-design-exploration`

The skill inspects repository context, clarifies ambiguity, compares two or three approaches, evaluates trade-offs, recommends a direction, obtains approval, and identifies ADR candidates.

Alternative considered: merge into ADR creation. Rejected because exploration precedes selection and recording.

### Make `041` input-composable

Planning accepts issue, approved design, ADR, OpenSpec, or combined inputs. The plan records source artifacts and derivation direction.

### Make `042` input-composable

OpenSpec creation accepts issue, plan, approved design, ADR, existing OpenSpec, or combined inputs and does not invent absent requirements.

### Decompose broad inputs inside `042`

Split into multiple changes only when outcomes differ by business value, release, ownership, dependency, risk, approval, rollback, or deployment boundary. A single atomic outcome can update several capabilities.

### Assign authority by concern

- Issue/story: problem, value, scope, acceptance criteria.
- ADR: decisions and consequences.
- OpenSpec specification: requirements.
- Plan: technical delivery strategy.
- OpenSpec tasks: execution tracking only when selected.

Derived changes never rewrite sources automatically. Conflicts require review and an explicit user decision.

## Risks / Trade-offs

- [Risk] Flexible inputs produce inconsistent output. -> Require source summary, derivation metadata, and validation.
- [Risk] Multiple changes become fragmented. -> Require a user-approved change map.
- [Risk] New XML identifier conflicts. -> Confirm identifier and approval before creation.

## Migration Plan

1. Confirm XML approval and identifier.
2. Add design-discovery skill.
3. Refactor `041`.
4. Refactor `042`.
5. Update `skills.xml`.
6. Validate XML, generator tests, and local generated skills.

## Open Questions

- Confirm `034` as the final skill identifier.
