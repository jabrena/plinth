# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review GitHub issue #855 comment listing the GenAI Regulatory Stack (EU).
- [x] 1.2 Confirm `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr` are already covered and the Data Act remains pending.
- [x] 1.3 Create a per-regulation OpenSpec change for the Data Act.
- [x] 1.4 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-indexes/806-skill.xml`.
- [ ] 1.6 Add `skills-generator/src/main/resources/skill-references/806-regulations-eu-data-act-chapters-summary.xml` with direct official-source chapter links and article mapping in the same style as `804-regulations-eu-nis2-chapters-summary.xml`.
- [ ] 1.7 Add `skills-generator/src/main/resources/skill-references/806-regulations-eu-data-act-engineering-examples.xml` with Java-focused examples and output guidance.
- [ ] 1.8 Add `skills-generator/src/main/resources/skill-references/assets/reports/806-eu-data-act-engineering-review-report-template.md` with a potential violation or non-compliance table that includes reference area and associated official-source link columns.
- [ ] 1.9 Include engineering controls for data inventory, access authorization, portability APIs, export formats, interoperability, metadata, audit logs, cloud-switching support, non-personal data safeguards, trade-secret or sensitive-data handoff, data-sharing request workflows, contract evidence, and operational controls for data access requests.
- [ ] 1.10 Update `806-skill.xml` so the workflow reads chapter summary, engineering examples, and report template before implementation review.
- [ ] 1.11 Register skill id `806` with explicit `skillId="806-regulations-eu-data-act"`, references, and report template resource in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.12 Add `skills-generator/src/test/resources/gherkin/806-regulations-eu-data-act.feature` with pull-request and direct-to-main acceptance scenarios modeled after `801-804`.
- [ ] 1.13 Ensure the Gherkin scenarios require reading the chapters summary, engineering examples, and report template, and require linked violation mapping in generated reports.
- [ ] 1.14 Add Data Act report examples under `examples/regulations/eu-data-act`.
- [ ] 1.15 Add `806-regulations-eu-data-act` to `skills-generator/src/test/resources/gherkin/acceptance-tests-prompts-skills.md`.
- [ ] 1.16 Validate changed XML files with `xmllint --noout`.
- [ ] 1.17 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.18 Inspect generated local `.agents/skills/806-regulations-eu-data-act/SKILL.md`.
- [ ] 1.19 Inspect generated local chapters summary, engineering examples, and report template outputs.
- [ ] 1.20 Execute the listed `806-regulations-eu-data-act` acceptance prompt and verify it passes.
- [ ] 1.21 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.22 Run `openspec validate --all`.
