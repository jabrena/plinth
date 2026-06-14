# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review GitHub issue #855 comment listing the GenAI Regulatory Stack (EU).
- [x] 1.2 Confirm `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr` are already covered and the Digital Markets Act remains pending.
- [x] 1.3 Create a per-regulation OpenSpec change for the Digital Markets Act.
- [x] 1.4 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [x] 1.5 Add `skills-generator/src/main/resources/skill-indexes/808-skill.xml`.
- [x] 1.6 Add `skills-generator/src/main/resources/skill-references/808-regulations-eu-digital-markets-act-chapters-summary.xml` with direct official-source chapter links and article mapping in the same style as `804-regulations-eu-nis2-chapters-summary.xml`.
- [x] 1.7 Add `skills-generator/src/main/resources/skill-references/808-regulations-eu-digital-markets-act-engineering-examples.xml` with Java-focused examples and output guidance.
- [x] 1.8 Add `skills-generator/src/main/resources/skill-references/assets/reports/808-eu-digital-markets-act-engineering-review-report-template.md` with a potential violation or non-compliance table that includes reference area and associated official-source link columns.
- [x] 1.9 Include engineering controls for interoperability interfaces, data access APIs, consent and preference evidence, ranking and self-preferencing audit signals, business-user export workflows, anti-circumvention guardrails, access control, observability, change control, documentation, and compliance evidence handoff.
- [x] 1.10 Update `808-skill.xml` so the workflow reads chapter summary, engineering examples, and report template before implementation review.
- [x] 1.11 Register skill id `808` with explicit `skillId="808-regulations-eu-digital-markets-act"`, references, and report template resource in `skills-generator/src/main/resources/skills.xml`.
- [x] 1.12 Add `skills-generator/src/test/resources/gherkin/808-regulations-eu-digital-markets-act.feature` with pull-request and direct-to-main acceptance scenarios modeled after `801-804`.
- [x] 1.13 Ensure the Gherkin scenarios require reading the chapters summary, engineering examples, and report template, and require linked violation mapping in generated reports.
- [x] 1.14 Add Digital Markets Act report examples under `examples/regulations/eu-digital-markets-act`.
- [x] 1.15 Add `808-regulations-eu-digital-markets-act` to `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`.
- [x] 1.16 Validate changed XML files with `xmllint --noout`.
- [x] 1.17 Run `./mvnw clean install -pl skills-generator`.
- [x] 1.18 Inspect generated local `.agents/skills/808-regulations-eu-digital-markets-act/SKILL.md`.
- [x] 1.19 Inspect generated local chapters summary, engineering examples, and report template outputs.
- [x] 1.20 Execute the listed `808-regulations-eu-digital-markets-act` acceptance prompt and verify it passes.
  - Manual coverage recorded with `examples/regulations/eu-digital-markets-act/DMA-ENGINEERING-REVIEW-REPORT.md` and `examples/regulations/eu-digital-markets-act/DMA-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md`; no automated acceptance runner exists beyond the documented prompt inventory.
- [x] 1.21 Run `./mvnw clean verify -pl skills-generator`.
- [x] 1.22 Run `openspec validate --all`.
