# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review the combined UK regulation planning and identify UK GDPR as a separate regulation scope.
- [x] 1.2 Create a per-regulation OpenSpec change for UK GDPR.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.4 Add `skills-generator/src/main/resources/skill-indexes/822-skill.xml`.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-references/822-regulations-uk-gdpr.xml`.
- [ ] 1.6 Include engineering controls for personal-data inventory, minimization, lawful-basis handoff, purpose limitation, privacy by design, data-subject rights, retention/deletion, security of processing, data transfers, DPIA escalation, breach evidence, and privacy-safe logging.
- [ ] 1.7 Register skill id `822` and reference `822-regulations-uk-gdpr` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.8 Validate changed XML files with `xmllint --noout`.
- [ ] 1.9 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.10 Inspect generated local `822-regulations-uk-gdpr/SKILL.md`.
- [ ] 1.11 Check `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`; record that no listed acceptance prompt exists for `822` unless added by this change.
- [ ] 1.12 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.13 Run `openspec validate --all`.
