# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review the combined USA regulation planning and identify NYDFS cybersecurity as a separate regulation scope.
- [x] 1.2 Create a per-regulation OpenSpec change for NYDFS cybersecurity.
- [x] 1.3 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.4 Add `skills-generator/src/main/resources/skill-indexes/832-skill.xml`.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-references/832-regulations-usa-nydfs-cybersecurity.xml`.
- [ ] 1.6 Include engineering controls for cybersecurity governance, asset inventory, risk assessment, MFA, IAM, least privilege, secure configuration, encryption, vulnerability management, application security, logging, incident response, business continuity, disaster recovery, third-party service-provider security, audit trails, and certification evidence.
- [ ] 1.7 Register skill id `832` and reference `832-regulations-usa-nydfs-cybersecurity` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.8 Validate changed XML files with `xmllint --noout`.
- [ ] 1.9 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.10 Inspect generated local `832-regulations-usa-nydfs-cybersecurity/SKILL.md`.
- [ ] 1.11 Check `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`; record that no listed acceptance prompt exists for `832` unless added by this change.
- [ ] 1.12 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.13 Run `openspec validate --all`.
