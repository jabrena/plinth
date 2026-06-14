## Context

Issue #848 requests regulation support for AI-enabled Java enterprise systems, operational resilience, privacy, cybersecurity, and disclosure obligations. The original UK OpenSpec change grouped UK GDPR with FCA/PRA-style operational resilience. This split gives UK GDPR its own implementation path and validation boundary.

The existing `801-regulations-eu-ai-act` skill provides the regulation-aware engineering pattern: it is not legal advice, it starts from authoritative references, it translates regulation concerns into engineering controls, and it escalates classification or interpretation decisions to the appropriate governance owners.

## Decisions

### Skill Identifier

Use `822-regulations-uk-gdpr` for UK GDPR guidance.

This identifier preserves the numbering already planned by the combined UK change and keeps UK regulation skills in the `800` band after the existing EU regulation skills.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/822-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/822-regulations-uk-gdpr.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire and report assets are not required for the first UK GDPR addition. The initial deliverable is regulation-aware engineering guidance with concrete review patterns. Questionnaire/report assets can be added later if issue #848 or follow-up issues request formal review reports comparable to `801`, `802`, or `803`.

### UK GDPR Scope

The UK GDPR skill focuses on personal-data processing in Java enterprise systems under UK GDPR and ICO guidance. Guidance should cover privacy and data protection as engineering controls: personal-data inventory, data minimization, lawful-basis handoff, purpose limitation, data-subject rights workflows, retention and deletion, pseudonymization/anonymization, security of processing, processor/controller boundaries, international transfer review, DPIA escalation, breach-response evidence, and privacy-safe logging.

The skill must not provide legal conclusions about lawful basis, controller/processor status, transfer mechanisms, DPIA outcomes, or regulatory interpretation. Those decisions require escalation to legal, privacy, data protection officer, security, compliance, or risk owners.

### Relationship to Other Regulation Skills

- Use `801-regulations-eu-ai-act` when the primary concern is AI systems, AI agents, RAG, LLM workflows, tool calling, or model-driven decision support under the EU AI Act.
- Use `803-regulations-gdpr` when the primary concern is EU GDPR personal-data processing.
- Use `821-regulations-uk-operational-resilience` when the primary concern is UK operational resilience, important business services, impact tolerances, scenario testing, or FCA/PRA-style resilience evidence.
- Use `822-regulations-uk-gdpr` when the primary concern is UK personal-data processing, ICO guidance, data-subject rights, retention, transfers, breach evidence, or privacy controls.
- Use multiple regulation skills together when the same Java system crosses jurisdiction or concern boundaries.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `822-regulations-uk-gdpr/SKILL.md`.
- Check `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`; record that no listed acceptance prompt exists for `822` unless added by this change.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, lawful-basis decisions, controller/processor role, transfer mechanism decisions, DPIA outcomes, and jurisdiction-specific advice remain outside the skill and must be escalated to legal, privacy, data protection officer, compliance, security, or risk owners.
