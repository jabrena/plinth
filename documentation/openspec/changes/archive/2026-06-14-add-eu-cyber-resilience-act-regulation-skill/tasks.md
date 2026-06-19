# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review GitHub issue #855 comment listing the GenAI Regulatory Stack (EU).
- [x] 1.2 Confirm `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr` are already covered and the Cyber Resilience Act remains pending.
- [x] 1.3 Create a per-regulation OpenSpec change for the Cyber Resilience Act.
- [x] 1.4 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 1.5 Add `skills-generator/src/main/resources/skill-indexes/805-skill.xml`.
- [x] 1.6 Add `skills-generator/src/main/resources/skill-references/805-regulations-eu-cyber-resilience-act-chapters-summary.xml` with direct official-source chapter links and article/annex mapping in the same style as `804-regulations-eu-nis2-chapters-summary.xml`.
- [x] 1.7 Add `skills-generator/src/main/resources/skill-references/805-regulations-eu-cyber-resilience-act-engineering-examples.xml` with Java-focused examples and output guidance.
- [x] 1.8 Add `skills-generator/src/main/resources/skill-references/assets/reports/805-eu-cyber-resilience-act-engineering-review-report-template.md` with a potential violation or non-compliance table that includes reference area and associated official-source link columns.
- [x] 1.9 Include engineering controls for secure-by-design development, threat modeling, secure defaults, vulnerability management, coordinated disclosure, security update mechanisms, dependency and SBOM evidence, cryptography, authentication and authorization, sensitive-data-safe logging, product security documentation, end-of-support signaling, and release readiness.
- [x] 1.10 Update `805-skill.xml` so the workflow reads chapter summary, engineering examples, and report template before implementation review.
- [x] 1.11 Register skill id `805` with explicit `skillId="805-regulations-eu-cyber-resilience-act"`, references, and report template resource in `skills-generator/src/main/resources/skills.xml`.
- [x] 1.12 Add `skills-generator/src/test/resources/gherkin/skills/805-regulations-eu-cyber-resilience-act.feature` with pull-request and direct-to-main acceptance scenarios modeled after `801-804`.
- [x] 1.13 Ensure the Gherkin scenarios require reading the chapters summary, engineering examples, and report template, and require linked violation mapping in generated reports.
- [x] 1.14 Add Cyber Resilience Act report examples under `examples/regulations/eu-cyber-resilience-act`.
- [x] 1.15 Add `805-regulations-eu-cyber-resilience-act` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [x] 1.16 Validate changed XML files with `xmllint --noout`.
- [x] 1.17 Run `./mvnw clean install -pl skills-generator`.
- [x] 1.18 Inspect generated local `.agents/skills/805-regulations-eu-cyber-resilience-act/SKILL.md`.
- [x] 1.19 Inspect generated local chapters summary, engineering examples, and report template outputs.
- [x] 1.20 Execute the listed `805-regulations-eu-cyber-resilience-act` acceptance prompt and verify it passes. Manual coverage recorded through `examples/regulations/eu-cyber-resilience-act/CRA-ENGINEERING-REVIEW-REPORT.md` and `examples/regulations/eu-cyber-resilience-act/CRA-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md`; no automated acceptance runner was found beyond the documented prompt.
- [x] 1.21 Run `./mvnw clean verify -pl skills-generator`.
- [x] 1.22 Run `openspec validate --all`.
