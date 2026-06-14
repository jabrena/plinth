# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review GitHub issue #855 comment listing the GenAI Regulatory Stack (EU).
- [x] 1.2 Confirm `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr` are already covered and NIS2 remains pending.
- [x] 1.3 Create a per-regulation OpenSpec change for NIS2.
- [x] 1.4 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 1.5 Add `skills-generator/src/main/resources/skill-indexes/804-skill.xml`.
- [x] 1.6 Add `skills-generator/src/main/resources/skill-references/804-regulations-eu-nis2-chapters-summary.xml`.
- [x] 1.7 Include engineering controls for cybersecurity risk management, asset and service inventory, dependency mapping, secure configuration, vulnerability handling, logging and monitoring, incident detection and escalation, backup and recovery, business continuity, supply-chain security, access control, cryptography, secure development, and change control.
- [x] 1.8 Register skill id `804` with explicit `skillId="804-regulations-eu-nis2"` and references in `skills-generator/src/main/resources/skills.xml`.
- [x] 1.9 Validate changed XML files with `xmllint --noout`.
- [x] 1.10 Run `./mvnw clean install -pl skills-generator`.
- [x] 1.11 Inspect generated local `.agents/skills/804-regulations-eu-nis2/SKILL.md`.
- [x] 1.12 Run `./mvnw clean verify -pl skills-generator`.
- [x] 1.13 Run `openspec validate --all`.
- [x] 1.14 Add `skills-generator/src/main/resources/skill-references/assets/reports/804-nis2-engineering-review-report-template.md`.
- [x] 1.15 Register the NIS2 report template as a skill resource in `skills-generator/src/main/resources/skills.xml`.
- [x] 1.16 Update `skills-generator/src/main/resources/skill-indexes/804-skill.xml` and NIS2 references so the skill reports conclusions and actions using the NIS2 engineering review report template.
- [x] 1.17 Add `skills-generator/src/test/resources/gherkin/804-regulations-eu-nis2.feature` with two acceptance-test scenarios modeled after the regulation skill scenarios for PR-based and direct-to-main delivery.
- [x] 1.18 Ensure the NIS2 Gherkin scenarios write reports under `examples/regulations/nis2`.
- [x] 1.19 Add `804-regulations-eu-nis2` to `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`.
- [x] 1.20 Re-run XML, generator, OpenSpec, and Markdown validation after adding report and acceptance artifacts.
- [x] 1.21 Decouple the NIS2 Directive article summary from Java engineering examples by moving examples into `skills-generator/src/main/resources/skill-references/804-regulations-eu-nis2-engineering-examples.xml`.
- [x] 1.22 Register and validate the generated local `references/804-regulations-eu-nis2-engineering-examples.md` output.
- [x] 1.23 Rename the NIS2 Directive summary output to `references/804-regulations-eu-nis2-chapters-summary.md` and add direct EUR-Lex chapter links.
- [x] 1.24 Remove the redundant primary `references/804-regulations-eu-nis2.md` router by using explicit skill id registration.
