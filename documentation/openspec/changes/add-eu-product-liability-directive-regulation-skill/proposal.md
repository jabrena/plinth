## Why

GitHub issue [#876](https://github.com/jabrena/cursor-rules-java/issues/876) scopes the next EU regulation-skill batch to three explicit deliverables. This change covers the third deliverable: `812-regulations-eu-product-liability-directive`.

Java enterprise teams that build AI-enabled software products, SaaS products, embedded software, GenAI assistants, RAG workflows, AI agents, generated instructions, automated updates, or cybersecurity-sensitive product features need Product Liability Directive-aware engineering guidance. The guidance must help teams produce reviewable product-liability evidence for qualified owners without turning the skill into legal advice.

## What Changes

- Add `812-regulations-eu-product-liability-directive` for engineering review of Directive (EU) 2024/2853 concerns in Java software and AI-enabled products.
- Model the skill after the existing regulation skill pattern: source-first review, "not legal advice" constraints, Java-focused engineering controls, explicit owner handoff, examples, questionnaire/report assets, Gherkin acceptance coverage, and local generated skill validation.
- Register the skill in the generator inventory so local skill generation emits `.agents/skills/812-regulations-eu-product-liability-directive`.
- Keep generated public `skills/` output out of scope unless a release profile is intentionally run later.

## Capabilities

### New Capabilities

- `eu-product-liability-directive-regulation-skill-reference`: Adds Product Liability Directive regulation skill guidance.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#876](https://github.com/jabrena/cursor-rules-java/issues/876).
- Existing implementation model: `801-regulations-eu-ai-act`, `805-regulations-eu-cyber-resilience-act`, and `804-regulations-eu-nis2`.
- Official source: [Directive (EU) 2024/2853, EUR-Lex](https://eur-lex.europa.eu/eli/dir/2024/2853/oj/eng).
- Derivation direction: issue #876 plus official Product Liability Directive reference plus existing regulation skill pattern -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change for the Product Liability Directive only. It has its own source authority, software/AI product-liability scope, product/legal/safety-owner boundary, and implementation acceptance criteria. It depends conceptually on the issue #876 delivery order after MiFID II and MAR, but it can be reviewed as a standalone regulation skill.

## Impact

XML skill indexes, XML skill references, questionnaire/report assets, `skills.xml`, Gherkin acceptance tests, acceptance prompt inventory, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
