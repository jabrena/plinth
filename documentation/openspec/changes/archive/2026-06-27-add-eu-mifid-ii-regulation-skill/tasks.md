# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review issue #876 and identify MiFID II as scoped skill `810`.
- [x] 1.2 Create a per-regulation OpenSpec change for MiFID II.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 1.4 Add `plinth-skills-generator/src/main/resources/skill-indexes/810-skill.xml`.
- [x] 1.5 Add `plinth-skills-generator/src/main/resources/skill-references/810-regulations-eu-mifid-ii-chapters-summary.xml` with official EUR-Lex links and MiFID II structure mapping.
- [x] 1.6 Add `plinth-skills-generator/src/main/resources/skill-references/810-regulations-eu-mifid-ii-engineering-examples.xml` with Java-focused examples and output guidance.
- [x] 1.7 Add `plinth-skills-generator/src/main/resources/skill-references/assets/questions/810-mifid-ii-engineering-review-questionnaire.md`.
- [x] 1.8 Add `plinth-skills-generator/src/main/resources/skill-references/assets/reports/810-mifid-ii-engineering-review-report-template.md` with a potential non-compliance table that includes reference area and associated official-source link columns.
- [x] 1.9 Include engineering controls for investment-service scope triage, algorithmic trading controls, order handling, suitability/appropriateness workflows, client classification, record keeping, audit evidence, monitoring, incident/escalation paths, and owner handoff.
- [x] 1.10 Update `810-skill.xml` so the workflow reads the chapters summary, engineering examples, questionnaire, and report template before implementation review.
- [x] 1.11 Register skill id `810` with explicit `skillId="810-regulations-eu-mifid-ii"`, references, questionnaire, and report template resource in `plinth-skills-generator/src/main/resources/skills.xml`.
- [x] 1.12 Add `plinth-skills-generator/src/test/resources/gherkin/skills/810-regulations-eu-mifid-ii.feature` with pull-request and direct-to-main acceptance scenarios modeled after existing `801`-`808` regulation skills.
- [x] 1.13 Ensure the Gherkin scenarios require reading bundled references and producing engineering evidence without legal advice.
- [x] 1.14 Add `810-regulations-eu-mifid-ii` to `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [x] 1.15 Validate changed XML files with `xmllint --noout`.
- [x] 1.16 Run `./mvnw clean install -pl plinth-skills-generator`.
- [x] 1.17 Inspect generated local `.agents/skills/810-regulations-eu-mifid-ii/SKILL.md`.
- [x] 1.18 Inspect generated local chapters summary, engineering examples, questionnaire, and report template outputs.
- [ ] 1.19 Execute the listed `810-regulations-eu-mifid-ii` acceptance prompt and verify it passes.
- [x] 1.20 Run `./mvnw clean verify -pl plinth-skills-generator`.
- [x] 1.21 Run `openspec validate --all`.
