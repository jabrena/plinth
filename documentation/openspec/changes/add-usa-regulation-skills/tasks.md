# Tasks

## 1. Source Analysis

- [x] 1.1 Review GitHub issue #848 source links for USA regulations.
- [x] 1.2 Review the existing `801-regulations-eu-ai-act` skill index and reference pattern.
- [x] 1.3 Review the active `add-eu-regulation-skills` and `add-uk-regulation-skills` changes to avoid identifier overlap.
- [x] 1.4 Confirm GLBA Safeguards, NYDFS Cybersecurity Regulation, and SEC Cybersecurity Disclosure Rules belong in the `800` regulations skill band as new skills `806`, `807`, and `808`.

## 2. OpenSpec Artifacts

- [x] 2.1 Create an OpenSpec change for USA regulation skills.
- [x] 2.2 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 2.3 Add requirements for GLBA Safeguards, NYDFS cybersecurity, SEC cybersecurity disclosure readiness, generator registration, and generated-output boundaries.

## 3. GLBA Safeguards Skill

- [ ] 3.1 Add `skills-generator/src/main/resources/skill-indexes/806-skill.xml`.
- [ ] 3.2 Add `skills-generator/src/main/resources/skill-references/806-regulations-usa-glba-safeguards.xml`.
- [ ] 3.3 Include engineering controls for customer-information inventory, risk assessment evidence, access controls, encryption, secure development, change management, monitoring, vulnerability management, incident response, service-provider oversight, retention/disposal, and audit-ready safeguards evidence.

## 4. NYDFS Cybersecurity Skill

- [ ] 4.1 Add `skills-generator/src/main/resources/skill-indexes/807-skill.xml`.
- [ ] 4.2 Add `skills-generator/src/main/resources/skill-references/807-regulations-usa-nydfs-cybersecurity.xml`.
- [ ] 4.3 Include engineering controls for cybersecurity governance, asset inventory, risk assessment, MFA, IAM, least privilege, secure configuration, encryption, vulnerability management, application security, logging, incident response, business continuity, disaster recovery, third-party service-provider security, audit trails, and certification evidence.

## 5. SEC Cybersecurity Disclosure Skill

- [ ] 5.1 Add `skills-generator/src/main/resources/skill-indexes/808-skill.xml`.
- [ ] 5.2 Add `skills-generator/src/main/resources/skill-references/808-regulations-usa-sec-cybersecurity-disclosure.xml`.
- [ ] 5.3 Include engineering controls for incident classification handoff, materiality escalation paths, governance reporting signals, risk management evidence, timeline reconstruction, audit logs, remediation tracking, third-party dependency evidence, board/executive reporting inputs, and disclosure-safe documentation.

## 6. Generator Registration

- [ ] 6.1 Register skill id `806` and reference `806-regulations-usa-glba-safeguards` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 6.2 Register skill id `807` and reference `807-regulations-usa-nydfs-cybersecurity` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 6.3 Register skill id `808` and reference `808-regulations-usa-sec-cybersecurity-disclosure` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 6.4 Confirm generated local skills include `806-regulations-usa-glba-safeguards`, `807-regulations-usa-nydfs-cybersecurity`, and `808-regulations-usa-sec-cybersecurity-disclosure`.

## 7. Validation

- [ ] 7.1 Validate changed XML files with `xmllint --noout`.
- [ ] 7.2 Run `./mvnw clean install -pl skills-generator`.
- [ ] 7.3 Inspect generated local `806-regulations-usa-glba-safeguards/SKILL.md`, `807-regulations-usa-nydfs-cybersecurity/SKILL.md`, and `808-regulations-usa-sec-cybersecurity-disclosure/SKILL.md`.
- [ ] 7.4 Check `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`; record that no listed acceptance prompts exist for 806/807/808 unless added by this change.
- [ ] 7.5 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 7.6 Run `openspec validate --all`.
