# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review issue #876 and identify Product Liability Directive as scoped skill `812`.
- [x] 1.2 Create a per-regulation OpenSpec change for Product Liability Directive.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 1.4 Add `plinth-skills-generator/src/main/resources/skill-indexes/812-skill.xml`.
- [x] 1.5 Add `plinth-skills-generator/src/main/resources/skill-references/812-regulations-eu-product-liability-directive-chapters-summary.xml` with official EUR-Lex links and Directive (EU) 2024/2853 structure mapping.
- [x] 1.6 Add `plinth-skills-generator/src/main/resources/skill-references/812-regulations-eu-product-liability-directive-engineering-examples.xml` with Java-focused examples and output guidance.
- [x] 1.7 Add `plinth-skills-generator/src/main/resources/skill-references/assets/questions/812-product-liability-directive-engineering-review-questionnaire.md`.
- [x] 1.8 Add `plinth-skills-generator/src/main/resources/skill-references/assets/reports/812-product-liability-directive-engineering-review-report-template.md` with a product-liability evidence gap table that includes reference area and associated official-source link columns.
- [x] 1.9 Include engineering controls for software/AI product scope triage, generated instructions, RAG source governance, warnings/instructions for use, safety validation, model/version provenance, update history, incident records, audit logs, vulnerability handling, corrective updates, and product-safety escalation.
- [x] 1.10 Update `812-skill.xml` so the workflow reads the chapters summary, engineering examples, questionnaire, and report template before implementation review.
- [x] 1.11 Register skill id `812` with explicit `skillId="812-regulations-eu-product-liability-directive"`, references, questionnaire, and report template resource in `plinth-skills-generator/src/main/resources/skills.xml`.
- [x] 1.12 Add `plinth-skills-generator/src/test/resources/gherkin/skills/812-regulations-eu-product-liability-directive.feature` with pull-request and direct-to-main acceptance scenarios modeled after existing `801`-`808` regulation skills.
- [x] 1.13 Ensure the Gherkin scenarios require reading bundled references, recognizing the Spring Boot RAG maintenance-assistant trigger pattern, and producing engineering evidence without legal advice.
- [x] 1.14 Add `812-regulations-eu-product-liability-directive` to `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [x] 1.15 Validate changed XML files with `xmllint --noout`.
- [x] 1.16 Run `./mvnw clean install -pl plinth-skills-generator`.
- [x] 1.17 Inspect generated local `.agents/skills/812-regulations-eu-product-liability-directive/SKILL.md`.
- [x] 1.18 Inspect generated local chapters summary, engineering examples, questionnaire, and report template outputs.
- [ ] 1.19 Execute the listed `812-regulations-eu-product-liability-directive` acceptance prompt and verify it passes.
- [x] 1.20 Run `./mvnw clean verify -pl plinth-skills-generator`.
- [x] 1.21 Run `openspec validate --all`.
