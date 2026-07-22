## MODIFIED Requirements

### Requirement: Command-to-skill propagation verification

The build MUST prove that frontmatter-enabled command assets from `plinth-commands-generator` reach generated skills for `001-commands-inventory` and `004-commands-installation` without losing inventory coverage or command content.

#### Scenario: Generated 004 skill packages frontmatter-enabled command assets

- **GIVEN** `./mvnw clean verify -pl plinth-skills-generator -am` has been executed
- **WHEN** generated output under `.agents/skills/004-commands-installation` is inspected
- **THEN** `assets/commands` contains one Markdown asset for every command listed in `commands.xml`
- **AND** every embedded command asset begins with valid YAML frontmatter
- **AND** every embedded command retains its existing Markdown content after the frontmatter
- **AND** the asset file names and inventory order remain unchanged

#### Scenario: Generated 001 skill lists the command inventory

- **GIVEN** `./mvnw clean verify -pl plinth-skills-generator -am` has been executed
- **WHEN** generated output under `.agents/skills/001-commands-inventory` is inspected
- **THEN** `references/001-commands-inventory.md` lists every command row corresponding to `commands.xml`
- **AND** no command from `plinth-commands-generator` is missing from the generated `001` inventory or `004` installer assets

#### Scenario: Propagate command-specific tool metadata

- **GIVEN** an inventoried command source declares `tools/list-tools/tool` entries
- **WHEN** the generated `004-commands-installation` asset for that command is inspected
- **THEN** its YAML frontmatter contains the corresponding `tools` sequence entries
- **AND** the bridge does not replace them with a global or stale tool list

#### Scenario: Automated guard fails when frontmatter propagation breaks

- **GIVEN** `plinth-skills-generator` has generator tests for command-backed skills
- **WHEN** `./mvnw clean verify -pl plinth-skills-generator -am` is executed
- **THEN** the tests verify inventory parity, frontmatter presence and validity, command-specific tool propagation, and existing body preservation
- **AND** verification fails when an inventoried asset is removed, renamed, not staged, or loses its required frontmatter
