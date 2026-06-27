## Why

GitHub issue [#876](https://github.com/jabrena/cursor-rules-java/issues/876) scopes the next EU regulation-skill batch to three explicit deliverables. This change covers the first deliverable: `810-regulations-eu-mifid-ii`.

Java enterprise teams that build investment-service systems, algorithmic trading workflows, order handling, client classification, suitability, appropriateness, or AI-assisted investment decision support need MiFID II-aware engineering guidance. The guidance must help teams produce reviewable evidence for compliance owners without turning the skill into legal advice.

## What Changes

- Add `810-regulations-eu-mifid-ii` for engineering review of MiFID II concerns in Java enterprise systems.
- Model the skill after the existing regulation skill pattern: source-first review, "not legal advice" constraints, Java-focused engineering controls, explicit owner handoff, examples, questionnaire/report assets, Gherkin acceptance coverage, and local generated skill validation.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/810-regulations-eu-mifid-ii`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `eu-mifid-ii-regulation-skill-reference`: Adds MiFID II regulation skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#876](https://github.com/jabrena/cursor-rules-java/issues/876).
- Existing implementation model: `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr`.
- Official source: [Directive 2014/65/EU (MiFID II), EUR-Lex](https://eur-lex.europa.eu/eli/dir/2014/65/oj/eng).
- Derivation direction: issue #876 plus official MiFID II reference plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for MiFID II only. It has its own source authority, financial-market scope, compliance-owner boundary, and implementation acceptance criteria. It should be delivered before MAR and Product Liability Directive as requested in issue #876.

## Impact

XML skill indexes, XML skill references, questionnaire/report assets, `skills.xml`, Gherkin acceptance tests, acceptance prompt inventory, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
