# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review GitHub issue #855 comment listing the GenAI Regulatory Stack (EU).
- [x] 1.2 Confirm `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr` are already covered and Digital Omnibus remains pending as a cross-cutting simplification item.
- [x] 1.3 Create a per-regulation OpenSpec change for Digital Omnibus.
- [x] 1.4 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-indexes/809-skill.xml`.
- [ ] 1.6 Add `skills-generator/src/main/resources/skill-references/809-regulations-eu-digital-omnibus.xml`.
- [ ] 1.7 Include engineering controls for source-status checks, affected-regulation mapping, evidence inventory updates, change-control impacts, questionnaire or report-template update candidates, incident-reporting workflow consolidation, data-rights workflow impacts, AI governance timeline changes, compatibility with existing regulation skills, and escalation when proposal-stage language is ambiguous.
- [ ] 1.8 Register skill id `809` and reference `809-regulations-eu-digital-omnibus` in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.9 Validate changed XML files with `xmllint --noout`.
- [ ] 1.10 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.11 Inspect generated local `.agents/skills/809-regulations-eu-digital-omnibus/SKILL.md`.
- [ ] 1.12 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.13 Run `openspec validate --all`.
