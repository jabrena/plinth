# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review the combined USA regulation planning and identify GLBA Safeguards as a separate regulation scope.
- [x] 1.2 Create a per-regulation OpenSpec change for GLBA Safeguards.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.4 Add `skills-generator/src/main/resources/skill-indexes/831-skill.xml`.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-references/831-regulations-usa-glba-safeguards.xml`.
- [ ] 1.6 Include engineering controls for customer-information inventory, risk assessment evidence, access controls, encryption, secure development, change management, monitoring, vulnerability management, incident response, service-provider oversight, retention/disposal, and audit-ready safeguards evidence.
- [ ] 1.7 Register skill id `831` and reference `831-regulations-usa-glba-safeguards` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.8 Validate changed XML files with `xmllint --noout`.
- [ ] 1.9 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.10 Inspect generated local `831-regulations-usa-glba-safeguards/SKILL.md`.
- [ ] 1.11 Check `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`; record that no listed acceptance prompt exists for `831` unless added by this change.
- [ ] 1.12 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.13 Run `openspec validate --all`.
