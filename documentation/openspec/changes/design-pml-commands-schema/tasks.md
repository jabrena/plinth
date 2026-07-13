## 1. Schema Design

- [ ] 1.1 Draft `pml-command-inventory.xsd` covering `<command-inventory>`, metadata, ordered `<command>` entries with `@id`, `@kind`, `@file`, optional `@slash`, `<summary>`, and `<owning-agent>`.
- [ ] 1.2 Draft `pml-command.xsd` as a structural superset for all `@kind` profiles (`standard`, `delivery`, `performance`, `cli`).
- [ ] 1.3 Extract shared types into `pml-command-types.xsd` (command id, association refs; optional alignment with `pml-core-types.xsd`).
- [ ] 1.4 Define namespace `https://jabrena.github.io/pml/schemas/command/1.0.0` and schema location conventions.
- [ ] 1.5 Draft `pml-command.sch` Schematron rules for `@kind` profiles, slash/id parity, and command-specific behavioral constraints.

## 2. PML Alignment and Documentation

- [ ] 2.1 Document inventory vs command-body elements, `@kind` taxonomy, association normalization, and kind-specific XSLT template strategy.
- [ ] 2.2 Document the relationship to `pml.xsd`, `pml-workflow.xsd`, and parallel agent schema conventions.
- [ ] 2.3 Map each of the twelve commands to a `@kind` profile and align with `analysis-design-commands` and `CommandIndexesTest` checks.

## 3. Examples and Migration

- [x] 3.1 Add a valid XML example pair for `update-issue` (`kind="standard"`) under `examples/`.
- [x] 3.2 Add at least one invalid XML example demonstrating a documented validation failure.
- [ ] 3.3 Add a valid XML example for `close-spec` (`kind="cli"`) and an invalid `cli` example with forbidden `<output>`.
- [ ] 3.4 Update migration notes with command-by-command slice order and Schematron/XSLT phased rollout.

## 4. Publication and Validation

- [ ] 4.1 Publish or prepare publication of XSD + Schematron artifacts in the PML project under `/schemas/command/1.0.0/`.
- [ ] 4.2 Validate example XML with `xmllint --schema` and Schematron tooling against draft rules.
- [ ] 4.3 Run `openspec validate --all` and resolve any OpenSpec validation failures for this change.
