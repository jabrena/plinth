# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review the combined USA regulation planning and identify SEC cybersecurity disclosure readiness as a separate regulation scope.
- [x] 1.2 Create a per-regulation OpenSpec change for SEC cybersecurity disclosure readiness.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.4 Add `skills-generator/src/main/resources/skill-indexes/833-skill.xml`.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-references/833-regulations-usa-sec-cybersecurity-disclosure-sections-summary.xml` with direct official-source links and rule/disclosure mapping in the same style as the `801-804` chapter summary references.
- [ ] 1.6 Add `skills-generator/src/main/resources/skill-references/833-regulations-usa-sec-cybersecurity-disclosure-engineering-examples.xml` with Java-focused examples and output guidance.
- [ ] 1.7 Add `skills-generator/src/main/resources/skill-references/assets/reports/833-usa-sec-cybersecurity-disclosure-engineering-review-report-template.md` with a potential disclosure readiness gap table that includes reference area and associated official-source link columns.
- [ ] 1.8 Include engineering controls for incident classification handoff, materiality escalation paths, governance reporting signals, risk management evidence, timeline reconstruction, audit logs, remediation tracking, third-party dependency evidence, board/executive reporting inputs, and disclosure-safe documentation.
- [ ] 1.9 Update `833-skill.xml` so the workflow reads section summary, engineering examples, and report template before implementation review.
- [ ] 1.10 Register skill id `833` with explicit `skillId="833-regulations-usa-sec-cybersecurity-disclosure"`, references, and report template resource in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.11 Add `skills-generator/src/test/resources/gherkin/833-regulations-usa-sec-cybersecurity-disclosure.feature` with pull-request and direct-to-main acceptance scenarios modeled after `801-804`.
- [ ] 1.12 Ensure the Gherkin scenarios require reading the summary, engineering examples, and report template, and require linked disclosure-readiness mapping in generated reports.
- [ ] 1.13 Add SEC cybersecurity disclosure report examples under `examples/regulations/usa-sec-cybersecurity-disclosure`.
- [ ] 1.14 Add `833-regulations-usa-sec-cybersecurity-disclosure` to `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`.
- [ ] 1.15 Validate changed XML files with `xmllint --noout`.
- [ ] 1.16 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.17 Inspect generated local `.agents/skills/833-regulations-usa-sec-cybersecurity-disclosure/SKILL.md`.
- [ ] 1.18 Inspect generated local summary, engineering examples, and report template outputs.
- [ ] 1.19 Execute the listed `833-regulations-usa-sec-cybersecurity-disclosure` acceptance prompt and verify it passes.
- [ ] 1.20 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.21 Run `openspec validate --all`.
