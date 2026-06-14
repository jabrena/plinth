## Why

The generated `804-regulations-eu-nis2` skill introduced a cleaner regulation-skill structure than the earlier `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr` skills. It separates directive source context from Java engineering examples, points the generated `SKILL.md` directly at both references, and uses report templates that map findings to linked regulation areas.

Skills `801-803` remain functionally useful, but their reference structure is less consistent:

- `801-regulations-eu-ai-act` has a chapter summary, but Java examples remain mixed into the primary reference.
- `802-regulations-dora` and `803-regulations-gdpr` mix regulation context, examples, output guidance, and safeguards in one reference file.
- `801-803` report templates do not use the stronger linked reference-area mapping introduced by `804`.

This technical-debt change captures the follow-up work needed to align `801-803` with the `804` structural pattern without changing the legal/regulatory intent of those skills.

## What Changes

- Split Java engineering examples for `801`, `802`, and `803` into dedicated `*-engineering-examples` references.
- Add dedicated chapter/article summary references for `802` and `803`.
- Improve the existing `801` chapter summary with direct official-source chapter links and a consistent article-map structure where practical.
- Update `801-803` generated `SKILL.md` workflows to read regulation summary, engineering examples, questionnaire asset when applicable, and report template in a clear order.
- Update `801-803` report templates to include linked regulation reference-area mapping similar to `804`.
- Preserve questionnaires for `801-803` unless a later approved change intentionally replaces them.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `eu-regulation-skill-references`: Aligns EU regulation skills `801-803` with the newer split-reference structure introduced by `804-regulations-eu-nis2`.

## Source and Derivation

- Source artifact: local generated skill structure review for `.agents/skills/801-regulations-eu-ai-act`, `.agents/skills/802-regulations-dora`, `.agents/skills/803-regulations-gdpr`, and `.agents/skills/804-regulations-eu-nis2`.
- Existing implementation model: `804-regulations-eu-nis2` generated `SKILL.md`, `references/804-regulations-eu-nis2-chapters-summary.md`, `references/804-regulations-eu-nis2-engineering-examples.md`, and `assets/reports/804-nis2-engineering-review-report-template.md`.
- Existing baseline sources: `skills-generator/src/main/resources/skill-indexes/801-skill.xml`, `802-skill.xml`, `803-skill.xml`; their skill references; their questionnaire and report assets; their Gherkin acceptance scenarios.
- Derivation direction: generated skill structure comparison -> OpenSpec technical-debt change -> future XML source refactor -> local skill regeneration and validation.

## Change Boundary Assessment

This is one technical-debt change because the outcome is structural alignment for the existing implemented EU regulation baseline (`801-803`). It does not add a new regulation skill and does not change the scope or numbering of the pending EU regulation stack.

## Impact

XML skill indexes, XML skill references, report templates, `skills.xml`, Gherkin acceptance expectations, local generated skill output, and OpenSpec artifacts may be affected during implementation. Generated `.cursor/rules/`, public `skills/`, and `docs/` must not be edited directly.
