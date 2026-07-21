# Tasks

## 1. OpenSpec Artifacts

- [x] 1.1 Scaffold the OpenSpec change with `openspec new change add-explore-problem-command`.
- [x] 1.2 Author `proposal.md` recording source issue #1043, the scope-discovery finding (all five `021`-`025` skills missing, not only `025`), and the change-boundary decision.
- [x] 1.3 Author `design.md` recording context, goals/non-goals, change boundary assessment, decisions (owning agent, skill band, orchestration split, output contract, clarifying-question trigger), compatibility review, validation strategy, risks, and open questions.
- [x] 1.4 Author spec deltas for `explore-problem-command` and `problem-exploration-technique-skill-references`.
- [x] 1.5 Replace the CLI-generated placeholder `README.md` once `proposal.md` is authored.

## 2. `/explore-problem` Command Contract

- [ ] 2.1 Add the canonical command asset `plinth-commands-generator/src/main/resources/commands/explore-problem.xml` with required argument `<issue-url>`, Owning Agent `@robot-business-analyst`, and Associated Skills `021`-`025` plus the existing `043-planning-github-issues`, `044-planning-jira`, and `045-planning-azure-devops` (reused, not new) for multi-tracker support.
- [ ] 2.2 Define the five-lens evaluation workflow: apply each of `021`-`025` in turn, evaluate available issue/User Story content against that lens's required fields, and ask clarifying questions only for vague/ambiguous fields before writing that section.
- [ ] 2.3 Define the Functional Specification output contract with the five required sections and field lists from `design.md`, delivered by posting a new comment on the issue at `<issue-url>` (not a repository file).
- [ ] 2.4 Define the confirm-before-posting step: present the full draft to the user and require explicit confirmation before posting the comment, mirroring `/update-issue`'s existing safeguard.
- [ ] 2.5 Provide explicit error handling for: missing `<issue-url>` argument and an unreadable/unresolvable issue URL.
- [ ] 2.6 Reference `/update-issue` (prior User Story input) and note `/create-gherkin` (future consumer) for pipeline context only, without introducing a hard dependency on either.
- [ ] 2.7 Support GitHub, Jira, and Azure DevOps `<issue-url>` values by tracker-URL shape, reusing `043`-`045` for reading the issue and posting the Functional Specification comment on the corresponding tracker.

## 3. `021-problem-framing` Skill

- [ ] 3.1 Add `plinth-skills-generator/src/main/resources/skill-indexes/021-skill.xml`.
- [ ] 3.2 Add `plinth-skills-generator/src/main/resources/skill-references/021-problem-framing.xml` covering Problem statement, Current state, Desired state, Stakeholders, Success criteria.
- [ ] 3.3 Model structure/format on `055-design-parallel-change` (skill-index + skill-reference shape) rather than the interactive `014-agile-user-story` Q&A shape, since the command owns clarifying-question orchestration.

## 4. `022-root-cause-analysis` Skill

- [ ] 4.1 Add `plinth-skills-generator/src/main/resources/skill-indexes/022-skill.xml`.
- [ ] 4.2 Add `plinth-skills-generator/src/main/resources/skill-references/022-root-cause-analysis.xml` covering Five Whys, Fishbone (Ishikawa), Current Reality Tree, and constraint identification.

## 5. `023-assumption-analysis` Skill

- [ ] 5.1 Add `plinth-skills-generator/src/main/resources/skill-indexes/023-skill.xml`.
- [ ] 5.2 Add `plinth-skills-generator/src/main/resources/skill-references/023-assumption-analysis.xml` covering explicit Assumptions, Unknowns, and Validation plan.

## 6. `024-context-mapping` Skill

- [ ] 6.1 Add `plinth-skills-generator/src/main/resources/skill-indexes/024-skill.xml`.
- [ ] 6.2 Add `plinth-skills-generator/src/main/resources/skill-references/024-context-mapping.xml` covering Existing systems, Integrations, Ownership, External dependencies.

## 7. `025-quality-attribute-discovery` Skill

- [ ] 7.1 Add `plinth-skills-generator/src/main/resources/skill-indexes/025-skill.xml`.
- [ ] 7.2 Add `plinth-skills-generator/src/main/resources/skill-references/025-quality-attribute-discovery.xml` covering quality-attribute identification and prioritization.
- [ ] 7.3 Explicitly note this skill precedes and feeds `030`-`032` ADR skills and `/explore-design`, without duplicating their content.

## 8. Bundle, Inventory, and Generator Registration

- [ ] 8.1 Register `/explore-problem` in the command inventory (`plinth-commands-generator/src/main/resources/commands.xml`) and the embedded installer/bundle.
- [ ] 8.2 Register skill ids `021`-`025` in `plinth-skills-generator/src/main/resources/skills.xml`.
- [ ] 8.3 Confirm local generation emits `.agents/skills/021-problem-framing` through `.agents/skills/025-quality-attribute-discovery` and the installed `/explore-problem` command, without touching public `skills/` unless a release is intentionally run later.

## 9. Tests and Acceptance Coverage

- [ ] 9.1 Add/update focused command-generator tests asserting the `/explore-problem` contract and installation (inventory, installer, contract), mirroring `close-spec` test coverage.
- [ ] 9.2 Add `plinth-commands-generator/src/test/resources/gherkin/commands/explore-problem.feature`.
- [ ] 9.3 Add `plinth-skills-generator/src/test/resources/gherkin/skills/02{1,2,3,4,5}-*.feature` for each new skill.
- [ ] 9.4 Register the new skill prompts in `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`, grouped by skill id, per `CLAUDE.md`'s skill-acceptance-prompt rule.
- [ ] 9.5 Update `documentation/guides/INVENTORY-COMMANDS-JAVA.md` and `documentation/guides/INVENTORY-SKILLS-JAVA.md` with rows for `/explore-problem` and `021`-`025`.

## 10. Validation

- [ ] 10.1 Validate all added/edited XML with `xmllint --noout`.
- [ ] 10.2 Run `./mvnw clean install -pl plinth-skills-generator -am` and inspect generated `.agents/skills/02{1..5}-*/SKILL.md`.
- [ ] 10.3 Run `./mvnw clean verify -pl plinth-commands-generator` and `./mvnw clean verify -pl plinth-skills-generator -am`.
- [ ] 10.4 Execute the listed acceptance prompts for `021`-`025` from `acceptance-tests-prompts-skills.md`.
- [ ] 10.5 Run `jbang markdown-validator/src/main/java/info/jab/mv/MarkdownValidator.java .` for changed Markdown/inventory docs.
- [ ] 10.6 Run `openspec validate --all` from `documentation/`.
