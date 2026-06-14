# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review GitHub issue #855 comment listing the GenAI Regulatory Stack (EU).
- [x] 1.2 Confirm `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr` are already covered and NIS2 remains pending.
- [x] 1.3 Create a per-regulation OpenSpec change for NIS2.
- [x] 1.4 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 1.5 Add `skills-generator/src/main/resources/skill-indexes/804-skill.xml`.
- [x] 1.6 Add `skills-generator/src/main/resources/skill-references/804-regulations-eu-nis2.xml`.
- [x] 1.7 Include engineering controls for cybersecurity risk management, asset and service inventory, dependency mapping, secure configuration, vulnerability handling, logging and monitoring, incident detection and escalation, backup and recovery, business continuity, supply-chain security, access control, cryptography, secure development, and change control.
- [x] 1.8 Register skill id `804` and reference `804-regulations-eu-nis2` in `skills-generator/src/main/resources/skills.xml`.
- [x] 1.9 Validate changed XML files with `xmllint --noout`.
- [x] 1.10 Run `./mvnw clean install -pl skills-generator`.
- [x] 1.11 Inspect generated local `.agents/skills/804-regulations-eu-nis2/SKILL.md`.
- [x] 1.12 Run `./mvnw clean verify -pl skills-generator`.
- [x] 1.13 Run `openspec validate --all`.
