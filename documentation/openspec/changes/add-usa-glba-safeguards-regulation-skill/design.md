## Context

Issue #848 requests regulation support for AI-enabled Java enterprise systems, operational resilience, privacy, cybersecurity, and disclosure obligations. The original USA OpenSpec change grouped GLBA Safeguards with NYDFS cybersecurity and SEC cybersecurity disclosure readiness. This split gives GLBA Safeguards its own implementation path and validation boundary.

The existing `801-regulations-eu-ai-act` skill provides the regulation-aware engineering pattern: it is not legal advice, it starts from authoritative references, it translates regulation concerns into engineering controls, and it escalates classification or interpretation decisions to the appropriate governance owners.

## Decisions

### Skill Identifier

Use `831-regulations-usa-glba-safeguards` for GLBA Safeguards Rule guidance.

This identifier preserves the numbering already planned by the combined USA change and keeps USA regulation skills in the `800` band after the EU and UK regulation skills.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/831-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/831-regulations-usa-glba-safeguards.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire and report assets are not required for the first GLBA Safeguards addition. The initial deliverable is regulation-aware engineering guidance with concrete review patterns. Questionnaire/report assets can be added later if issue #848 or follow-up issues request formal review reports comparable to `801`, `802`, or `803`.

### GLBA Safeguards Scope

The GLBA Safeguards skill focuses on engineering controls for Java enterprise systems that handle customer information for financial institutions or service-provider contexts. Guidance should cover information security programs as engineering controls: customer-information inventory, risk assessment evidence, access controls, encryption, secure development, change management, logging and monitoring, vulnerability management, incident response, service-provider oversight, data retention and disposal, and audit-ready security evidence.

The skill must not provide legal conclusions about covered-entity status, customer-information classification, service-provider obligations, or regulatory applicability. Those decisions require escalation to legal, compliance, privacy, security, risk, or vendor-management owners.

### Relationship to Other Regulation Skills

- Use `803-regulations-gdpr` or `822-regulations-uk-gdpr` when the primary concern is EU or UK personal-data processing and privacy controls.
- Use `831-regulations-usa-glba-safeguards` when the primary concern is customer-information safeguards, GLBA information security programs, financial institution service-provider security, or safeguards evidence.
- Use `832-regulations-usa-nydfs-cybersecurity` when the primary concern is NYDFS cybersecurity program controls, incident response, audit trails, third-party service-provider security, or certification evidence.
- Use multiple regulation skills together when the same Java system crosses jurisdiction, privacy, cybersecurity, sector, disclosure, AI, or resilience boundaries.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `831-regulations-usa-glba-safeguards/SKILL.md`.
- Check `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`; record that no listed acceptance prompt exists for `831` unless added by this change.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, covered-entity status, customer-information classification, and jurisdiction-specific advice remain outside the skill and must be escalated to legal, compliance, privacy, security, vendor-management, or risk owners.
