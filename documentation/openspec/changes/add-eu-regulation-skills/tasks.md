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

- [ ] 3.1 Add `skills-generator/src/main/resources/skill-indexes/802-skill.xml`.
- [ ] 3.2 Add `skills-generator/src/main/resources/skill-references/802-regulations-dora.xml`.
- [ ] 3.3 Include engineering controls for ICT risk management, operational resilience, incident preparation, resilience testing, third-party ICT provider risk, backup/recovery, monitoring, and operational evidence.
- [ ] 3.4 Align the `802` skill index and reference structure with `801-regulations-eu-ai-act`, adapting the content to DORA.

## 4. GDPR Skill

- [ ] 4.1 Add `skills-generator/src/main/resources/skill-indexes/803-skill.xml`.
- [ ] 4.2 Add `skills-generator/src/main/resources/skill-references/803-regulations-gdpr.xml`.
- [ ] 4.3 Include engineering controls for personal-data inventory, minimization, privacy by design, data-subject rights, retention/deletion, security of processing, data transfers, DPIA escalation, breach evidence, and privacy-safe logging.
- [ ] 4.4 Align the `803` skill index and reference structure with `801-regulations-eu-ai-act`, adapting the content to GDPR.

## 5. Gherkin Acceptance Coverage

- [ ] 5.1 Add `skills-generator/src/test/resources/gherkin/801-regulations-eu-ai-act.feature` for the existing EU AI Act skill.
- [ ] 5.2 Add `skills-generator/src/test/resources/gherkin/802-regulations-dora.feature` for the DORA skill.
- [ ] 5.3 Add `skills-generator/src/test/resources/gherkin/803-regulations-gdpr.feature` for the GDPR skill.
- [ ] 5.4 Register the `801`, `802`, and `803` Gherkin prompts in `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`.

## 6. Generator Registration

- [ ] 6.1 Register skill id `802` and reference `802-regulations-dora` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 6.2 Register skill id `803` and reference `803-regulations-gdpr` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 6.3 Confirm generated local skills include `802-regulations-dora` and `803-regulations-gdpr`.

## 7. Validation

- [ ] 7.1 Validate changed XML files with `xmllint --noout`.
- [ ] 7.2 Run `./mvnw clean install -pl skills-generator`.
- [ ] 7.3 Inspect generated local `802-regulations-dora/SKILL.md` and `803-regulations-gdpr/SKILL.md`.
- [ ] 7.4 Compare generated local `802` and `803` skill structure against generated local `801-regulations-eu-ai-act/SKILL.md` for consistent `800`-band regulation skill format.
- [ ] 7.5 Execute the listed acceptance prompts for `801`, `802`, and `803` from `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`.
- [ ] 7.6 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 7.7 Run `openspec validate --all`.
