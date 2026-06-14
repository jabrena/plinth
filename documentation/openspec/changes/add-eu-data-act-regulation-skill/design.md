## Context

Issue #855 expands the EU regulation skill area beyond the implemented `801`, `802`, and `803` skills. The Data Act needs separate planning because it focuses on data access, portability, interoperability, cloud switching, and non-personal data controls rather than AI governance, privacy law, operational resilience, or cybersecurity programs.

## Decisions

### Skill Identifier

Use `806-regulations-eu-data-act` for Data Act guidance.

This identifier keeps pending EU regulation skills in the `804` through `809` range before the active UK/USA plan.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/806-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/806-regulations-eu-data-act.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire and report assets are not required for the initial change. The first deliverable is regulation-aware engineering guidance with concrete review patterns. Formal questionnaire/report assets can be added later if maintainers request parity with `801`, `802`, and `803` report workflows.

### Data Act Engineering Scope

The Data Act skill focuses on Java systems that expose, exchange, store, process, export, or port data across users, businesses, connected products, cloud providers, APIs, event streams, or AI/data-platform workflows.

Guidance should cover data inventory, access authorization, portability APIs, export formats, interoperability, metadata, audit logs, cloud-switching support, non-personal data safeguards, trade-secret or sensitive-data handoff, data-sharing request workflows, contract/evidence handoff, and operational controls for data access requests.

The skill must not decide data-holder status, user entitlement, contractual fairness, trade-secret disclosure boundaries, international access restrictions, or regulatory interpretation. Those decisions require escalation to legal, compliance, privacy, data governance, security, product, or risk owners.

### Relationship to Existing Regulation Skills

- Use `801-regulations-eu-ai-act` for EU AI systems and AI-agent governance.
- Use `802-regulations-dora` for EU financial ICT resilience and critical ICT provider concerns.
- Use `803-regulations-gdpr` for EU personal-data processing and privacy controls.
- Use `806-regulations-eu-data-act` for EU data access, sharing, portability, interoperability, and cloud-switching concerns.
- Use multiple regulation skills together when the same Java system crosses AI, personal-data, non-personal data, platform, cloud, cybersecurity, or resilience boundaries.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills`.
- Inspect generated local `806-regulations-eu-data-act/SKILL.md`.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, data-holder classification, user-entitlement decisions, and contract interpretation remain outside the skill and must be escalated.
