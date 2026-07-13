## 1. Schema Design

- [ ] 1.1 Draft `pml-agent-inventory.xsd` covering `<agent-inventory>`, metadata, ordered `<agent>` entries, unique `@id`, `@file`, optional `@readonly`, and `<summary>`.
- [ ] 1.2 Draft `pml-agent.xsd` covering `<agent>` with `<frontmatter>`, `<identity>`, `<missions>`, `<role-boundaries>`, `<output-format>`, and conditional `<routing>` / `<safeguards>` elements.
- [ ] 1.3 Extract shared types into `pml-agent-types.xsd` if needed to avoid duplication between inventory and body schemas.
- [ ] 1.4 Define namespace `https://jabrena.github.io/pml/schemas/agent/1.0.0` and schema location conventions for inventory and body documents.

## 2. PML Alignment and Documentation

- [ ] 2.1 Write a schema design document describing inventory vs agent-body elements, frontmatter mapping, and section normalization rules for Markdown generation.
- [ ] 2.2 Document the relationship to `pml.xsd` (skill `<prompt>` parallel family) and `pml-workflow.xsd` (orthogonal workflow graphs).
- [ ] 2.3 Align required agent sections with `documentation/openspec/specs/analysis-design-agents/spec.md` behavioral fields and the nine contracts under `plinth-agents-generator/src/main/resources/agents/`.

## 3. Examples and Migration

- [ ] 3.1 Add a valid XML example pair for `robot-business-analyst` (inventory entry + agent body) under `documentation/openspec/changes/design-pml-agents-schema/examples/`.
- [ ] 3.2 Add at least one invalid XML example demonstrating a documented schema failure (e.g. missing `<missions>` or empty `<role-boundaries>`).
- [ ] 3.3 Publish migration notes covering phased adoption from Markdown-first sources to XSD-validated XML without implementing XSLT or generator changes in this change.

## 4. Publication and Validation

- [ ] 4.1 Publish or prepare publication of the agent XSD artifacts in the PML project (or document the exact target path and version if external publication is blocked).
- [ ] 4.2 Validate example XML with `xmllint --noout --schema` against the draft XSDs.
- [ ] 4.3 Run `openspec validate --all` and resolve any OpenSpec validation failures for this change.
