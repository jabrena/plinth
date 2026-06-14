# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review the combined UK regulation planning and identify UK operational resilience as a separate regulation scope.
- [x] 1.2 Create a per-regulation OpenSpec change for UK operational resilience.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.4 Add `skills-generator/src/main/resources/skill-indexes/821-skill.xml`.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-references/821-regulations-uk-operational-resilience.xml`.
- [ ] 1.6 Include engineering controls for important business services, impact tolerances, dependency mapping, severe-but-plausible scenario testing, third-party risk, monitoring, incident response, backup/recovery, change control, rollback, and operational evidence.
- [ ] 1.7 Register skill id `821` and reference `821-regulations-uk-operational-resilience` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.8 Validate changed XML files with `xmllint --noout`.
- [ ] 1.9 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.10 Inspect generated local `821-regulations-uk-operational-resilience/SKILL.md`.
- [ ] 1.11 Check `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`; record that no listed acceptance prompt exists for `821` unless added by this change.
- [ ] 1.12 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.13 Run `openspec validate --all`.
