# Tasks

## 1. Source Analysis

- [x] 1.1 Review GitHub issue #848 source links for EU regulations.
- [x] 1.2 Review the existing `801-regulations-eu-ai-act` skill index and reference pattern.
- [x] 1.3 Confirm DORA and GDPR belong in the `800` regulations skill band as new skills `802` and `803`.

## 2. OpenSpec Artifacts

- [x] 2.1 Create an OpenSpec change for EU regulation skills.
- [x] 2.2 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 2.3 Add requirements for DORA, GDPR, generator registration, and generated-output boundaries.

## 3. DORA Skill

- [ ] 3.1 Add `skills-generator/src/main/resources/skill-indexes/802-skill.xml`.
- [ ] 3.2 Add `skills-generator/src/main/resources/skill-references/802-regulations-dora.xml`.
- [ ] 3.3 Include engineering controls for ICT risk management, operational resilience, incident preparation, resilience testing, third-party ICT provider risk, backup/recovery, monitoring, and operational evidence.

## 4. GDPR Skill

- [ ] 4.1 Add `skills-generator/src/main/resources/skill-indexes/803-skill.xml`.
- [ ] 4.2 Add `skills-generator/src/main/resources/skill-references/803-regulations-gdpr.xml`.
- [ ] 4.3 Include engineering controls for personal-data inventory, minimization, privacy by design, data-subject rights, retention/deletion, security of processing, data transfers, DPIA escalation, breach evidence, and privacy-safe logging.

## 5. Generator Registration

- [ ] 5.1 Register skill id `802` and reference `802-regulations-dora` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 5.2 Register skill id `803` and reference `803-regulations-gdpr` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 5.3 Confirm generated local skills include `802-regulations-dora` and `803-regulations-gdpr`.

## 6. Validation

- [ ] 6.1 Validate changed XML files with `xmllint --noout`.
- [ ] 6.2 Run `./mvnw clean install -pl skills-generator`.
- [ ] 6.3 Inspect generated local `802-regulations-dora/SKILL.md` and `803-regulations-gdpr/SKILL.md`.
- [ ] 6.4 Check `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`; record that no listed acceptance prompts exist for 802/803 unless added by this change.
- [ ] 6.5 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 6.6 Run `openspec validate --all`.
