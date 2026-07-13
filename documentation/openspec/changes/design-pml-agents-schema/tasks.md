## 1. Schema Design

- [ ] 1.1 Draft `pml-agent-inventory.xsd` covering `<agent-inventory>`, metadata, ordered `<agent>` entries with `@id`, `@kind`, `@file`, optional `@readonly`, and `<summary>`.
- [ ] 1.2 Draft `pml-agent.xsd` as a structural superset for all `@kind` profiles (`analyst`, `architect`, `coordinator`, `performance`, `coder`).
- [ ] 1.3 Extract shared types into `pml-agent-types.xsd` (agent id, skill-ref, optional shared patterns with `pml-core-types.xsd`).
- [ ] 1.4 Define namespace `https://jabrena.github.io/pml/schemas/agent/1.0.0` and schema location conventions for inventory and body documents.
- [ ] 1.5 Draft `pml-agent.sch` Schematron rules for `@kind` profiles, id/name parity, and non-empty boundary/safeguard sections.

## 2. PML Alignment and Documentation

- [ ] 2.1 Document inventory vs agent-body elements, `@kind` taxonomy, frontmatter mapping, and kind-specific XSLT template strategy.
- [ ] 2.2 Document the relationship to `pml.xsd` (skill `<prompt>` parallel family) and `pml-workflow.xsd` (orthogonal workflow graphs).
- [ ] 2.3 Map each of the nine agents to a `@kind` profile and align required sections with `analysis-design-agents` and `AgentIndexesTest` behavioral checks.

## 3. Examples and Migration

- [x] 3.1 Add a valid XML example pair for `robot-business-analyst` (`kind="analyst"`) under `examples/`.
- [x] 3.2 Add at least one invalid XML example demonstrating a documented schema failure.
- [ ] 3.3 Add a valid XML example for `robot-tech-lead` (`kind="coordinator"`) and an invalid `kind="coder"` profile example.
- [ ] 3.4 Update migration notes with agent-by-agent slice order and Schematron/XSLT phased rollout.

## 4. Publication and Validation

- [ ] 4.1 Publish or prepare publication of XSD + Schematron artifacts in the PML project under `/schemas/agent/1.0.0/`.
- [ ] 4.2 Validate example XML with `xmllint --schema` and Schematron tooling against draft rules.
- [ ] 4.3 Run `openspec validate --all` and resolve any OpenSpec validation failures for this change.
