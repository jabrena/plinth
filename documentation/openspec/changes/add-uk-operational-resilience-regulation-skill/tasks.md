# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review the combined UK regulation planning and identify UK operational resilience as a separate regulation scope.
- [x] 1.2 Create a per-regulation OpenSpec change for UK operational resilience.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.4 Add `skills-generator/src/main/resources/skill-indexes/821-skill.xml`.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-references/821-regulations-uk-operational-resilience-chapters-summary.xml` with direct official-source links and section mapping in the same style as `804-regulations-eu-nis2-chapters-summary.xml`.
- [ ] 1.6 Add `skills-generator/src/main/resources/skill-references/821-regulations-uk-operational-resilience-engineering-examples.xml` with Java-focused examples and output guidance.
- [ ] 1.7 Add `skills-generator/src/main/resources/skill-references/assets/reports/821-uk-operational-resilience-engineering-review-report-template.md` with a potential violation, gap, or non-compliance table that includes reference area and associated official-source link columns.
- [ ] 1.8 Include engineering controls for important business services, impact tolerances, dependency mapping, severe-but-plausible scenario testing, third-party risk, monitoring, incident response, backup/recovery, change control, rollback, and operational evidence.
- [ ] 1.9 Update `821-skill.xml` so the workflow reads section summary, engineering examples, and report template before implementation review.
- [ ] 1.10 Register skill id `821` with explicit `skillId="821-regulations-uk-operational-resilience"`, references, and report template resource in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.11 Add `skills-generator/src/test/resources/gherkin/skills/821-regulations-uk-operational-resilience.feature` with pull-request and direct-to-main acceptance scenarios modeled after `801-804`.
- [ ] 1.12 Ensure the Gherkin scenarios require reading the summary, engineering examples, and report template, and require linked gap/non-compliance mapping in generated reports.
- [ ] 1.13 Add UK operational resilience report examples under `examples/regulations/uk-operational-resilience`.
- [ ] 1.14 Add `821-regulations-uk-operational-resilience` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [ ] 1.15 Validate changed XML files with `xmllint --noout`.
- [ ] 1.16 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.17 Inspect generated local `.agents/skills/821-regulations-uk-operational-resilience/SKILL.md`.
- [ ] 1.18 Inspect generated local summary, engineering examples, and report template outputs.
- [ ] 1.19 Execute the listed `821-regulations-uk-operational-resilience` acceptance prompt and verify it passes.
- [ ] 1.20 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.21 Run `openspec validate --all`.
