## Context

Issue #855 expands the EU regulation skill area beyond the implemented `801`, `802`, and `803` skills. The Cyber Resilience Act needs separate planning because it focuses on products with digital elements, secure-by-design obligations, vulnerability handling, and product security evidence.

## Decisions

### Skill Identifier

Use `805-regulations-eu-cyber-resilience-act` for Cyber Resilience Act guidance.

This identifier keeps pending EU regulation skills in the `804` through `809` range before the active UK/USA plan.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/805-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/805-regulations-eu-cyber-resilience-act.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire and report assets are not required for the initial change. The first deliverable is regulation-aware engineering guidance with concrete review patterns. Formal questionnaire/report assets can be added later if maintainers request parity with `801`, `802`, and `803` report workflows.

### Cyber Resilience Act Engineering Scope

The CRA skill focuses on Java products, services, libraries, agents, plugins, connected components, or platform modules that may qualify as products with digital elements or product-adjacent software supply-chain components.

Guidance should cover secure-by-design controls, threat modeling, secure defaults, vulnerability management, coordinated disclosure, security update mechanisms, dependency and SBOM evidence, cryptography, authentication and authorization, logging without sensitive data exposure, product security documentation, end-of-support signaling, and release readiness.

The skill must not decide product classification, conformity assessment obligations, manufacturer/importer/distributor role, CE marking implications, or regulatory interpretation. Those decisions require escalation to legal, compliance, product, security, risk, or executive accountability owners.

### Relationship to Existing Regulation Skills

- Use `801-regulations-eu-ai-act` for EU AI systems and AI-agent governance.
- Use `802-regulations-dora` for EU financial ICT resilience and critical ICT provider concerns.
- Use `803-regulations-gdpr` for EU personal-data processing and privacy controls.
- Use `804-regulations-eu-nis2` for cybersecurity risk-management and critical-sector operational security concerns.
- Use `805-regulations-eu-cyber-resilience-act` for secure-by-design and vulnerability-handling concerns for products with digital elements.
- Use multiple regulation skills together when the same Java system crosses AI, privacy, cybersecurity, product-security, or resilience boundaries.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills`.
- Inspect generated local `805-regulations-eu-cyber-resilience-act/SKILL.md`.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, product classification, role classification, and conformity-assessment decisions remain outside the skill and must be escalated.
