## 1. Command Contract and Inventory

- [ ] 1.1 Add `plinth-commands-generator/src/main/resources/commands/add-acceptance-criteria.xml` with usage `/add-acceptance-criteria <issue-url>`, `@robot-business-analyst` ownership, `058-design-bdd` orchestration, and tracker-skill routing.
- [ ] 1.2 Implement Functional Specification comment discovery, missing/ambiguous-source handling, untrusted-data safeguards, focused clarification, complete-draft confirmation, and separate-comment publication in the command contract.
- [ ] 1.3 Define one common Markdown output with an acceptance-criteria heading and fenced `gherkin` Feature, without editing the issue description, Functional Specification comment, or prior acceptance comments.
- [ ] 1.4 Register the command in `plinth-commands-generator/src/main/resources/commands.xml` and update `plinth-commands-generator/src/main/resources/java-commands-inventory-template.md` plus `documentation/guides/INVENTORY-COMMANDS-JAVA.md`.
- [ ] 1.5 Validate edited command XML and inventory XML with `xmllint --noout`.

## 2. Focused Command and Propagation Tests

- [ ] 2.1 Extend `plinth-commands-generator/src/test/java/info/jab/pml/CommandIndexesTest.java` with routing and contract assertions for the new command.
- [ ] 2.2 Add `plinth-commands-generator/src/test/resources/gherkin/commands/add-acceptance-criteria.feature` covering successful generation/publication, missing and ambiguous Functional Specifications, behavior clarification, common cross-tracker Markdown, confirmation decline, unsupported URLs, and read/publication failures.
- [ ] 2.3 Register the new command feature in `plinth-commands-generator/src/test/resources/gherkin/commands/acceptance-tests-prompts-commands.md`.
- [ ] 2.4 Update `plinth-skills-generator/src/test/resources/gherkin/skills/001-commands-inventory.feature` and `004-commands-installation.feature` so generated command inventory and installation output include `add-acceptance-criteria.md`.
- [ ] 2.5 Update any command-to-skill propagation assertions affected by the expanded command inventory.

## 3. Validation

- [ ] 3.1 Run `./mvnw clean verify -pl plinth-commands-generator`.
- [ ] 3.2 Run `./mvnw clean install -pl plinth-skills-generator -am` and inspect `.agents/skills/001-commands-inventory` and `.agents/skills/004-commands-installation` for the generated command entry and embedded asset.
- [ ] 3.3 Execute only the listed acceptance prompts for changed generated skills `001-commands-inventory` and `004-commands-installation`, recording any skipped prompt with its reason.
- [ ] 3.4 Execute the listed `add-acceptance-criteria.feature` command acceptance prompt and verify the workflow against representative GitHub, Jira, and Azure DevOps cases or document unavailable external tracker environments.
- [ ] 3.5 Run `jbang markdown-validator/src/main/java/info/jab/mv/MarkdownValidator.java .` and inspect changed local links.
- [ ] 3.6 Run `openspec validate --all` from `documentation/` and resolve all validation errors before implementation promotion.
