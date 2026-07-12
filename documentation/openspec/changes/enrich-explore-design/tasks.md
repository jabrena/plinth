## 1. Acceptance Contracts

- [ ] 1.1 Update `skills-generator/src/test/resources/gherkin/commands/create-spec.feature` from `acceptance-tests/create-spec.feature` so the `@acceptance-test` scenario verifies only `042-planning-openspec` behaviors and the `@integration-test` scenario covers create-spec-first skill routing.
- [ ] 1.2 Update `skills-generator/src/test/resources/gherkin/commands/explore-design.feature` from `acceptance-tests/explore-design.feature` so the `@acceptance-test` scenario verifies each associated design skill is called during elaboration and integration scenarios cover issue input and conditional feature-toggle invocation.
- [ ] 1.3 Update `skills-generator/src/test/resources/gherkin/agents/robot-architect.feature` from `acceptance-tests/robot-architect.feature` so one `@acceptance-test` covers initial OpenSpec creation with `042-planning-openspec` only and a second `@acceptance-test` creates an initial change then improves it through `/explore-design` with design-skill calls.

## 2. Canonical Source Assets

- [ ] 2.1 Update `skills-generator/src/main/resources/skill-references/assets/commands/create-spec.md` to retain only `042-planning-openspec`, remove design-skill workflow steps 3–11, and document first-step workflow position.
- [ ] 2.2 Update `skills-generator/src/main/resources/skill-references/assets/commands/explore-design.md` with target purpose, usage `<issue|openspec-change>`, accepted inputs including OpenSpec change, enriched associated skills, and post-create-spec workflow position.
- [ ] 2.3 Update `skills-generator/src/main/resources/skill-references/assets/agents/robot-architect.md` to decouple OpenSpec authoring from design refinement, remove skill `034` from mission 1, and assign design skills to the refinement step.
- [ ] 2.4 Delete skill `034` source assets: `skills-generator/src/main/resources/skill-indexes/034-skill.xml`, `skills-generator/src/main/resources/skill-references/034-architecture-design-exploration.xml` if present, and the `034` entry in `skills-generator/src/main/resources/skills.xml`.
- [ ] 2.5 Remove `034` from `skills-generator/src/test/resources/gherkin/skills/034-architecture-design-exploration.feature` and its entry in `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`.
- [ ] 2.6 Update related command, agent, skill inventory, installer, and README references that still route to or document `034-architecture-design-exploration`.

## 3. Documentation and Migration

- [ ] 3.1 Update getting-started guides and inventories under `documentation/guides/` to document create-spec-first and explore-design-second workflow.
- [ ] 3.2 Update localized guide counterparts when English guide files change.
- [ ] 3.3 Update `README.md`, `README_ES.md`, and `README_ZH.md` discoverability sections when command/skill lists change.
- [ ] 3.4 Add or update release-note or migration guidance for users who invoked design skills through `/create-spec` or skill `034` through `/explore-design`.

## 4. Generation and Validation

- [ ] 4.1 Validate edited XML sources with `xmllint --noout` when XML files are changed.
- [ ] 4.2 Regenerate local agent/command/skill output with `./mvnw clean install -pl skills-generator`.
- [ ] 4.3 Run focused `skills-generator` verification or `./mvnw clean verify -pl skills-generator`.
- [ ] 4.4 Execute affected acceptance prompts for changed generated command, agent, or skill outputs listed in prompt inventories.
- [ ] 4.5 Run Markdown validation when documentation changes are included.
- [ ] 4.6 Confirm no repository file still routes to or documents `034-architecture-design-exploration`.
- [ ] 4.7 Run `openspec validate --all`.
