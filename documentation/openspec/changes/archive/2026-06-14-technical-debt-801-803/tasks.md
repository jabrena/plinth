# Tasks

## 1. Discovery and Planning

- [x] 1.1 Compare generated `.agents/skills/801-regulations-eu-ai-act`, `.agents/skills/802-regulations-dora`, `.agents/skills/803-regulations-gdpr`, and `.agents/skills/804-regulations-eu-nis2` structures.
- [x] 1.2 Identify structural gaps in `801-803` relative to the `804` split-reference pattern.
- [x] 1.3 Create OpenSpec change `technical-debt-801-803`.
- [x] 1.4 Record source artifacts, derivation direction, scope boundary, and validation expectations.

## 2. 801-regulations-eu-ai-act

- [x] 2.1 Split `801-regulations-eu-ai-act` Java examples into `skills-generator/src/main/resources/skill-references/801-regulations-eu-ai-act-engineering-examples.xml`.
- [x] 2.2 Update `801-regulations-eu-ai-act-chapters-summary.xml` so each chapter or annex summary heading includes the associated official-source link in the same style as `804-regulations-eu-nis2-chapters-summary.xml`.
- [x] 2.3 Update `skills-generator/src/main/resources/skill-indexes/801-skill.xml` so the workflow reads summary, engineering examples, questionnaire asset, and report template in a clear order.
- [x] 2.4 Update `skills-generator/src/main/resources/skill-references/assets/reports/801-eu-ai-act-engineering-review-report-template.md` so the potential violation or non-compliance mapping table includes an associated official-source link column in the same style as the `804` report template.
- [x] 2.5 Update `skills-generator/src/test/resources/gherkin/skills/801-regulations-eu-ai-act.feature` to expect the new engineering examples reference and linked violation mapping format.
- [x] 2.6 Refresh `examples/regulations/eu-ai-act` acceptance report examples so their potential violation or non-compliance sections use the new linked mapping table format.
- [x] 2.7 Inspect generated local `.agents/skills/801-regulations-eu-ai-act` output.
- [x] 2.8 Execute the listed `801-regulations-eu-ai-act` acceptance prompt in `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.

## 3. 802-regulations-dora

- [x] 3.1 Add `skills-generator/src/main/resources/skill-references/802-regulations-dora-chapters-summary.xml` so each chapter summary heading includes the associated official-source link in the same style as `804-regulations-eu-nis2-chapters-summary.xml`.
- [x] 3.2 Split `802-regulations-dora` Java examples into `skills-generator/src/main/resources/skill-references/802-regulations-dora-engineering-examples.xml`.
- [x] 3.3 Update `skills-generator/src/main/resources/skill-indexes/802-skill.xml` so the workflow reads summary, engineering examples, questionnaire asset, and report template in a clear order.
- [x] 3.4 Update `skills-generator/src/main/resources/skill-references/assets/reports/802-dora-engineering-review-report-template.md` so the potential violation or non-compliance mapping table includes an associated official-source link column in the same style as the `804` report template.
- [x] 3.5 Update `skills-generator/src/test/resources/gherkin/skills/802-regulations-dora.feature` to expect the new chapter summary, engineering examples reference, and linked violation mapping format.
- [x] 3.6 Refresh `examples/regulations/dora` acceptance report examples so their potential violation or non-compliance sections use the new linked mapping table format.
- [x] 3.7 Inspect generated local `.agents/skills/802-regulations-dora` output.
- [x] 3.8 Execute the listed `802-regulations-dora` acceptance prompt in `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.

## 4. 803-regulations-gdpr

- [x] 4.1 Add `skills-generator/src/main/resources/skill-references/803-regulations-gdpr-chapters-summary.xml` so each chapter summary heading includes the associated official-source link in the same style as `804-regulations-eu-nis2-chapters-summary.xml`.
- [x] 4.2 Split `803-regulations-gdpr` Java examples into `skills-generator/src/main/resources/skill-references/803-regulations-gdpr-engineering-examples.xml`.
- [x] 4.3 Update `skills-generator/src/main/resources/skill-indexes/803-skill.xml` so the workflow reads summary, engineering examples, questionnaire asset, and report template in a clear order.
- [x] 4.4 Update `skills-generator/src/main/resources/skill-references/assets/reports/803-gdpr-engineering-review-report-template.md` so the potential violation or non-compliance mapping table includes an associated official-source link column in the same style as the `804` report template.
- [x] 4.5 Update `skills-generator/src/test/resources/gherkin/skills/803-regulations-gdpr.feature` to expect the new chapter summary, engineering examples reference, and linked violation mapping format.
- [x] 4.6 Refresh `examples/regulations/gdpr` acceptance report examples so their potential violation or non-compliance sections use the new linked mapping table format.
- [x] 4.7 Inspect generated local `.agents/skills/803-regulations-gdpr` output.
- [x] 4.8 Execute the listed `803-regulations-gdpr` acceptance prompt in `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.

## 5. Shared Generator and Validation

- [x] 5.1 Update `skills-generator/src/main/resources/skills.xml` to register the new or renamed references and preserve questionnaire/report resources.
- [x] 5.2 Validate changed XML files with `xmllint --noout`.
- [x] 5.3 Run `./mvnw clean install -pl skills-generator`.
- [x] 5.4 Run `./mvnw clean verify -pl skills-generator`.
- [x] 5.5 Run `openspec validate --all`.
