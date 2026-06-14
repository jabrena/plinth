## Context

Issue #855 expands the EU regulation skill area beyond the implemented `801`, `802`, and `803` skills. The Digital Services Act needs separate planning because it focuses on intermediary services, online platforms, content moderation, recommender transparency, advertising transparency, user redress, and systemic-risk evidence.

## Decisions

### Skill Identifier

Use `807-regulations-eu-digital-services-act` for Digital Services Act guidance.

This identifier keeps pending EU regulation skills in the `804` through `809` range before the active UK/USA plan.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/807-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/807-regulations-eu-digital-services-act.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire and report assets are not required for the initial change. The first deliverable is regulation-aware engineering guidance with concrete review patterns. Formal questionnaire/report assets can be added later if maintainers request parity with `801`, `802`, and `803` report workflows.

### Digital Services Act Engineering Scope

The DSA skill focuses on Java systems that support intermediary services, hosting services, online platforms, marketplaces, content moderation, notice-and-action workflows, recommender systems, advertising delivery, trader traceability, complaint handling, or transparency reporting.

Guidance should cover content decision audit logs, moderation workflow state, notice intake and response tracking, recommender and ranking explanation evidence, ad transparency metadata, user controls, complaint and appeal workflows, risk assessment evidence, incident escalation, data access for auditors or researchers where applicable, and privacy-safe observability.

The skill must not decide intermediary or platform classification, very-large-online-platform status, illegal-content determinations, systemic-risk conclusions, or regulatory interpretation. Those decisions require escalation to legal, compliance, trust-and-safety, privacy, security, product, or risk owners.

### Relationship to Existing Regulation Skills

- Use `801-regulations-eu-ai-act` for EU AI systems and AI-agent governance.
- Use `803-regulations-gdpr` for EU personal-data processing and privacy controls.
- Use `806-regulations-eu-data-act` for EU data access and portability concerns.
- Use `807-regulations-eu-digital-services-act` for online-platform, content-moderation, recommender-transparency, ad-transparency, and user-redress concerns.
- Use multiple regulation skills together when the same Java system crosses AI, privacy, platform, data access, cybersecurity, or transparency boundaries.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills`.
- Inspect generated local `807-regulations-eu-digital-services-act/SKILL.md`.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, platform classification, illegal-content determinations, and systemic-risk decisions remain outside the skill and must be escalated.
