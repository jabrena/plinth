# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review the combined USA regulation planning and identify SEC cybersecurity disclosure readiness as a separate regulation scope.
- [x] 1.2 Create a per-regulation OpenSpec change for SEC cybersecurity disclosure readiness.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.4 Add `skills-generator/src/main/resources/skill-indexes/833-skill.xml`.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-references/833-regulations-usa-sec-cybersecurity-disclosure.xml`.
- [ ] 1.6 Include engineering controls for incident classification handoff, materiality escalation paths, governance reporting signals, risk management evidence, timeline reconstruction, audit logs, remediation tracking, third-party dependency evidence, board/executive reporting inputs, and disclosure-safe documentation.
- [ ] 1.7 Register skill id `833` and reference `833-regulations-usa-sec-cybersecurity-disclosure` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.8 Validate changed XML files with `xmllint --noout`.
- [ ] 1.9 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.10 Inspect generated local `833-regulations-usa-sec-cybersecurity-disclosure/SKILL.md`.
- [ ] 1.11 Check `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`; record that no listed acceptance prompt exists for `833` unless added by this change.
- [ ] 1.12 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.13 Run `openspec validate --all`.
