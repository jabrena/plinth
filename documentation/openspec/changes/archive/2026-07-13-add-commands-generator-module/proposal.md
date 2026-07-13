## Why

GitHub issue [#1035](https://github.com/jabrena/plinth/issues/1035) requests extracting embedded command sources from `plinth-skills-generator` into a dedicated `plinth-commands-generator` Maven module.

Command inventory, markdown assets, loader code, focused tests, and Gherkin acceptance coverage currently live inside `plinth-skills-generator`. That coupling makes command ownership unclear, duplicates generator concerns, and blocks independent command evolution planned for milestone v0.18.0 alongside the parallel agents extraction ([#1036](https://github.com/jabrena/plinth/issues/1036)).

## What Changes

- Add a `plinth-commands-generator` Maven module registered in the root reactor.
- Move `commands.xml`, `CommandIndexes.java`, `InventoryXmlLoader.java`, the 11 embedded command markdown assets, `CommandIndexesTest`, and command Gherkin features out of `plinth-skills-generator`.
- Add a one-way Maven dependency from `plinth-skills-generator` to `plinth-commands-generator`.
- Bridge command assets into `plinth-skills-generator` during `generate-resources` so skills `001-commands-inventory` and `004-commands-installation` continue to embed command content at build time.
- Add automated tests that prove command assets from `plinth-commands-generator` propagate into generated skills for `001` and `004`.
- Update contributor documentation (`AGENTS.md`, `DEVELOPER.md`) and affected Gherkin paths.

## Capabilities

### New Capabilities

- `plinth-commands-generator-module`: Dedicated Maven ownership for embedded command inventory, assets, loader code, tests, and the `generate-resources` bridge consumed by `plinth-skills-generator`.

### Modified Capabilities

- `analysis-design-commands`: Update command source ownership and validation expectations so command bundle requirements remain satisfied after the module extraction.

## Source and Derivation

- Source artifact: GitHub issue [#1035](https://github.com/jabrena/plinth/issues/1035).
- Related source artifacts:
  - [#1036](https://github.com/jabrena/plinth/issues/1036) — parallel agents module extraction pattern.
  - [#993](https://github.com/jabrena/plinth/issues/993) — future PML schema for commands (out of scope unless required for the move).
- Existing specification: `documentation/openspec/specs/analysis-design-commands/spec.md`.
- Current implementation anchors:
  - `plinth-skills-generator/src/main/resources/commands.xml`
  - `plinth-skills-generator/src/main/java/info/jab/pml/CommandIndexes.java`
  - `plinth-skills-generator/src/main/resources/skill-references/assets/commands/`
  - `plinth-skills-generator/src/test/java/info/jab/pml/CommandIndexesTest.java`
  - `plinth-skills-generator/src/test/resources/gherkin/commands/`
- Derivation direction: issue #1035 -> OpenSpec change artifacts -> `plinth-commands-generator` module implementation -> `plinth-skills-generator` bridge and validation.

## Change Boundary Assessment

This is one OpenSpec change because the requested outcome is atomic: isolate command ownership in `plinth-commands-generator` while preserving command bundle behavior, skill generation for `001` and `004`, and full-reactor green builds.

The change does not include PML schema design ([#993](https://github.com/jabrena/plinth/issues/993)), agents module extraction ([#1036](https://github.com/jabrena/plinth/issues/1036)), new command contracts, or intentional public `skills/` release promotion unless a separate release step is explicitly requested.

## Impact

- Root `pom.xml`, new `plinth-commands-generator` module, `plinth-skills-generator` build wiring, command-focused tests, skill Gherkin features for `001` and `004`, and contributor docs.
- Skills `001-commands-inventory` and `004-commands-installation` remain in `plinth-skills-generator` but consume bridged command assets rather than owning command sources directly.
- OpenSpec `analysis-design-commands` requirements must remain satisfied after source relocation.
