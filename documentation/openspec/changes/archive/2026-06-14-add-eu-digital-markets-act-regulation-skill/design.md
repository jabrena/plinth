## Context

Issue #855 expands the EU regulation skill area beyond the implemented `801`, `802`, and `803` skills. The Digital Markets Act needs separate planning because it focuses on gatekeeper platforms, core platform services, interoperability, data access, anti-circumvention evidence, and business-user controls.

## Decisions

### Skill Identifier

Use `808-regulations-eu-digital-markets-act` for Digital Markets Act guidance.

This identifier keeps pending EU regulation skills in the `804` through `809` range before the active UK/USA plan.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/808-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/808-regulations-eu-digital-markets-act.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire and report assets are not required for the initial change. The first deliverable is regulation-aware engineering guidance with concrete review patterns. Formal questionnaire/report assets can be added later if maintainers request parity with `801`, `802`, and `803` report workflows.

### Digital Markets Act Engineering Scope

The DMA skill focuses on Java systems that may support gatekeeper core platform services, app stores, marketplaces, advertising systems, ranking systems, identity services, messaging interoperability, business-user data access, consent-dependent data combination, or platform telemetry.

Guidance should cover interoperability interfaces, data access APIs, consent and preference evidence, ranking and self-preferencing audit signals, business-user export workflows, anti-circumvention guardrails, access control, observability, change control, documentation, and compliance evidence handoff.

The skill must not decide gatekeeper designation, core-platform-service classification, obligation applicability, self-preferencing determinations, or regulatory interpretation. Those decisions require escalation to legal, compliance, platform governance, product, privacy, security, or risk owners.

### Relationship to Existing Regulation Skills

- Use `801-regulations-eu-ai-act` for EU AI systems and AI-agent governance.
- Use `803-regulations-gdpr` for EU personal-data processing and privacy controls.
- Use `806-regulations-eu-data-act` for EU data access and portability concerns.
- Use `807-regulations-eu-digital-services-act` for online-platform, moderation, recommender, ad transparency, and user-redress concerns.
- Use `808-regulations-eu-digital-markets-act` for gatekeeper-platform, interoperability, business-user access, self-preferencing, and platform-control concerns.
- Use multiple regulation skills together when the same Java system crosses AI, privacy, platform, marketplace, advertising, data access, or interoperability boundaries.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills`.
- Inspect generated local `808-regulations-eu-digital-markets-act/SKILL.md`.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, gatekeeper designation, core-platform-service classification, and obligation applicability remain outside the skill and must be escalated.
