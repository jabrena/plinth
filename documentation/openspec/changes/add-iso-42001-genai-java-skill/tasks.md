# Tasks

## 1. Implementation Checklist

- [ ] 1.1 Review issue #939 and identify ISO/IEC 42001 as scoped skill `813`.
- [ ] 1.2 Record source artifacts, derivation direction, scope boundary, and validation expectations.
- [ ] 1.3 Compare the existing `801-regulations-eu-ai-act` and current regulation skill pattern before implementing the new skill.
- [ ] 1.4 Add `skills-generator/src/main/resources/skill-indexes/813-skill.xml`.
- [ ] 1.5 Add `skills-generator/src/main/resources/skill-references/813-regulations-iso-42001-chapters-summary.xml` with issue-provided ISO/IEC 42001 source links and AI management system concept mapping.
- [ ] 1.6 Add `skills-generator/src/main/resources/skill-references/813-regulations-iso-42001-engineering-examples.xml` with Java GenAI engineering examples and output guidance.
- [ ] 1.7 Include engineering controls for hallucinated code, insecure generated implementation, generated dependency and supply-chain risk, IP leakage, confidentiality breach, regulatory non-compliance risk, and biased generated business logic.
- [ ] 1.8 Ensure the skill workflow reads the ISO/IEC 42001 summary and engineering examples before implementation review.
- [ ] 1.9 Ensure the skill clearly states that it provides engineering guidance, not legal advice, certification advice, audit conclusions, or final conformity decisions.
- [ ] 1.10 Register skill id `813` with explicit `skillId="813-regulations-iso-42001"` and references in `skills-generator/src/main/resources/skills.xml`.
- [ ] 1.11 Add `skills-generator/src/test/resources/gherkin/skills/813-regulations-iso-42001.feature` with acceptance and integration scenarios modeled after existing regulation skills.
- [ ] 1.12 Ensure the Gherkin scenarios require reading bundled references, recognizing GenAI-oriented Java development risks, and producing engineering evidence without legal, audit, or certification conclusions.
- [ ] 1.13 Add `813-regulations-iso-42001` to `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [ ] 1.14 Validate changed XML files with `xmllint --noout`.
- [ ] 1.15 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.16 Inspect generated local `.agents/skills/813-regulations-iso-42001/SKILL.md`.
- [ ] 1.17 Inspect generated local ISO/IEC 42001 summary and engineering examples outputs.
- [ ] 1.18 Execute the listed `813-regulations-iso-42001` acceptance prompt and verify it passes.
- [ ] 1.19 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.20 Run `openspec validate --all`.
