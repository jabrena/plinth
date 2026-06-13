## Why

GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848) requests regulation support for AI-related Java enterprise work. The first regulation skill in this area, `801-regulations-eu-ai-act`, already translates EU AI Act concerns into engineering controls for Java AI systems and agents, but the EU regulation set is incomplete without operational resilience and personal-data protection guidance.

Java enterprise teams building AI systems, AI agents, RAG workflows, integrations, and regulated business applications need separate guidance for DORA and GDPR because those regulations create different engineering review questions: ICT risk and operational resilience for DORA, and lawful personal-data processing and data-subject rights for GDPR.

## What Changes

- Add `802-regulations-dora` for engineering review of Digital Operational Resilience Act concerns in Java enterprise systems.
- Add `803-regulations-gdpr` for engineering review of General Data Protection Regulation concerns in Java enterprise systems.
- Model both skills after the `801-regulations-eu-ai-act` format and structure: skill index metadata, title, goal, constraints, triggers, step-by-step workflow, detailed reference role/goal/examples/output-format/safeguards, "not legal advice" constraints, source-first review workflow, explicit escalation to legal/compliance/privacy/security/risk owners, and concrete architecture controls.
- Register both skills in the skill inventory so local skill generation emits `.agents/skills/802-regulations-dora` and `.agents/skills/803-regulations-gdpr`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `eu-regulation-skill-references`: Adds EU regulation skill references for DORA and GDPR, complementing the existing EU AI Act skill.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848), milestone `v0.16.0`.
- Existing implementation model and structural baseline: `801-regulations-eu-ai-act`.
- External reference: [DORA Regulation (EU) 2022/2554](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2554).
- External reference: [GDPR Regulation (EU) 2016/679](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32016R0679).
- Derivation direction: issue #848 plus official EUR-Lex references plus the `801` skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because it delivers one atomic outcome: two additional EU regulation skills in the same `800` regulations band. DORA and GDPR are separate skills, but they share the same release milestone, source issue, generator registration path, validation workflow, and user-facing regulatory-support theme.

The change does not require splitting by regulation because no independent deployment, runtime code path, website publication, or public release output is requested. Future UK or USA regulation support can be separate changes because those jurisdictions have different source authorities, terminology, and likely review owners.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
