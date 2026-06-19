## Context

Issue #855 expands the EU regulation skill area beyond the implemented `801`, `802`, and `803` skills. NIS2 needs separate planning because it focuses on cybersecurity risk management and reporting for essential and important entities rather than AI-system classification, financial ICT resilience, or personal-data processing.

## Decisions

### Skill Identifier

Use `804-regulations-eu-nis2` for NIS2 cybersecurity guidance.

This identifier keeps pending EU regulation skills in the `804` through `809` range before the active UK/USA plan.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/804-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/804-regulations-eu-nis2-chapters-summary.xml` provides the NIS2 Directive chapter, article, and annex summary for engineering review.
- `skills-generator/src/main/resources/skill-references/804-regulations-eu-nis2-engineering-examples.xml` provides Java-focused NIS2 engineering examples and output guidance.
- `skills-generator/src/main/resources/skill-references/assets/reports/804-nis2-engineering-review-report-template.md` provides the engineering review report structure.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and references.

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

### Acceptance Evidence and Example Reports

The NIS2 Gherkin feature mirrors adjacent regulation skills with two delivery modes:

- Pull-request based CI/CD review using `examples/diagrams/deployment/system-example-cicd-pr-model.md`.
- Direct-to-main CI/CD review using `examples/diagrams/deployment/system-example-cicd-model.md`.

Both scenarios use `examples/diagrams/deployment/expected-system-deployment.puml`, `examples/diagrams/deployment/checkout-service-feature-request.md`, and the generated local NIS2 skill. The expected output reports are committed under `examples/regulations/nis2` as reviewable acceptance evidence:

- `NIS2-ENGINEERING-REVIEW-REPORT.md` for the pull-request delivery model.
- `NIS2-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md` for the direct-to-main delivery model.

The reports must stay grounded in the reviewed example files, use the NIS2 chapters summary and engineering examples, frame findings as engineering controls rather than legal advice, and hand off legal applicability, entity classification, incident-reporting duties, cybersecurity risk acceptance, and regulatory interpretation to qualified owners. The direct-to-main report must additionally identify missing pre-merge review, protected-main bypass, and direct-to-main release-policy risks.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills`.
- Inspect generated local `804-regulations-eu-nis2/SKILL.md`.
- Inspect generated local `804-regulations-eu-nis2/references/804-regulations-eu-nis2-chapters-summary.md`.
- Inspect generated local `804-regulations-eu-nis2/references/804-regulations-eu-nis2-engineering-examples.md`.
- Inspect generated local `804-regulations-eu-nis2/assets/reports/804-nis2-engineering-review-report-template.md`.
- Add and review `skills-generator/src/test/resources/gherkin/skills/804-regulations-eu-nis2.feature` with PR-based and direct-to-main scenarios that write reports under `examples/regulations/nis2`.
- Review `examples/regulations/nis2/NIS2-ENGINEERING-REVIEW-REPORT.md` and `examples/regulations/nis2/NIS2-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md` as acceptance execution evidence.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, entity classification, member-state implementation details, and incident-reporting decisions remain outside the skill and must be escalated.
