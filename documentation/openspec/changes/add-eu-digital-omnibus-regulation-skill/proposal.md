## Why

GitHub issue [#855](https://github.com/jabrena/cursor-rules-java/issues/855) identifies the Digital Omnibus (2025) as a pending cross-cutting part of the GenAI Regulatory Stack (EU). The European Commission describes the digital omnibus as a simplification package for AI, cybersecurity, and data rules, so it should be planned separately from single-regulation skills and must avoid presenting proposal-stage changes as final legal obligations.

Java enterprise teams using the existing EU regulation skills need guidance for tracking Digital Omnibus impacts on AI Act, GDPR, NIS2, DORA, Data Act, and related digital-rule evidence without silently rewriting regulation-specific conclusions or lowering escalation requirements.

## What Changes

- Add `809-regulations-eu-digital-omnibus` for engineering review of Digital Omnibus simplification impacts across EU digital regulation skills.
- Frame the skill as a cross-cutting regulatory-change tracker and engineering evidence review, not as a finalized single-regulation compliance checklist.
- Model the skill after `801`, `802`, and `803`: source-first review, "not legal advice" constraints, scope classification, evidence-driven findings, explicit escalation to legal/compliance/privacy/security/risk owners, and concrete Java architecture controls.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/809-regulations-eu-digital-omnibus`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `eu-digital-omnibus-regulation-skill-reference`: Adds Digital Omnibus skill guidance for tracking EU digital simplification impacts across engineering controls.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#855](https://github.com/jabrena/cursor-rules-java/issues/855), comment listing the GenAI Regulatory Stack (EU).
- Existing implementation models: `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr`.
- External reference: [European Commission, simpler digital rules to help EU businesses grow](https://commission.europa.eu/news-and-media/news/simpler-digital-rules-help-eu-businesses-grow-2025-11-19_en).
- External reference: [An agile Digital Rulebook for the EU](https://digital-strategy.ec.europa.eu/en/policies/digital-rulebook).
- Derivation direction: issue #855 EU regulatory stack plus official European Commission references plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for Digital Omnibus only because the user requested one change per pending regulation item. Digital Omnibus has a cross-cutting proposal-stage source authority, simplification-tracking scope, affected governance owners, and implementation acceptance boundary distinct from enacted single-regulation skills.

## Impact

XML skill indexes, XML skill references, `skills.xml`, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.
