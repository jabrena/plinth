# Tasks

## 1. Source Analysis

- [x] 1.1 Review GitHub issue #848 source links for UK regulations.
- [x] 1.2 Review the existing `801-regulations-eu-ai-act` skill index and reference pattern.
- [x] 1.3 Review the active `add-eu-regulation-skills` change to avoid identifier overlap.
- [x] 1.4 Confirm UK operational resilience and UK GDPR belong in the `800` regulations skill band as new skills `804` and `805`.

## 2. OpenSpec Artifacts

- [x] 2.1 Create an OpenSpec change for UK regulation skills.
- [x] 2.2 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 2.3 Add requirements for UK operational resilience, UK GDPR, generator registration, and generated-output boundaries.

## 3. UK Operational Resilience Skill

- [ ] 3.1 Add `skills-generator/src/main/resources/skill-indexes/804-skill.xml`.
- [ ] 3.2 Add `skills-generator/src/main/resources/skill-references/804-regulations-uk-operational-resilience.xml`.
- [ ] 3.3 Include engineering controls for important business services, impact tolerances, dependency mapping, severe-but-plausible scenario testing, third-party risk, monitoring, incident response, backup/recovery, change control, rollback, and operational evidence.

## 4. UK GDPR Skill

- [ ] 4.1 Add `skills-generator/src/main/resources/skill-indexes/805-skill.xml`.
- [ ] 4.2 Add `skills-generator/src/main/resources/skill-references/805-regulations-uk-gdpr.xml`.
- [ ] 4.3 Include engineering controls for personal-data inventory, minimization, lawful-basis handoff, purpose limitation, privacy by design, data-subject rights, retention/deletion, security of processing, data transfers, DPIA escalation, breach evidence, and privacy-safe logging.

## 5. Generator Registration

- [ ] 5.1 Register skill id `804` and reference `804-regulations-uk-operational-resilience` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 5.2 Register skill id `805` and reference `805-regulations-uk-gdpr` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 5.3 Confirm generated local skills include `804-regulations-uk-operational-resilience` and `805-regulations-uk-gdpr`.

## 6. Validation

- [ ] 6.1 Validate changed XML files with `xmllint --noout`.
- [ ] 6.2 Run `./mvnw clean install -pl skills-generator`.
- [ ] 6.3 Inspect generated local `804-regulations-uk-operational-resilience/SKILL.md` and `805-regulations-uk-gdpr/SKILL.md`.
- [ ] 6.4 Check `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`; record that no listed acceptance prompts exist for 804/805 unless added by this change.
- [ ] 6.5 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 6.6 Run `openspec validate --all`.
