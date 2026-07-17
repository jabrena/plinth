## Why

GitHub issue [#993](https://github.com/jabrena/plinth/issues/993) requests an XSD-backed command schema and generator pipeline. After the agents generator shipped the no-namespace `agents.xsd` + XML sources + XSLT Markdown pattern, the commands module must match that layout instead of a namespaced split under `pml/schemas/command/`.

## What Changes

- Author single no-namespace `commands.xsd` at `plinth-commands-generator/src/main/resources/`.
- Convert twelve command Markdown contracts to `commands/*.xml` validated against `commands.xsd`.
- Add `command-to-markdown.xsl`, `CommandMarkdownRenderer`, and `CommandMarkdownGenerator`; wire `exec-maven-plugin` at `process-classes`.
- Update `CommandIndexes` so inventory `@file` entries are `.xml` and `commandFiles()` maps to generated `.md`.
- Remove hand-authored `commands/*.md` and the incorrect `pml/` namespaced schema tree.
- Realign this OpenSpec change (design, spec, migration, examples) to the agents-parity model.

## Capabilities

### New Capabilities

- `pml-commands-schema`: Defines the no-namespace command body XSD, inventory/file layout, XSLT Markdown generation, examples, and migration guidance aligned with `plinth-agents-generator`.

### Modified Capabilities

None. Command behavioral contracts in `analysis-design-commands` remain authoritative; this change structures and generates the same contracts.

## Impact

`plinth-commands-generator` becomes XML-source-of-truth for command contracts. Installers continue to consume generated Markdown on the classpath (`target/classes/commands/*.md`). Public `skills/` release output is not refreshed in this change.

## Source Artifacts and Derivation

| Source | Authority | Derivation |
|--------|-----------|------------|
| [Issue #993](https://github.com/jabrena/plinth/issues/993) | Problem, scope, acceptance criteria | OpenSpec + generator implementation |
| Shipped `plinth-agents-generator` | Reference layout | commands.xsd / XSLT / Indexes / pom wiring |
| Prior `commands/*.md` | Behavioral content | XML conversion fidelity |
| `CommandIndexesTest` | Routing substring contracts | Generated Markdown must preserve strings |

## Unresolved Questions

| Question | Decision | Status |
|----------|----------|--------|
| Namespaced split XSDs under `pml/schemas/command/`? | **Rejected** — agents-parity single `commands.xsd` | **Resolved** |
| Inventory XSD? | **No** — `commands.xml` outside body schema (like agents) | **Resolved** |
| Schematron? | **No** — kind profiles stay in tests | **Resolved** |
