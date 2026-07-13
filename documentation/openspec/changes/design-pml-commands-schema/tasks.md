## 1. Schema Design

- [ ] 1.1 Draft `pml-command-inventory.xsd` covering `<command-inventory>`, metadata, ordered `<command>` entries, unique `@id`, `@file`, optional `@slash`, `<summary>`, and `<owning-agent>`.
- [ ] 1.2 Draft `pml-command.xsd` covering `<command>` with `<purpose>`, `<usage>`, `<accepted-inputs>`, `<owning-agent>`, `<associated-skills>`, `<workflow>`, `<output>`, and `<safeguards>`.
- [ ] 1.3 Extract shared types into `pml-command-types.xsd` if needed to avoid duplication between inventory and body schemas.
- [ ] 1.4 Define namespace `https://jabrena.github.io/pml/schemas/command/1.0.0` and schema location conventions for inventory and body documents.

## 2. PML Alignment and Documentation

- [ ] 2.1 Write a schema design document describing inventory vs command-body elements, section normalization rules, and Markdown generation mapping.
- [ ] 2.2 Document the relationship to `pml.xsd` (skill `<prompt>` parallel family) and `pml-workflow.xsd` (orthogonal workflow graphs).
- [ ] 2.3 Align required command sections with `documentation/openspec/specs/analysis-design-commands/spec.md` behavioral fields and the twelve contracts under `plinth-commands-generator/src/main/resources/commands/`.

## 3. Examples and Migration

- [ ] 3.1 Add a valid XML example pair for `update-issue` (inventory entry + command body) under `documentation/openspec/changes/design-pml-commands-schema/examples/`.
- [ ] 3.2 Add at least one invalid XML example demonstrating a documented schema failure (e.g. missing `<workflow>` or empty `<safeguards>`).
- [ ] 3.3 Publish migration notes covering phased adoption from Markdown-first sources to XSD-validated XML without implementing XSLT or generator changes in this change.

## 4. Publication and Validation

- [ ] 4.1 Publish or prepare publication of the command XSD artifacts in the PML project (or document the exact target path and version if external publication is blocked).
- [ ] 4.2 Validate example XML with `xmllint --noout --schema` against the draft XSDs.
- [ ] 4.3 Run `openspec validate --all` and resolve any OpenSpec validation failures for this change.
