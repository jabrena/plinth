# Tasks

## 1. Implementation Checklist

### Step 1 — Behavior-preserving extraction

- [x] 1.1 Review issue [#1035](https://github.com/jabrena/plinth/issues/1035) and this change's refined design before editing code.
- [x] 1.2 Create `plinth-commands-generator` module (`artifactId` `plinth-commands-generator`, `groupId` `info.jab.pml`) and register it in the root `pom.xml`.
- [x] 1.3 Move `commands.xml`, `CommandIndexes.java`, `InventoryXmlLoader.java`, and the 11 command markdown assets to `plinth-commands-generator/src/main/resources/commands/`; make `InventoryXmlLoader` public.
- [x] 1.4 Move command contract tests and `plinth-skills-generator/src/test/resources/gherkin/commands/` into `plinth-commands-generator`; update asset classpath assertions from `skill-references/assets/commands/` to `commands/`.
- [x] 1.5 Add a one-way Maven dependency from `plinth-skills-generator` to `plinth-commands-generator`.
- [x] 1.6 Implement the `generate-resources` bridge in `plinth-skills-generator` using sibling-module copy into `target/generated-resources/` and expose staged files at `skill-references/assets/commands/` on the main classpath.
- [x] 1.7 Remove committed command markdown copies and `commands.xml` from `plinth-skills-generator` once the bridge is working.
- [x] 1.8 Add `CommandBridgeTest` and `CommandInstallerParityTest` in `plinth-skills-generator` for staged assets and `004-commands-installation.xml` XInclude parity against `CommandIndexes.commandFiles()`.
- [x] 1.9 Add or extend skill propagation tests so generated `001` and `004` references embed staged command content and fail when staging breaks.
- [x] 1.10 Update `004-commands-installation.xml` repository copy-path guidance to `plinth-commands-generator/src/main/resources/commands/` while preserving embedded reference fallback for skill-only installs.
- [x] 1.11 Update `001-commands-inventory.feature`, `004-commands-installation.feature`, and `acceptance-tests-prompts-commands.md` for relocated paths and generated-skill assertions.
- [x] 1.12 Update `AGENTS.md` and `DEVELOPER.md` with `plinth-commands-generator` commands and `./mvnw clean install -pl plinth-skills-generator -am`.
- [x] 1.13 Run `./mvnw clean verify -pl plinth-commands-generator`, `./mvnw clean install -pl plinth-skills-generator -am`, and `./mvnw clean verify`.

### Step 2 — Inventory template hardening

- [x] 2.1 Move `java-commands-inventory-template.md` ownership to `plinth-commands-generator` and bridge it into `skill-references/assets/` during `generate-resources`.
- [x] 2.2 Extend parity tests so `001` inventory output cannot drift from the manifest without failing the build.
- [x] 2.3 Re-run `./mvnw clean verify -pl plinth-skills-generator -am` and confirm generated `001` still lists every manifest command.

### Closeout

- [x] 3.1 Remove the `//TODO Move to plinth-commands-generator ASAP` comment after relocation.
- [x] 3.2 Run `openspec validate --all`.
