## Context

Issue #848 requests regulation support for AI-enabled Java enterprise systems, operational resilience, privacy, cybersecurity, and disclosure obligations. The original USA OpenSpec change grouped NYDFS cybersecurity with GLBA Safeguards and SEC cybersecurity disclosure readiness. This split gives NYDFS cybersecurity its own implementation path and validation boundary.

The existing `801-regulations-eu-ai-act` skill provides the regulation-aware engineering pattern: it is not legal advice, it starts from authoritative references, it translates regulation concerns into engineering controls, and it escalates classification or interpretation decisions to the appropriate governance owners.

## Decisions

### Skill Identifier

Use `832-regulations-usa-nydfs-cybersecurity` for NYDFS 23 NYCRR 500 cybersecurity guidance.

This identifier preserves the numbering already planned by the combined USA change and keeps USA regulation skills in the `800` band after the EU and UK regulation skills.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/832-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/832-regulations-usa-nydfs-cybersecurity.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire and report assets are not required for the first NYDFS cybersecurity addition. The initial deliverable is regulation-aware engineering guidance with concrete review patterns. Questionnaire/report assets can be added later if issue #848 or follow-up issues request formal review reports comparable to `801`, `802`, or `803`.

### NYDFS Cybersecurity Scope

The NYDFS cybersecurity skill focuses on cybersecurity program controls for covered entities and regulated financial services systems. Guidance should cover governance and technical controls as engineering concerns: asset inventory, risk assessment, MFA, identity and access management, least privilege, secure configuration, encryption, vulnerability management, penetration testing support, application security, logging and monitoring, incident response, business continuity and disaster recovery evidence, third-party service provider security, audit trails, and certification evidence.

The skill must not provide legal conclusions about covered-entity status, exemption status, certification obligations, or regulatory interpretation. Those decisions require escalation to legal, compliance, cybersecurity, risk, audit, and executive accountability owners.

### Relationship to Other Regulation Skills

- Use `831-regulations-usa-glba-safeguards` when the primary concern is customer-information safeguards, financial institution service-provider security, or GLBA information security evidence.
- Use `832-regulations-usa-nydfs-cybersecurity` when the primary concern is NYDFS cybersecurity program controls, incident response, audit trails, third-party service-provider security, or certification evidence.
- Use `833-regulations-usa-sec-cybersecurity-disclosure` when the primary concern is cybersecurity disclosure readiness, material incident escalation handoff, governance evidence, or public-company risk management reporting support.
- Use multiple regulation skills together when the same Java system crosses jurisdiction, sector, disclosure, AI, privacy, resilience, or cybersecurity boundaries.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `832-regulations-usa-nydfs-cybersecurity/SKILL.md`.
- Check `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`; record that no listed acceptance prompt exists for `832` unless added by this change.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, covered-entity status, exemption status, certification obligations, and jurisdiction-specific advice remain outside the skill and must be escalated to legal, compliance, cybersecurity, audit, executive accountability, or risk owners.
