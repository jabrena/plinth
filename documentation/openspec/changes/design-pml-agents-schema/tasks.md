## 1. Schema Design

- [ ] 1.1 Draft `agent.xsd` covering `<agent-inventory>` (metadata, ordered entries with `@id`, `@file`, optional `@readonly`, `<summary>`) and `<agent>` body superset with required `<frontmatter>` only.
- [ ] 1.2 Define optional body elements (`<missions>`, `<role-boundaries>`, `<responsibilities>`, `<routing>`, `<output-format>`, `<safeguards>`, etc.) without a global `<missions>` requirement.
- [ ] 1.3 Define namespace `https://jabrena.github.io/pml/schemas/agent/0.9.0` and `xsi:schemaLocation` conventions pointing to local `agent.xsd`.

## 2. PML Alignment and Documentation

- [ ] 2.1 Document inventory vs agent-body elements, frontmatter mapping, optional section superset, and per-agent-shape XSLT mapping notes.
- [ ] 2.2 Document the relationship to `pml.xsd` (skill `<prompt>` parallel family) and `pml-workflow.xsd` (orthogonal workflow graphs).
- [ ] 2.3 Document that behavioral contracts remain in `analysis-design-agents`, `AgentIndexesTest`, and Gherkin — not in XSD profiles.

## 3. Examples and Migration

- [x] 3.1 Add valid XML examples for all nine agents from `.cursor/agents/` under `examples/xml/` (`robot-*.xml` plus source `robot-*.md` copies under `examples/md/`).
- [x] 3.2 Replace invalid examples with XSD-only failures (e.g. missing frontmatter fields, duplicate inventory `@id`).
- [ ] 3.3 Update migration notes with agent-by-agent slice order and XSD-only phased rollout.

## 4. Publication and Validation

- [ ] 4.1 Add `agent.xsd` under `plinth-agents-generator/src/main/resources/pml/schemas/agent/0.9.0/` (local hosting this iteration; external PML publication deferred).
- [x] 4.2 Validate example XML with `xmllint --noout --schema` against draft `agent.xsd` (all nine `robot-*.xml` files under `examples/xml/`).
- [ ] 4.3 Run `openspec validate --all` and resolve any OpenSpec validation failures for this change.
