## Context

Issue #848 requests regulation support for AI-enabled Java enterprise systems, operational resilience, privacy, cybersecurity, and disclosure obligations. The UK portion names FCA operational resilience sources and ICO UK GDPR guidance. These sources should become UK-specific skills rather than being folded into the EU regulation skills because they use UK authorities, UK regulatory terminology, and UK governance escalation paths.

The existing `801-regulations-eu-ai-act` skill provides the regulation-aware engineering pattern: it is not legal advice, it starts from authoritative references, it translates regulation concerns into engineering controls, and it escalates classification or interpretation decisions to the appropriate governance owners.

## Decisions

### Skill identifiers

- Use `804-regulations-uk-operational-resilience` for UK operational resilience guidance.
- Use `805-regulations-uk-gdpr` for UK GDPR guidance.

These identifiers keep UK regulation skills in the `800` band after the existing `801` EU AI Act skill and the active EU DORA/GDPR plan for `802` and `803`.

### Skill shape

Both skills use the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/<id>-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/<skill-id>.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire and report assets are not required for the first UK regulation addition. The initial deliverable is regulation-aware engineering guidance with concrete review patterns. Questionnaire/report assets can be added later if issue #848 or follow-up issues request formal review reports comparable to `801`.

### UK operational resilience scope

The UK operational resilience skill focuses on engineering controls for firms and services that must identify important business services, set or support impact tolerances, map dependencies, test severe-but-plausible disruption scenarios, prepare communications, manage third-party and technology dependencies, preserve operational evidence, and design recovery paths.

The skill should frame these concerns as Java enterprise engineering controls: service ownership, dependency inventory, SLO/error-budget alignment, resilience testing, failover and backup/recovery evidence, observability, incident response, change control, third-party ICT risk, deployment rollback, and audit-ready evidence. It must recommend escalation to legal, compliance, operational resilience, security, risk, and business-continuity owners for applicability and regulatory interpretation.

### UK GDPR scope

The UK GDPR skill focuses on personal-data processing in Java enterprise systems under UK GDPR and ICO guidance. Guidance should cover privacy and data protection as engineering controls: personal-data inventory, data minimization, lawful-basis handoff, purpose limitation, data-subject rights workflows, retention and deletion, pseudonymization/anonymization, security of processing, processor/controller boundaries, international transfer review, DPIA escalation, breach-response evidence, and privacy-safe logging.

The skill must not provide legal conclusions about lawful basis, controller/processor status, transfer mechanisms, DPIA outcomes, or regulatory interpretation. Those decisions require escalation to legal, privacy, data protection officer, security, compliance, or risk owners.

### Relationship to other regulation skills

The new skills complement rather than replace existing or planned regulation skills:

- Use `801-regulations-eu-ai-act` when the primary concern is AI systems, AI agents, RAG, LLM workflows, tool calling, or model-driven decision support under the EU AI Act.
- Use `802-regulations-dora` when the primary concern is EU digital operational resilience and ICT risk under DORA.
- Use `803-regulations-gdpr` when the primary concern is EU GDPR personal-data processing.
- Use `804-regulations-uk-operational-resilience` when the primary concern is UK operational resilience, important business services, impact tolerances, scenario testing, or FCA/PRA-style resilience evidence.
- Use `805-regulations-uk-gdpr` when the primary concern is UK personal-data processing, ICO guidance, data-subject rights, retention, transfers, breach evidence, or privacy controls.
- Use multiple regulation skills together when the same Java system crosses jurisdiction or concern boundaries.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `804-regulations-uk-operational-resilience/SKILL.md` and `805-regulations-uk-gdpr/SKILL.md`.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, applicability classification, and jurisdiction-specific advice remain outside the skills and must be escalated to legal, compliance, privacy, security, operational resilience, business-continuity, or risk owners.
