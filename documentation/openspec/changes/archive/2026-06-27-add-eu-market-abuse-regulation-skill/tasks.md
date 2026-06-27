# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review issue #876 and identify MAR as scoped skill `811`.
- [x] 1.2 Create a per-regulation OpenSpec change for MAR.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 1.4 Add `skills-generator/src/main/resources/skill-indexes/811-skill.xml`.
- [x] 1.5 Add `skills-generator/src/main/resources/skill-references/811-regulations-eu-market-abuse-regulation-chapters-summary.xml` with official EUR-Lex links and MAR structure mapping.
- [x] 1.6 Add `skills-generator/src/main/resources/skill-references/811-regulations-eu-market-abuse-regulation-engineering-examples.xml` with Java-focused examples and output guidance.
- [x] 1.7 Add `skills-generator/src/main/resources/skill-references/assets/questions/811-market-abuse-regulation-engineering-review-questionnaire.md`.
- [x] 1.8 Add `skills-generator/src/main/resources/skill-references/assets/reports/811-market-abuse-regulation-engineering-review-report-template.md` with a potential non-compliance table that includes reference area and associated official-source link columns.
- [x] 1.9 Include engineering controls for trading surveillance, suspicious order/transaction monitoring, insider-list workflows, disclosure workflows, market-data lineage, alert explainability, model/rule provenance, reviewer decisions, false-positive handling, investigation records, and compliance escalation.
- [x] 1.10 Update `811-skill.xml` so the workflow reads the chapters summary, engineering examples, questionnaire, and report template before implementation review.
- [x] 1.11 Register skill id `811` with explicit `skillId="811-regulations-eu-market-abuse-regulation"`, references, questionnaire, and report template resource in `skills-generator/src/main/resources/skills.xml`.
- [x] 1.12 Add `skills-generator/src/test/resources/gherkin/skills/811-regulations-eu-market-abuse-regulation.feature` with pull-request and direct-to-main acceptance scenarios modeled after existing `801`-`808` regulation skills.
- [x] 1.13 Ensure the Gherkin scenarios require reading bundled references and producing engineering evidence without legal advice.
- [x] 1.14 Add `811-regulations-eu-market-abuse-regulation` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [x] 1.15 Validate changed XML files with `xmllint --noout`.
- [x] 1.16 Run `./mvnw clean install -pl skills-generator`.
- [x] 1.17 Inspect generated local `.agents/skills/811-regulations-eu-market-abuse-regulation/SKILL.md`.
- [x] 1.18 Inspect generated local chapters summary, engineering examples, questionnaire, and report template outputs.
- [ ] 1.19 Execute the listed `811-regulations-eu-market-abuse-regulation` acceptance prompt and verify it passes.
- [x] 1.20 Run `./mvnw clean verify -pl skills-generator`.
- [x] 1.21 Run `openspec validate --all`.
