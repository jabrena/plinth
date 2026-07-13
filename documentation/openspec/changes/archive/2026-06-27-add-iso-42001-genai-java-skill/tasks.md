# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review issue #939 and identify ISO/IEC 42001 as scoped skill `813`.
- [x] 1.2 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 1.3 Compare the existing `801-regulations-eu-ai-act` and current regulation skill pattern before implementing the new skill.
- [x] 1.4 Add `plinth-skills-generator/src/main/resources/skill-indexes/813-skill.xml`.
- [x] 1.5 Add `plinth-skills-generator/src/main/resources/skill-references/813-regulations-iso-42001-chapters-summary.xml` with issue-provided ISO/IEC 42001 source links and AI management system concept mapping.
- [x] 1.6 Add `plinth-skills-generator/src/main/resources/skill-references/813-regulations-iso-42001-engineering-examples.xml` with Java GenAI engineering examples and output guidance.
- [x] 1.7 Include engineering controls for hallucinated code, insecure generated implementation, generated dependency and supply-chain risk, IP leakage, confidentiality breach, regulatory non-compliance risk, and biased generated business logic.
- [x] 1.8 Add `plinth-skills-generator/src/main/resources/skill-references/assets/questions/813-iso-42001-engineering-review-questionnaire.md`.
- [x] 1.9 Add `plinth-skills-generator/src/main/resources/skill-references/assets/reports/813-iso-42001-engineering-review-report-template.md`.
- [x] 1.10 Ensure the skill workflow reads the ISO/IEC 42001 summary, engineering examples, questionnaire, and report template before implementation review.
- [x] 1.11 Ensure the skill clearly states that it provides engineering guidance, not legal advice, certification advice, audit conclusions, or final conformity decisions.
- [x] 1.12 Register skill id `813` with explicit `skillId="813-regulations-iso-42001"`, references, questionnaire, and report template in `plinth-skills-generator/src/main/resources/skills.xml`.
- [x] 1.13 Add `plinth-skills-generator/src/test/resources/gherkin/skills/813-regulations-iso-42001.feature` with acceptance and integration scenarios modeled after existing regulation skills.
- [x] 1.14 Ensure the Gherkin scenarios require reading bundled references and assets, recognizing GenAI-oriented Java development risks, producing an engineering review report, and avoiding legal, audit, certification, or conformity conclusions.
- [x] 1.15 Add `813-regulations-iso-42001` to `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [x] 1.16 Validate changed XML files with `xmllint --noout`.
- [x] 1.17 Run `./mvnw clean install -pl plinth-skills-generator`.
- [x] 1.18 Inspect generated local `.agents/skills/813-regulations-iso-42001/SKILL.md`.
- [x] 1.19 Inspect generated local ISO/IEC 42001 summary, engineering examples, questionnaire, and report template outputs.
- [x] 1.20 Execute the listed `813-regulations-iso-42001` acceptance prompt and verify it passes. Manual execution generated `examples/regulations/iso-42001/ISO-42001-ENGINEERING-REVIEW-REPORT.md` from the listed source files using the generated local skill; the final cleanup step was intentionally not applied because the report output was requested for review.
- [x] 1.21 Run `./mvnw clean verify -pl plinth-skills-generator`.
- [x] 1.22 Run `openspec validate --all`.
