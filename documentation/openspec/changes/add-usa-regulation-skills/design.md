## Context

Issue #848 requests regulation support for AI-enabled Java enterprise systems, operational resilience, privacy, cybersecurity, and disclosure obligations. The USA portion names GLBA Safeguards, NYDFS Cybersecurity Regulation, and SEC Cybersecurity Disclosure Rules. These sources should become USA-specific skills rather than being folded into EU or UK regulation skills because they use USA authorities, USA regulatory terminology, and USA governance escalation paths.

The existing `801-regulations-eu-ai-act` skill provides the regulation-aware engineering pattern: it is not legal advice, it starts from authoritative references, it translates regulation concerns into engineering controls, and it escalates classification or interpretation decisions to the appropriate governance owners.

## Decisions

### Skill identifiers

- Use `806-regulations-usa-glba-safeguards` for GLBA Safeguards Rule guidance.
- Use `807-regulations-usa-nydfs-cybersecurity` for NYDFS 23 NYCRR 500 cybersecurity guidance.
- Use `808-regulations-usa-sec-cybersecurity-disclosure` for SEC cybersecurity disclosure readiness guidance.

These identifiers keep USA regulation skills in the `800` band after the existing `801` EU AI Act skill, the active EU DORA/GDPR plan for `802` and `803`, and the active UK plan for `804` and `805`.

### Skill shape

All three skills use the existing XML source pattern:

- `skills-generator/src/main/resources/skill-indexes/<id>-skill.xml` defines metadata, title, goal, constraints, triggers, and workflow steps.
- `skills-generator/src/main/resources/skill-references/<skill-id>.xml` provides detailed examples and output guidance.
- `skills-generator/src/main/resources/skills.xml` registers the skill id and reference.

Dedicated questionnaire and report assets are not required for the first USA regulation addition. The initial deliverable is regulation-aware engineering guidance with concrete review patterns. Questionnaire/report assets can be added later if issue #848 or follow-up issues request formal review reports comparable to `801`.

### GLBA Safeguards scope

The GLBA Safeguards skill focuses on engineering controls for Java enterprise systems that handle customer information for financial institutions or service-provider contexts. Guidance should cover information security programs as engineering controls: customer-information inventory, risk assessment evidence, access controls, encryption, secure development, change management, logging and monitoring, vulnerability management, incident response, service-provider oversight, data retention and disposal, and audit-ready security evidence.

The skill must not provide legal conclusions about covered-entity status, customer-information classification, or regulatory applicability. Those decisions require escalation to legal, compliance, privacy, security, risk, or vendor-management owners.

### NYDFS cybersecurity scope

The NYDFS cybersecurity skill focuses on cybersecurity program controls for covered entities and regulated financial services systems. Guidance should cover governance and technical controls as engineering concerns: asset inventory, risk assessment, MFA, identity and access management, least privilege, secure configuration, encryption, vulnerability management, penetration testing support, application security, logging and monitoring, incident response, business continuity and disaster recovery evidence, third-party service provider security, audit trails, and certification evidence.

The skill must not provide legal conclusions about covered-entity status, exemption status, certification obligations, or regulatory interpretation. Those decisions require escalation to legal, compliance, cybersecurity, risk, audit, and executive accountability owners.

### SEC cybersecurity disclosure scope

The SEC cybersecurity disclosure skill focuses on engineering evidence that supports disclosure readiness for public-company cybersecurity risk management, strategy, governance, and material incident response. Guidance should cover evidence and coordination controls: incident classification handoff, severity and materiality escalation paths, governance reporting signals, risk management evidence, audit logs, timeline reconstruction, post-incident remediation tracking, third-party dependency evidence, board/executive reporting inputs, and disclosure-safe documentation.

The skill must not determine materiality, disclosure timing, public filing content, or securities-law obligations. Those decisions require escalation to legal, disclosure committee, compliance, investor relations, security, risk, and executive owners.

### Relationship to other regulation skills

The new skills complement rather than replace existing or planned regulation skills:

- Use `801-regulations-eu-ai-act` when the primary concern is AI systems, AI agents, RAG, LLM workflows, tool calling, or model-driven decision support under the EU AI Act.
- Use `802-regulations-dora` when the primary concern is EU digital operational resilience and ICT risk under DORA.
- Use `803-regulations-gdpr` when the primary concern is EU GDPR personal-data processing.
- Use `804-regulations-uk-operational-resilience` when the primary concern is UK operational resilience, important business services, impact tolerances, scenario testing, or FCA/PRA-style resilience evidence.
- Use `805-regulations-uk-gdpr` when the primary concern is UK personal-data processing, ICO guidance, data-subject rights, retention, transfers, breach evidence, or privacy controls.
- Use `806-regulations-usa-glba-safeguards` when the primary concern is customer-information safeguards, financial institution service-provider security, or GLBA information security evidence.
- Use `807-regulations-usa-nydfs-cybersecurity` when the primary concern is NYDFS cybersecurity program controls, incident response, audit trails, third-party service-provider security, or certification evidence.
- Use `808-regulations-usa-sec-cybersecurity-disclosure` when the primary concern is cybersecurity disclosure readiness, material incident escalation handoff, governance evidence, or public-company risk management reporting support.
- Use multiple regulation skills together when the same Java system crosses jurisdiction, sector, disclosure, AI, privacy, resilience, or cybersecurity boundaries.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills` without refreshing public `skills/`.
- Inspect generated local `806-regulations-usa-glba-safeguards/SKILL.md`, `807-regulations-usa-nydfs-cybersecurity/SKILL.md`, and `808-regulations-usa-sec-cybersecurity-disclosure/SKILL.md`.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for this change. Legal interpretation, applicability classification, materiality decisions, disclosure decisions, and jurisdiction-specific advice remain outside the skills and must be escalated to legal, compliance, privacy, security, disclosure, audit, executive, or risk owners.
