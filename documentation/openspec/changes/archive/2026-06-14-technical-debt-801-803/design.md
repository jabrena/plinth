## Context

The `804-regulations-eu-nis2` skill established a clearer generated skill layout for regulation references:

- `SKILL.md` names the official source reference, the generated chapters summary, the generated engineering examples reference, and the report template.
- The chapters summary focuses on directive structure, article or chapter mapping, engineering impact, owner escalation, output format, and safeguards.
- The engineering examples reference focuses on Java control patterns and reviewable evidence.
- The report template maps potential non-compliance signals to reference areas and linked official-source chapters.

The earlier `801`, `802`, and `803` skills predate this structure. They should be aligned so EU regulation skills remain predictable for agents and maintainers.

## Decisions

### Keep Skill IDs and Regulatory Scope Stable

Do not rename or renumber `801-regulations-eu-ai-act`, `802-regulations-dora`, or `803-regulations-gdpr`.

This change is structural. It must preserve each skill's current regulatory scope, "not legal advice" posture, owner-escalation model, and generated-output boundaries.

### Use 804 as the Structural Target

Use `804-regulations-eu-nis2` as the structural target for regulation reference layout:

- One generated `SKILL.md`.
- One dedicated regulation chapter/article summary reference.
- One dedicated Java engineering examples reference.
- Existing questionnaire asset when that skill already uses a questionnaire.
- One engineering review report template.

`801` already has a chapter summary, so implementation should improve and reuse it instead of creating an overlapping second summary. `802` and `803` need new chapter/article summary references.

### Preserve Questionnaires for 801-803

The existing questionnaires are part of the `801-803` review workflow. This technical-debt change should preserve them and update the workflow order to:

1. Read the chapter/article summary.
2. Read the engineering examples reference.
3. Read the questionnaire asset, when present.
4. Read the report template.
5. Run the questionnaire and review workflow as defined by the skill.

Removing or replacing questionnaires would be a behavior change and should be handled by a separate approved OpenSpec change.

### Improve Report Mapping

Report templates for `801-803` should keep their existing sections but strengthen potential violation or non-compliance mapping by adding linked reference areas comparable to the NIS2 template.

Where official-source chapter anchors are stable, templates and summaries should include direct links. Where anchors are not stable or are impractical, use chapter/article labels and link to the official EUR-Lex source.

### Acceptance Evidence

Existing Gherkin acceptance scenarios for `801-803` must be updated so they expect the new reference files to be read. Existing example reports under `examples/regulations` must be refreshed only through acceptance execution or equivalent generated-skill review, not hand-edited as arbitrary documentation.

## Validation Strategy

- Validate changed XML files with `xmllint --noout`.
- Run `./mvnw clean install -pl skills-generator` to regenerate local skills into `.agents/skills`.
- Inspect generated local `801-803` `SKILL.md` files, chapter summaries, engineering examples, questionnaire assets, and report templates.
- Update and review `skills-generator/src/test/resources/gherkin/skills/801-regulations-eu-ai-act.feature`, `802-regulations-dora.feature`, and `803-regulations-gdpr.feature`.
- If regenerated local skill output changes for skills listed in `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`, execute the listed prompt for each changed skill and verify acceptance passes.
- Run `./mvnw clean verify -pl skills-generator`.
- Run `openspec validate --all`.

## Open Questions

None for planning. During implementation, maintainers should verify official EUR-Lex anchor stability before adding direct chapter links to `801-803` report templates.
