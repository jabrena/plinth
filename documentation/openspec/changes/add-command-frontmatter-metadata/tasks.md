## 1. Command Metadata Contract

- [ ] 1.1 Review and approve the `description`, `argument-hint`, `model`, `agent`, and command-specific tool values for all fourteen entries in `plinth-commands-generator/src/main/resources/commands.xml`.
- [ ] 1.2 Extend `plinth-commands-generator/src/main/resources/commands.xsd` with the required `frontmatter` subtree and the repeating `tools/list-tools/tool` structure.
- [ ] 1.3 Add the approved frontmatter metadata to every XML source under `plinth-commands-generator/src/main/resources/commands/` without changing its narrative command contract.
- [ ] 1.4 Run `xmllint --noout` and schema validation for the updated XSD and all inventoried command XML sources before changing rendering.

## 2. Frontmatter Rendering

- [ ] 2.1 Obtain the repository-required maintainer approval before modifying `plinth-commands-generator/src/main/resources/command-to-markdown.xsl`.
- [ ] 2.2 Update `command-to-markdown.xsl` to emit valid YAML frontmatter before the command H1 and map each `tools/list-tools/tool` value to the YAML `tools` sequence.
- [ ] 2.3 Preserve the existing rendering of `goal`, `constraints`, `steps`, `output-format`, and `safeguards` after the closing YAML delimiter.
- [ ] 2.4 Add focused `plinth-commands-generator` tests that validate required frontmatter fields, YAML syntax, command-specific tool mapping, complete inventory coverage, and preservation of the pre-existing Markdown body.

## 3. Installer Asset Propagation

- [ ] 3.1 Extend `plinth-skills-generator` bridge and propagation tests to assert that every generated `004-commands-installation/assets/commands` file contains the corresponding frontmatter-enabled command output.
- [ ] 3.2 Update `CommandInstallerParityTest`, `CommandBridgeTest`, or `CommandSkillPropagationTest` as appropriate so missing, stale, or metadata-stripped command assets fail verification.
- [ ] 3.3 Confirm that `001-commands-inventory` retains the same command names and order and that `004-commands-installation` retains the same fourteen asset file names.
- [ ] 3.4 Verify that no generated command Markdown or `.agents/skills` output is edited directly and that the public `skills/` release output remains untouched.

## 4. Integrated Validation

- [ ] 4.1 Run `./mvnw clean verify -pl plinth-commands-generator` and resolve schema, rendering, and command-contract failures.
- [ ] 4.2 Run `./mvnw clean verify -pl plinth-skills-generator -am` and confirm the complete command generation and skill-packaging reactor succeeds.
- [ ] 4.3 Inspect every file under `.agents/skills/004-commands-installation/assets/commands` for valid frontmatter, matching XML tool entries, unchanged Markdown bodies, and exact inventory parity.
- [ ] 4.4 Execute only the listed `004-commands-installation.feature` acceptance prompt because that generated skill output changes; record the reason for any skipped prompt.
- [ ] 4.5 Run `openspec validate --all` from `documentation/` and resolve every validation error before implementation promotion.
