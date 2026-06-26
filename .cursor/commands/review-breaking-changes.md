# review-breaking-changes

## Purpose

Review an approved plan or OpenSpec artifact for potential breaking changes before implementation or release work proceeds.

## Usage

```text
/review-breaking-changes <plan|openspec-change|spec> [scope] [constraints]
```

## Accepted Inputs

- Approved implementation plan (`*.plan.md`)
- OpenSpec change directory containing proposal, design, specs, or tasks
- OpenSpec specification file
- Optional scope constraints such as command, skill, generated output, public API, data contract, documentation, or release boundary

## Owning Agent

`@robot-tech-lead`

## Associated Capabilities

- Compatibility and migration-risk review
- Public contract and generated-output impact analysis
- Command, agent, skill, documentation, and OpenSpec traceability review
- Validation planning for release or implementation readiness

## Workflow

1. Load the selected plan, OpenSpec change, or spec and identify the authoritative scope.
2. Inventory affected contracts, generated outputs, commands, skills, agents, documentation, APIs, schemas, configuration, and release artifacts mentioned by the source.
3. Classify each potential break as `BREAKING`, `POTENTIALLY BREAKING`, `NON-BREAKING`, or `UNKNOWN`.
4. For each breaking or uncertain item, explain the compatibility risk, affected consumers, migration need, and evidence from the source artifact.
5. Identify missing validation, release-note, migration-guide, deprecation, or fallback work.
6. Recommend follow-up changes, tests, OpenSpec updates, or `/review-alignment` when artifact conflicts block a reliable conclusion.
7. Report readiness as `READY`, `READY WITH BREAKING-CHANGE WARNINGS`, or `NOT READY`.

## Output

- Reviewed artifact and scope
- Compatibility summary
- Severity-ranked breaking-change findings
- Affected contracts, generated outputs, commands, skills, documentation, and consumers
- Required migration, release-note, fallback, or validation work
- Open questions and recommended follow-up
- Readiness result

## Safeguards

- Keep the review read-only.
- Do not modify plans, specs, source files, generated outputs, or issue descriptions without explicit follow-up approval.
- Do not infer breaking changes from unrelated repository files when they are not connected to the reviewed artifact.
- Mark uncertain impact as `UNKNOWN` and request more evidence instead of guessing.
- Preserve the source artifact's scope; do not add requirements or implementation tasks beyond compatibility review.
