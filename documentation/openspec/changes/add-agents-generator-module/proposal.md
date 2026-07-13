## Why

GitHub issue [#1036](https://github.com/jabrena/plinth/issues/1036) requests extracting embedded robot agent sources from `plinth-skills-generator` into a dedicated `plinth-agents-generator` Maven module.

Agent markdown assets, inventory template, bundle tests, and Gherkin acceptance coverage currently live inside `plinth-skills-generator`. That coupling makes agent ownership unclear, duplicates generator concerns, and blocks independent agent evolution planned for milestone v0.18.0 alongside the completed commands extraction ([#1035](https://github.com/jabrena/plinth/issues/1035)).

## What Changes

- Add an `plinth-agents-generator` Maven module registered in the root reactor.
- Introduce `agents.xml` and `AgentIndexes.java`; move the nine embedded agent markdown assets, `java-agents-inventory-template.md`, `EmbeddedAgentBundleTests`, and agent Gherkin features out of `plinth-skills-generator`.
- Add a one-way Maven dependency from `plinth-agents-generator` to `plinth-commands-generator` to reuse `InventoryXmlLoader`.
- Add a one-way Maven dependency from `plinth-skills-generator` to `plinth-agents-generator`.
- Bridge agent assets into `plinth-skills-generator` during `generate-resources` so skills `002-agents-inventory` and `005-agents-installation` continue to embed agent content at build time.
- Add automated tests that prove agent assets from `plinth-agents-generator` propagate into generated skills for `002` and `005`.
- Update contributor documentation (`AGENTS.md`, `DEVELOPER.md`) and affected Gherkin paths.

## Capabilities

### New Capabilities

- `plinth-agents-generator-module`: Dedicated Maven ownership for embedded agent inventory, assets, loader code, tests, and the `generate-resources` bridge consumed by `plinth-skills-generator`.

### Modified Capabilities

- `analysis-design-agents`: Update agent source ownership and validation expectations so agent bundle and delegation requirements remain satisfied after the module extraction.

## Source and Derivation

- Source artifact: GitHub issue [#1036](https://github.com/jabrena/plinth/issues/1036).
- Related source artifacts:
  - [#1035](https://github.com/jabrena/plinth/issues/1035) — completed commands module extraction pattern to mirror.
  - [#993](https://github.com/jabrena/plinth/issues/993) — future PML schema work (out of scope unless required for the move).
  - OpenSpec `add-commands-generator-module` — reference bridge architecture and validation split.
- Existing specification: `documentation/openspec/specs/analysis-design-agents/spec.md`.
- Current implementation anchors:
  - `plinth-skills-generator/src/main/resources/skill-references/assets/agents/` (nine agent markdown files)
  - `plinth-skills-generator/src/main/resources/skill-references/assets/java-agents-inventory-template.md`
  - `plinth-skills-generator/src/test/java/info/jab/pml/SkillsGeneratorTest.java` (`EmbeddedAgentBundleTests`)
  - `plinth-skills-generator/src/test/resources/gherkin/agents/`
  - `plinth-skills-generator/src/main/resources/skill-references/002-agents-inventory.xml`
  - `plinth-skills-generator/src/main/resources/skill-references/005-agents-installation.xml`
- Derivation direction: issue #1036 -> OpenSpec change artifacts -> `plinth-agents-generator` module implementation -> `plinth-skills-generator` bridge and validation.

## Change Boundary Assessment

This is one OpenSpec change because the requested outcome is atomic: isolate agent ownership in `plinth-agents-generator` while preserving agent bundle behavior, skill generation for `002` and `005`, and full-reactor green builds.

The change does not include PML schema design ([#993](https://github.com/jabrena/plinth/issues/993)), new agent roles or delegation contract changes, skill `200-agents-md` relocation, or intentional public `skills/` release promotion unless a separate release step is explicitly requested.

## Impact

- Root `pom.xml`, new `plinth-agents-generator` module, `plinth-skills-generator` build wiring, agent-focused tests, skill Gherkin features for `002` and `005`, and contributor docs.
- Skills `002-agents-inventory` and `005-agents-installation` remain in `plinth-skills-generator` but consume bridged agent assets rather than owning agent sources directly.
- OpenSpec `analysis-design-agents` requirements must remain satisfied after source relocation.
