## Context

The repository now has one EU regulation skill, `801-regulations-eu-ai-act`, focused on AI systems and AI agents. Issue #848 extends the regulation area to additional EU sources. The next two EU regulation skills should preserve the same engineering posture: they are not legal advice, they turn regulation themes into review questions and architecture controls, and they require escalation to the right governance owners when classification or interpretation matters.

## Decisions

### Skill identifiers

- Use `802-regulations-dora` for the Digital Operational Resilience Act.
- Use `803-regulations-gdpr` for the General Data Protection Regulation.

These identifiers keep EU regulation skills in the `800` band after `801-regulations-eu-ai-act` and make each regulation discoverable as a standalone skill.

### Skill shape

Both skills use the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/<id>-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/<skill-id>.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire and report assets are not required for the first DORA/GDPR addition. The initial deliverable is regulation-aware engineering guidance with concrete review patterns. Questionnaire/report assets can be added later if issue #848 or follow-up issues request formal review reports comparable to `801`.

### DORA engineering scope

The DORA skill focuses on ICT risk management and digital operational resilience for Java enterprise systems, especially systems supporting financial entities or critical ICT services. Guidance should cover resilience requirements as engineering controls: ICT risk governance, asset and dependency inventory, incident detection and reporting preparation, backup and recovery, business continuity, third-party ICT provider risk, testing, monitoring, logging, change control, and operational evidence.

### GDPR engineering scope

The GDPR skill focuses on personal-data processing in Java enterprise systems. Guidance should cover privacy and data protection as engineering controls: data minimization, lawful-basis documentation handoff, purpose limitation, consent or contract signals where relevant, data-subject rights workflows, retention and deletion, pseudonymization/anonymization, security of processing, processor/controller boundaries, data transfer review, DPIA escalation, breach-response evidence, and privacy-safe logging.

### Relationship to 801

The new skills complement rather than replace `801-regulations-eu-ai-act`:

- Use `801` when the primary concern is AI-system or AI-agent governance under the EU AI Act.
- Use `802` when the primary concern is digital operational resilience and ICT risk in financial or critical ICT contexts.
- Use `803` when the primary concern is personal-data processing, privacy controls, data-subject rights, or data protection evidence.
- Use multiple skills together when an AI-enabled Java system also processes personal data or supports regulated financial operations.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `802-regulations-dora/SKILL.md` and `803-regulations-gdpr/SKILL.md`.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, applicability classification, and jurisdiction-specific advice remain outside the skills and must be escalated to legal, compliance, privacy, security, or risk owners.
