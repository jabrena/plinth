## Why

GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848) requests regulation support for AI-related Java enterprise work across multiple jurisdictions. The previous combined UK planning grouped UK operational resilience and UK GDPR together; this change splits UK operational resilience into its own reviewable regulation change.

Java enterprise teams building AI systems, AI agents, regulated financial workflows, critical business services, and operationally important platform capabilities need UK-specific operational resilience guidance because FCA/PRA-style expectations use UK authorities, terminology, impact tolerance concepts, and governance owners that differ from EU DORA and privacy-focused regulations.

## What Changes

- Add `821-regulations-uk-operational-resilience` for engineering review of UK operational resilience concerns in Java enterprise systems.
- Model the skill after the regulation skill pattern: regulation-aware engineering guidance, "not legal advice" constraints, source-first review workflow, explicit escalation to legal/compliance/security/risk/resilience/business-continuity owners, and concrete architecture controls.
- Register the skill in the skill inventory so local skill generation emits `.agents/skills/821-regulations-uk-operational-resilience`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `uk-operational-resilience-regulation-skill-reference`: Adds UK operational resilience regulation skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848).
- Split source artifact: previous combined UK regulation planning.
- Existing implementation model: `801-regulations-eu-ai-act`.
- Adjacent EU resilience pattern: `802-regulations-dora`.
- External reference: [FCA operational resilience](https://www.fca.org.uk/firms/operational-resilience).
- External reference: [FCA CP19/32 Building operational resilience](https://www.fca.org.uk/publication/consultation/cp19-32.pdf).
- External reference: [Bank of England/PRA CP on impact tolerances for important business services](https://www.bankofengland.co.uk/-/media/boe/files/prudential-regulation/consultation-paper/2019/building-operational-resilience-impact-tolerances-for-important-business-services.pdf).
- Derivation direction: issue #848 plus official UK operational resilience references plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for UK operational resilience only because the user requested the combined UK change be split per regulation. UK operational resilience has its own source authorities, resilience scope, governance owners, and implementation acceptance boundary.

The former combined UK regulation planning is replaced by this change and `add-uk-gdpr-regulation-skill`.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
