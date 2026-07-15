## 1. Schema `agents.xsd` (PML-aligned body)

- [x] 1.1 Draft provisional schema under OpenSpec `examples/xsd/pml/0.9.0/agent.xsd` with root `<agent @id>` and PML-shaped children reused from staged `pml.xsd` 0.9.0: optional `<metadata>`, `<role>`, `<goal>`, `<constraints>`, `<steps>`, `<output-format>`, `<safeguards>`.
- [x] 1.2 Confirm the nine exploration agents share that shape: Markdown mission/responsibility/routing content stays inside `<goal>` CDATA (and optional structured `<constraints>` / `<output-format>` / `<safeguards>`).
- [ ] 1.3 Promote the provisional draft to module schema `plinth-agents-generator/src/main/resources/agents.xsd` (rename from `agent.xsd`); keep OpenSpec examples in sync or point them at the module schema.
- [ ] 1.4 Finalize required vs optional policy for production (today only `@id` is required; evaluate whether `<metadata>` title/description and `<role>` become required) and document inventory handling (`agents.xml` vs schema scope).

## 2. Align OpenSpec design artifacts to the delivery model

- [ ] 2.1 Update `design.md` and `proposal.md`: XML agent sources live under `plinth-agents-generator/src/main/resources/agents/`; validate with `agents.xsd`; Java generates `.md` for the existing `plinth-skills-generator` bridge (no longer design-only / deferred generator).
- [ ] 2.2 Update `specs/pml-agents-schema/spec.md` scenarios for `agents.xsd`, XML-under-`agents/`, and Markdown generation consumed by skills-generator.
- [ ] 2.3 Document relationship to staged `pml.xsd` 0.9.0 (shared element shapes) and `pml-workflow.xsd` (orthogonal); agents remain a parallel family, not a `<prompt>` reuse.
- [ ] 2.4 Document that behavioral contracts remain in `analysis-design-agents`, `AgentIndexesTest`, and Gherkin — not in XSD profiles or `@kind` Schematron.

## 3. Examples and migration notes

- [x] 3.1 Keep OpenSpec exploration examples: nine `examples/xml/robot-*.xml` plus matching `examples/md/` Markdown references.
- [ ] 3.2 Author invalid XSD-failure examples (missing `@id`, disallowed child, malformed `<constraints>`) and record expected `xmllint` failures against `agents.xsd`.
- [ ] 3.3 Rewrite `MIGRATION.md`: Markdown-first `agents/*.md` → XML sources in `src/main/resources/agents/`; frontmatter → `<metadata>` + `@id`; body → `<role>` / `<goal>` / optional structured sections; generated `.md` remains the skill-bridge input; preserve agent-by-agent slice order.

## 4. Module sources: XML under `agents/`

- [ ] 4.1 Add the nine agent XML contracts to `plinth-agents-generator/src/main/resources/agents/` (`robot-*.xml`), starting from the OpenSpec examples.
- [ ] 4.2 Point each XML document at `agents.xsd` (local classpath / `xsi:noNamespaceSchemaLocation` or agreed equivalent) and validate with `xmllint --noout --schema`.
- [ ] 4.3 Update `agents.xml` inventory so `@file` (or equivalent) references XML sources during/after migration without breaking installer discovery.

## 5. Java Markdown generation for skills-generator

- [ ] 5.1 Add Java generation support in `plinth-agents-generator` that reads validated agent XML and emits Cursor-compatible agent Markdown (YAML frontmatter + body) per agent.
- [ ] 5.2 Wire Maven (`generate-resources` or equivalent) so generated `.md` lands where `plinth-skills-generator` already bridges from (`…/plinth-agents-generator/src/main/resources/agents` or an explicit generated output directory the bridge is updated to consume).
- [ ] 5.3 Keep `005-agents-installation` / skills XInclude path working: `plinth-skills-generator` continues to package generated agent Markdown as skill assets without editing `skills/` unless preparing a release.
- [ ] 5.4 Update `AgentIndexes` / `AgentIndexesTest` (and inventory template if needed) so tests assert against generated Markdown parity, not hand-edited Markdown as the sole source of truth.

## 6. Validation

- [x] 6.1 Validate all nine OpenSpec `examples/xml/robot-*.xml` against the provisional `examples/xsd/pml/0.9.0/agent.xsd`.
- [ ] 6.2 Run `./mvnw clean verify -pl plinth-agents-generator` (and `-pl plinth-skills-generator -am` when the bridge or generated assets change).
- [ ] 6.3 Run `openspec validate --all` and resolve any OpenSpec validation failures for this change.
