## Why

GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848) requests regulation support for AI-related Java enterprise work across multiple jurisdictions. The EU portion is already planned separately in `add-eu-regulation-skills`; this change focuses only on the UK sources named in the issue.

Java enterprise teams building AI systems, AI agents, regulated financial workflows, and privacy-sensitive services need UK-specific guidance because FCA operational resilience expectations and UK GDPR guidance use different source authorities, terminology, and governance owners than the EU regulation set.

## What Changes

- Add `804-regulations-uk-operational-resilience` for engineering review of UK operational resilience concerns in Java enterprise systems.
- Add `805-regulations-uk-gdpr` for engineering review of UK GDPR concerns in Java enterprise systems.
- Model both skills after the regulation skill pattern: regulation-aware engineering guidance, "not legal advice" constraints, source-first review workflow, explicit escalation to legal/compliance/privacy/security/risk/resilience owners, and concrete architecture controls.
- Register both skills in the skill inventory so local skill generation emits `.agents/skills/804-regulations-uk-operational-resilience` and `.agents/skills/805-regulations-uk-gdpr`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `uk-regulation-skill-references`: Adds UK regulation skill references for operational resilience and UK GDPR.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#848](https://github.com/jabrena/cursor-rules-java/issues/848).
- Adjacent OpenSpec context: `documentation/openspec/changes/add-eu-regulation-skills`.
- Existing implementation model: `801-regulations-eu-ai-act`.
- External reference: [FCA operational resilience](https://www.fca.org.uk/firms/operational-resilience).
- External reference: [FCA CP19/32 Building operational resilience](https://www.fca.org.uk/publication/consultation/cp19-32.pdf).
- External reference: [Bank of England/PRA CP on impact tolerances for important business services](https://www.bankofengland.co.uk/-/media/boe/files/prudential-regulation/consultation-paper/2019/building-operational-resilience-impact-tolerances-for-important-business-services.pdf).
- External reference: [ICO UK GDPR guidance and resources](https://ico.org.uk/for-organisations/uk-gdpr-guidance-and-resources/).
- Derivation direction: issue #848 plus official UK references plus the existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because it delivers one atomic outcome: UK regulation skills in the same `800` regulations band. UK operational resilience and UK GDPR are separate skills, but they share the same jurisdiction, source issue, generator registration path, validation workflow, and user-facing regulatory-support theme.

The change does not require splitting by regulation because no independent deployment, runtime code path, website publication, or public release output is requested. The active EU change remains separate because EU DORA/GDPR references use different source authorities. Future USA regulation support can be a separate change because it has different source authorities and likely review owners.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
