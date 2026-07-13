# Tasks

## 1. Source Analysis

- [x] 1.1 Review GitHub issue #848 source links for EU regulations.
- [x] 1.2 Review the existing `801-regulations-eu-ai-act` skill index and reference format/structure.
- [x] 1.3 Confirm DORA and GDPR belong in the `800` regulations skill band as new skills `802` and `803`.

## 2. OpenSpec Artifacts

- [x] 2.1 Create an OpenSpec change for EU regulation skills.
- [x] 2.2 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 2.3 Add requirements for DORA, GDPR, generator registration, and generated-output boundaries.
- [x] 2.4 Add requirements for `802` and `803` to follow the `801-regulations-eu-ai-act` format and structure baseline.

## 3. DORA Skill

- [x] 3.1 Add `plinth-skills-generator/src/main/resources/skill-indexes/802-skill.xml`.
- [x] 3.2 Add `plinth-skills-generator/src/main/resources/skill-references/802-regulations-dora.xml`.
- [x] 3.3 Include engineering controls for ICT risk management, operational resilience, incident preparation, resilience testing, third-party ICT provider risk, backup/recovery, monitoring, and operational evidence.
- [x] 3.4 Align the `802` skill index and reference structure with `801-regulations-eu-ai-act`, adapting the content to DORA.
- [x] 3.5 Add `plinth-skills-generator/src/main/resources/skill-references/assets/questions/802-dora-engineering-review-questionnaire.md`.
- [x] 3.6 Add `plinth-skills-generator/src/main/resources/skill-references/assets/reports/802-dora-engineering-review-report-template.md`.
- [x] 3.7 Update `802` workflow and reference output guidance to require questionnaire-first review and report generation.
- [x] 3.8 Include DORA Java technical examples for operational resilience and release-policy controls.

## 4. GDPR Skill

- [x] 4.1 Add `plinth-skills-generator/src/main/resources/skill-indexes/803-skill.xml`.
- [x] 4.2 Add `plinth-skills-generator/src/main/resources/skill-references/803-regulations-gdpr.xml`.
- [x] 4.3 Include engineering controls for personal-data inventory, minimization, privacy by design, data-subject rights, retention/deletion, security of processing, data transfers, DPIA escalation, breach evidence, and privacy-safe logging.
- [x] 4.4 Align the `803` skill index and reference structure with `801-regulations-eu-ai-act`, adapting the content to GDPR.
- [x] 4.5 Add `plinth-skills-generator/src/main/resources/skill-references/assets/questions/803-gdpr-engineering-review-questionnaire.md`.
- [x] 4.6 Add `plinth-skills-generator/src/main/resources/skill-references/assets/reports/803-gdpr-engineering-review-report-template.md`.
- [x] 4.7 Update `803` workflow and reference output guidance to require questionnaire-first review and report generation.
- [x] 4.8 Include GDPR Java technical examples for personal-data minimization, rights workflows, privacy-safe logging, or field-level authorization.

## 5. Gherkin Acceptance Coverage

- [x] 5.1 Add `plinth-skills-generator/src/test/resources/gherkin/skills/801-regulations-eu-ai-act.feature` for the existing EU AI Act skill.
- [x] 5.2 Add `plinth-skills-generator/src/test/resources/gherkin/skills/802-regulations-dora.feature` for the DORA skill.
- [x] 5.3 Add `plinth-skills-generator/src/test/resources/gherkin/skills/803-regulations-gdpr.feature` for the GDPR skill.
- [x] 5.4 Register the `801`, `802`, and `803` Gherkin prompts in `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.

## 6. Generator Registration

- [x] 6.1 Register skill id `802` and reference `802-regulations-dora` in `plinth-skills-generator/src/main/resources/skills.xml`.
- [x] 6.2 Register skill id `803` and reference `803-regulations-gdpr` in `plinth-skills-generator/src/main/resources/skills.xml`.
- [x] 6.3 Confirm generated local skills include `802-regulations-dora` and `803-regulations-gdpr`.
- [x] 6.4 Register `802` questionnaire and report template resources in `plinth-skills-generator/src/main/resources/skills.xml`.
- [x] 6.5 Register `803` questionnaire and report template resources in `plinth-skills-generator/src/main/resources/skills.xml`.

## 7. Validation

- [x] 7.1 Validate changed XML files with `xmllint --noout`.
- [x] 7.2 Run `./mvnw clean install -pl plinth-skills-generator`.
- [x] 7.3 Inspect generated local `802-regulations-dora/SKILL.md` and `803-regulations-gdpr/SKILL.md`.
- [x] 7.4 Compare generated local `802` and `803` skill structure against generated local `801-regulations-eu-ai-act/SKILL.md` for consistent `800`-band regulation skill format.
- [x] 7.5 Inspect generated local DORA and GDPR questionnaire/report assets.
- [x] 7.6 Execute the listed acceptance prompts for `801`, `802`, and `803` from `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` (verified manually against generated local skill output; no separate automated runner exists).
- [x] 7.7 Run `./mvnw clean verify -pl plinth-skills-generator`.
- [x] 7.8 Run `openspec validate --all`.
