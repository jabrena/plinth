## Why

GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848) requests regulation support for AI-related Java enterprise work across multiple jurisdictions. The previous combined UK planning grouped UK operational resilience and UK GDPR together; this change splits UK GDPR into its own reviewable regulation change.

Java enterprise teams building AI systems, AI agents, privacy-sensitive workflows, data platforms, and customer-facing services need UK GDPR guidance because ICO guidance and UK data-protection governance use UK authorities, terminology, transfer considerations, and escalation paths that differ from EU GDPR.

## What Changes

- Add `822-regulations-uk-gdpr` for engineering review of UK GDPR concerns in Java enterprise systems.
- Model the skill after the regulation skill pattern: regulation-aware engineering guidance, "not legal advice" constraints, source-first review workflow, explicit escalation to legal/privacy/data-protection-officer/compliance/security/risk owners, and concrete architecture controls.
- Register the skill in the skill inventory so local skill generation emits `.agents/skills/822-regulations-uk-gdpr`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `uk-gdpr-regulation-skill-reference`: Adds UK GDPR regulation skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848).
- Split source artifact: previous combined UK regulation planning.
- Existing implementation model: `801-regulations-eu-ai-act`.
- Adjacent EU privacy pattern: `803-regulations-gdpr`.
- External reference: [ICO UK GDPR guidance and resources](https://ico.org.uk/for-organisations/uk-gdpr-guidance-and-resources/).
- Derivation direction: issue #848 plus official ICO UK GDPR reference plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for UK GDPR only because the user requested the combined UK change be split per regulation. UK GDPR has its own source authority, privacy scope, governance owners, and implementation acceptance boundary.

The former combined UK regulation planning is replaced by this change and `add-uk-operational-resilience-regulation-skill`.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
