## Context

Issue #855 expands the EU regulation skill area beyond the implemented `801`, `802`, and `803` skills and includes Digital Omnibus (2025) as a cross-cutting simplification item. European Commission sources describe the Digital Omnibus as a package of proposed simplification measures for AI, cybersecurity, and data rules, not as a single stable regulation with one settled compliance checklist.

## Decisions

### Skill Identifier

Use `809-regulations-eu-digital-omnibus` for Digital Omnibus simplification-impact guidance.

This identifier keeps pending EU regulation skills in the `804` through `809` range before the active UK/USA plan.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/809-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/809-regulations-eu-digital-omnibus.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire and report assets are not required for the initial change. The first deliverable is regulation-aware engineering guidance with concrete review patterns. Formal questionnaire/report assets can be added later if maintainers request parity with `801`, `802`, and `803` report workflows.

### Digital Omnibus Engineering Scope

The Digital Omnibus skill focuses on tracking and reviewing engineering impacts from EU simplification proposals that may affect AI Act implementation, GDPR/ePrivacy rules, cybersecurity incident reporting, DORA/NIS2 reporting paths, Data Act/data-governance changes, and related compliance evidence.

Guidance should cover source-status checks, affected-regulation mapping, evidence inventory updates, change-control impacts, questionnaire/report-template update candidates, incident-reporting workflow consolidation, data-rights workflow impacts, AI governance timeline changes, compatibility with existing regulation skills, and escalation when proposal-stage language is ambiguous.

The skill must not present proposal-stage content as final law, decide whether simplification changes apply, rewrite conclusions from regulation-specific skills, or reduce escalation requirements. Those decisions require escalation to legal, compliance, privacy, security, risk, resilience, data-governance, or AI governance owners.

### Relationship to Existing Regulation Skills

- Use `801-regulations-eu-ai-act`, `802-regulations-dora`, `803-regulations-gdpr`, `804-regulations-eu-nis2`, and `806-regulations-eu-data-act` for regulation-specific review.
- Use `809-regulations-eu-digital-omnibus` when the primary concern is how Digital Omnibus simplification proposals may affect existing EU digital-rule evidence, timelines, reporting paths, or skill guidance.
- Use Digital Omnibus guidance as an overlay only; it must not replace regulation-specific review.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills`.
- Inspect generated local `809-regulations-eu-digital-omnibus/SKILL.md`.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this planning change. Legal interpretation, legislative-status assessment, and applicability decisions remain outside the skill and must be escalated.
