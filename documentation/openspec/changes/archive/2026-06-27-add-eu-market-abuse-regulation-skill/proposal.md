## Why

GitHub issue [#876](https://github.com/jabrena/cursor-rules-java/issues/876) scopes the next EU regulation-skill batch to three explicit deliverables. This change covers the second deliverable: `811-regulations-eu-market-abuse-regulation`.

Java enterprise teams that build trading systems, surveillance platforms, suspicious order/transaction monitoring, disclosure workflows, insider-list support, or AI-assisted market-abuse detection need MAR-aware engineering guidance. The guidance must help teams produce reviewable evidence for compliance owners without turning the skill into legal advice.

## What Changes

- Add `811-regulations-eu-market-abuse-regulation` for engineering review of Market Abuse Regulation concerns in Java enterprise systems.
- Model the skill after the existing regulation skill pattern: source-first review, "not legal advice" constraints, Java-focused engineering controls, explicit owner handoff, examples, questionnaire/report assets, Gherkin acceptance coverage, and local generated skill validation.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/811-regulations-eu-market-abuse-regulation`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `eu-market-abuse-regulation-skill-reference`: Adds Market Abuse Regulation skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#876](https://github.com/jabrena/cursor-rules-java/issues/876).
- Existing implementation model: `801-regulations-eu-ai-act`, `802-regulations-dora`, and `807-regulations-eu-digital-services-act`.
- Official source: [Regulation (EU) No 596/2014 (Market Abuse Regulation), EUR-Lex](https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng).
- Derivation direction: issue #876 plus official MAR reference plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for MAR only. It has its own source authority, market-integrity scope, compliance-owner boundary, and implementation acceptance criteria. It depends conceptually on the issue #876 delivery order after MiFID II, but it can be reviewed as a standalone regulation skill.

## Impact

XML skill indexes, XML skill references, questionnaire/report assets, `skills.xml`, Gherkin acceptance tests, acceptance prompt inventory, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
