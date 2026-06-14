# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review GitHub issue #855 comment listing the GenAI Regulatory Stack (EU).
- [x] 1.2 Confirm `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr` are already covered and Digital Omnibus remains pending as a cross-cutting simplification item.
- [x] 1.3 Create a per-regulation OpenSpec change for Digital Omnibus.
- [x] 1.4 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-indexes/809-skill.xml`.
- [ ] 1.6 Add `skills-generator/src/main/resources/skill-references/809-regulations-eu-digital-omnibus-source-summary.xml` with official source links, source-status notes, and affected-regulation mapping instead of treating Digital Omnibus as a settled single-regulation chapter checklist.
- [ ] 1.7 Add `skills-generator/src/main/resources/skill-references/809-regulations-eu-digital-omnibus-engineering-examples.xml` with Java-focused examples and output guidance.
- [ ] 1.8 Add `skills-generator/src/main/resources/skill-references/assets/reports/809-eu-digital-omnibus-engineering-review-report-template.md` with a potential impact or non-compliance mapping table that includes affected regulation area and associated official-source link columns.
- [ ] 1.9 Include engineering controls for source-status checks, affected-regulation mapping, evidence inventory updates, change-control impacts, questionnaire or report-template update candidates, incident-reporting workflow consolidation, data-rights workflow impacts, AI governance timeline changes, compatibility with existing regulation skills, and escalation when proposal-stage language is ambiguous.
- [ ] 1.10 Update `809-skill.xml` so the workflow reads source summary, engineering examples, and report template before implementation review.
- [ ] 1.11 Register skill id `809` with explicit `skillId="809-regulations-eu-digital-omnibus"`, references, and report template resource in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.12 Add `skills-generator/src/test/resources/gherkin/809-regulations-eu-digital-omnibus.feature` with pull-request and direct-to-main acceptance scenarios modeled after `801-804`.
- [ ] 1.13 Ensure the Gherkin scenarios require reading the source summary, engineering examples, and report template, and require linked source/status mapping in generated reports.
- [ ] 1.14 Add Digital Omnibus report examples under `examples/regulations/eu-digital-omnibus`.
- [ ] 1.15 Add `809-regulations-eu-digital-omnibus` to `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`.
- [ ] 1.16 Validate changed XML files with `xmllint --noout`.
- [ ] 1.17 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.18 Inspect generated local `.agents/skills/809-regulations-eu-digital-omnibus/SKILL.md`.
- [ ] 1.19 Inspect generated local source summary, engineering examples, and report template outputs.
- [ ] 1.20 Execute the listed `809-regulations-eu-digital-omnibus` acceptance prompt and verify it passes.
- [ ] 1.21 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.22 Run `openspec validate --all`.
