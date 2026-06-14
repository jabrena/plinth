## Context

Issue #855 expands the EU regulation skill area beyond the implemented `801`, `802`, and `803` skills. NIS2 needs separate planning because it focuses on cybersecurity risk management and reporting for essential and important entities rather than AI-system classification, financial ICT resilience, or personal-data processing.

## Decisions

### Skill Identifier

Use `804-regulations-eu-nis2` for NIS2 cybersecurity guidance.

This identifier keeps pending EU regulation skills in the `804` through `809` range before the active UK/USA plan.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/804-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/804-regulations-eu-nis2.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skill-references/assets/reports/804-nis2-engineering-review-report-template.md` provides the engineering review report structure.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire assets are not required for the initial change. The skill must still generate a structured engineering review report, so a report template is required and must be packaged as a local skill asset.

### NIS2 Engineering Scope

The NIS2 skill focuses on cybersecurity risk management for Java enterprise systems that may support essential or important entities, critical-sector services, managed service providers, supply-chain dependencies, or incident-reporting obligations.

Guidance should cover engineering controls for asset and service inventory, dependency mapping, secure configuration, vulnerability handling, logging and monitoring, incident detection and escalation, backup and recovery, business continuity evidence, supply-chain risk, access control, cryptography, secure development, and change control.

The skill must not decide entity classification, member-state applicability, reporting obligations, or regulatory interpretation. Those decisions require escalation to legal, compliance, security, risk, resilience, business-continuity, or executive accountability owners.

### Relationship to Existing Regulation Skills

- Use `801-regulations-eu-ai-act` for EU AI systems and AI-agent governance.
- Use `802-regulations-dora` for EU financial ICT resilience and critical ICT provider concerns.
- Use `803-regulations-gdpr` for EU personal-data processing and privacy controls.
- Use `804-regulations-eu-nis2` for EU cybersecurity risk-management and critical-sector operational security concerns.
- Use multiple regulation skills together when the same Java system crosses AI, privacy, resilience, cybersecurity, or sector boundaries.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills`.
- Inspect generated local `804-regulations-eu-nis2/SKILL.md`.
- Inspect generated local `804-regulations-eu-nis2/assets/reports/804-nis2-engineering-review-report-template.md`.
- Add and review `skills-generator/src/test/resources/gherkin/804-regulations-eu-nis2.feature` with PR-based and direct-to-main scenarios that write reports under `examples/regulations/nis2`.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, entity classification, member-state implementation details, and incident-reporting decisions remain outside the skill and must be escalated.
