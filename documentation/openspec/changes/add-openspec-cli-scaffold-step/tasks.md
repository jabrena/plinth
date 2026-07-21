# Tasks

## 1. Update the skill workflow sources

- [ ] 1.1 Update `plinth-skills-generator/src/main/resources/skill-references/042-planning-openspec.xml` Step 3 content to run `openspec new change <change-id>` for a new change before authoring artifacts, and to note that an existing change (detected via `openspec show <change-id>`) skips this step.
- [ ] 1.2 Update `plinth-skills-generator/src/main/resources/skill-references/042-planning-openspec.xml` Step 4 content to remove the CLI-generated placeholder `README.md` once `proposal.md` is authored.
- [ ] 1.3 Add a step constraint (or extend an existing one) in `042-planning-openspec.xml` capturing "MUST scaffold new changes with `openspec new change <change-id>`" and "MUST NOT re-run it for an existing change".
- [ ] 1.4 Update `plinth-skills-generator/src/main/resources/skill-indexes/042-skill.xml` Step 3/Step 4 summaries and constraint list to match.
- [ ] 1.5 Validate both edited XML files with `xmllint --noout`.

## 2. Add acceptance coverage and documentation

- [ ] 2.1 Add a scenario to `plinth-skills-generator/src/test/resources/gherkin/skills/042-planning-openspec.feature` covering: scaffolding a new change with `openspec new change <change-id>`, the resulting `.openspec.yaml`, and removal of the placeholder `README.md`; and a scenario covering that an existing change is not re-scaffolded.
- [ ] 2.2 Confirm `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` still has a correct `042-planning-openspec` entry (update only if the prompt wording needs to change).
- [ ] 2.3 Update the `042-planning-openspec` row in `documentation/guides/INVENTORY-SKILLS-JAVA.md` to note that new changes are scaffolded via `openspec new change <change-id>`.

## 3. Generate and verify

- [ ] 3.1 Run `./mvnw clean install -pl plinth-skills-generator -am` and verify `.agents/skills/042-planning-openspec/SKILL.md` plus `.agents/skills/042-planning-openspec/references/042-planning-openspec.md` contain the new scaffolding guidance with no unresolved include markers or broken local paths.
- [ ] 3.2 Execute the `042-planning-openspec` acceptance prompt listed in `acceptance-tests-prompts-skills.md` against the generated skill output, or record a skipped prompt with reason.
- [ ] 3.3 Run `./mvnw clean verify -pl plinth-skills-generator`.
- [ ] 3.4 Run `jbang markdown-validator/src/main/java/info/jab/mv/MarkdownValidator.java .` and inspect changed local links in the Java skill inventory.
- [ ] 3.5 Run `openspec validate --all` from `documentation/` and resolve validation failures before implementation is promoted.
- [ ] 3.6 Review `git diff` to confirm `.cursor/rules/`, `.agents/skills/`, public `skills/`, and `docs/` were not directly edited, and that issue #1054 remains a one-way source.
