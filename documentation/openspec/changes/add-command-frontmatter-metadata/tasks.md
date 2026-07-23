## 1. Command Metadata Contract

- [ ] 1.1 Review, record, and obtain maintainer approval for the `description`, `argument-hint`, `model`, `agent`, and ordered command-specific tool values for all fourteen entries in `plinth-commands-generator/src/main/resources/commands.xml`.
- [x] 1.2 Extend `plinth-commands-generator/src/main/resources/commands.xsd` with the required agents-style `metadata` subtree and the repeating `tools/tools-list/tool` structure.
- [ ] 1.3 Add the approved YAML-frontmatter values to each XML `metadata` element under `plinth-commands-generator/src/main/resources/commands/` without changing its narrative command contract.
- [x] 1.4 Add automated XSD contract tests that validate all inventoried sources and prove that missing required fields and zero-tool metadata fail while one-tool and multiple-tool metadata pass.
- [x] 1.5 Run `xmllint --noout` and schema validation for the updated XSD and all inventoried command XML sources before changing rendering.

## 2. Frontmatter Rendering

- [x] 2.1 Obtain the repository-required maintainer approval before modifying `plinth-commands-generator/src/main/resources/command-to-markdown.xsl`.
- [x] 2.2 Update `command-to-markdown.xsl` to use one named YAML-scalar template, emit safely single-quoted values with embedded apostrophes doubled, and map each `tools/tools-list/tool` value to the YAML sequence in XML document order.
- [x] 2.3 Preserve the existing rendering of `goal`, `constraints`, `steps`, `output-format`, and `safeguards` byte-for-byte after the closing YAML delimiter and its required separator.
- [x] 2.4 Add SnakeYAML Engine as a test-scoped YAML 1.2 parser without adding a production runtime dependency.
- [x] 2.5 Add focused `plinth-commands-generator` tests that parse and compare all approved XML-metadata/YAML-frontmatter values, validate punctuation and whitespace boundaries, assert ordered one-tool and multiple-tool mappings, cover the complete inventory, and compare the post-frontmatter Markdown with the previous generated body byte-for-byte.

## 3. Installer Asset Propagation

- [x] 3.1 Extend `plinth-skills-generator` bridge and propagation tests to assert that every generated `004-commands-installation/assets/commands` file contains the corresponding frontmatter-enabled command output, including parsed metadata values and ordered tools.
- [x] 3.2 Update `CommandInstallerParityTest`, `CommandBridgeTest`, or `CommandSkillPropagationTest` as appropriate so missing, stale, or metadata-stripped command assets fail verification.
- [x] 3.3 Confirm that `001-commands-inventory` retains the same command names and order and that `004-commands-installation` retains the same fourteen asset file names.
- [x] 3.4 Verify that no generated command Markdown or `.agents/skills` output is edited directly and that the public `skills/` release output remains untouched.

## 4. Integrated Validation

- [x] 4.1 Run `./mvnw clean verify -pl plinth-commands-generator` and resolve schema, rendering, and command-contract failures.
- [x] 4.2 Run `./mvnw clean verify -pl plinth-skills-generator -am` and confirm the complete command generation and skill-packaging reactor succeeds.
- [x] 4.3 Use automated tests as the pass/fail gate, then spot-check representative files under `.agents/skills/004-commands-installation/assets/commands` for valid frontmatter, matching ordered XML tool entries, byte-for-byte preserved Markdown bodies, and exact filename parity.
- [x] 4.4 Execute only the listed `004-commands-installation.feature` acceptance prompt because that generated skill output changes; record the reason for any skipped prompt.
- [x] 4.5 Run `openspec validate --all` from `documentation/` and resolve every validation error before implementation promotion.
