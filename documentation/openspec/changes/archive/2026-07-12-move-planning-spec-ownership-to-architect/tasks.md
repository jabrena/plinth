## 1. Acceptance Contracts

- [ ] 1.1 Update `plinth-skills-generator/src/test/resources/gherkin/agents/robot-architect.feature` from `acceptance-tests/robot-architect.feature` so architect acceptance covers pre-implementation plan/OpenSpec creation and handoff to tech lead.
- [ ] 1.2 Update `plinth-skills-generator/src/test/resources/gherkin/agents/robot-tech-lead.feature` from `acceptance-tests/robot-tech-lead.feature` so tech lead acceptance starts from an approved execution artifact and routes planning/specification requests to architect.
- [ ] 1.3 Update `plinth-skills-generator/src/test/resources/gherkin/commands/create-spec.feature` from `acceptance-tests/create-spec.feature` so `/create-spec` routes OpenSpec creation through `@robot-architect`.

## 2. Canonical Source Assets

- [ ] 2.1 Update `plinth-skills-generator/src/main/resources/skill-references/assets/agents/robot-architect.md` with the planning/specification mission and handoff output.
- [ ] 2.2 Update `plinth-skills-generator/src/main/resources/skill-references/assets/agents/robot-tech-lead.md` to remove plan/OpenSpec creation as primary missions and require an approved execution artifact for delivery.
- [ ] 2.3 Update `plinth-skills-generator/src/main/resources/skill-references/assets/commands/create-spec.md` so the owning agent is `@robot-architect`.
- [ ] 2.4 Update related command, agent, inventory, and installer source references when they describe `/create-spec`, `robot-architect`, or `robot-tech-lead` ownership.

## 3. Documentation and Migration

- [ ] 3.1 Update getting-started guides and inventories to describe architect-owned pre-implementation planning/specification and tech-lead-owned delivery.
- [ ] 3.2 Update localized guide counterparts when English guide files change.
- [ ] 3.3 Update release-note or migration guidance for users who currently route `/create-spec` or direct plan/spec creation requests to `@robot-tech-lead`.

## 4. Generation and Validation

- [ ] 4.1 Validate edited XML sources with `xmllint --noout` when XML files are changed.
- [ ] 4.2 Regenerate local agent/command output with `./mvnw clean install -pl plinth-skills-generator`.
- [ ] 4.3 Run focused `plinth-skills-generator` verification or `./mvnw clean verify -pl plinth-skills-generator`.
- [ ] 4.4 Execute the affected acceptance prompts for changed generated agent or command outputs.
- [ ] 4.5 Run Markdown validation when documentation changes are included.
- [ ] 4.6 Run `openspec validate --all`.
