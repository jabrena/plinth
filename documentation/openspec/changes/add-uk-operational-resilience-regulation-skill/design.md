## Context

Issue #848 requests regulation support for AI-enabled Java enterprise systems, operational resilience, privacy, cybersecurity, and disclosure obligations. The original UK OpenSpec change grouped FCA/PRA-style operational resilience with UK GDPR. This split gives UK operational resilience its own implementation path and validation boundary.

The existing `801-regulations-eu-ai-act` skill provides the regulation-aware engineering pattern: it is not legal advice, it starts from authoritative references, it translates regulation concerns into engineering controls, and it escalates classification or interpretation decisions to the appropriate governance owners.

## Decisions

### Skill Identifier

Use `821-regulations-uk-operational-resilience` for UK operational resilience guidance.

This identifier preserves the numbering already planned by the combined UK change and keeps UK regulation skills in the `800` band after the existing EU regulation skills.

### Skill Shape

The skill uses the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/821-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/821-regulations-uk-operational-resilience.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire and report assets are not required for the first UK operational resilience addition. The initial deliverable is regulation-aware engineering guidance with concrete review patterns. Questionnaire/report assets can be added later if issue #848 or follow-up issues request formal review reports comparable to `801`, `802`, or `803`.

### UK Operational Resilience Scope

The UK operational resilience skill focuses on engineering controls for firms and services that must identify important business services, set or support impact tolerances, map dependencies, test severe-but-plausible disruption scenarios, prepare communications, manage third-party and technology dependencies, preserve operational evidence, and design recovery paths.

The skill should frame these concerns as Java enterprise engineering controls: service ownership, dependency inventory, SLO/error-budget alignment, resilience testing, failover and backup/recovery evidence, observability, incident response, change control, third-party ICT risk, deployment rollback, and audit-ready evidence. It must recommend escalation to legal, compliance, operational resilience, security, risk, resilience, and business-continuity owners for applicability and regulatory interpretation.

### Relationship to Other Regulation Skills

- Use `801-regulations-eu-ai-act` when the primary concern is AI systems, AI agents, RAG, LLM workflows, tool calling, or model-driven decision support under the EU AI Act.
- Use `802-regulations-dora` when the primary concern is EU digital operational resilience and ICT risk under DORA.
- Use `821-regulations-uk-operational-resilience` when the primary concern is UK operational resilience, important business services, impact tolerances, scenario testing, FCA/PRA-style resilience evidence, or UK third-party ICT risk concerns.
- Use `822-regulations-uk-gdpr` when the primary concern is UK personal-data processing, ICO guidance, data-subject rights, retention, transfers, breach evidence, or privacy controls.
- Use multiple regulation skills together when the same Java system crosses jurisdiction or concern boundaries.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `821-regulations-uk-operational-resilience/SKILL.md`.
- Check `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`; record that no listed acceptance prompt exists for `821` unless added by this change.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, applicability classification, impact-tolerance approval, and jurisdiction-specific advice remain outside the skill and must be escalated to legal, compliance, security, operational resilience, business-continuity, or risk owners.
