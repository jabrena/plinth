## 1. Schema Design (agents parity)

- [x] 1.1 Author no-namespace `commands.xsd` at `plinth-commands-generator/src/main/resources/commands.xsd`.
- [x] 1.2 Keep inventory outside the body schema (`commands.xml` file list only).
- [x] 1.3 Define optional `@kind` (`standard`, `delivery`, `performance`, `cli`) and `@slash` on `<command>`.
- [x] 1.4 Document structural superset + behavioral tests (no Schematron; no namespaced `pml/` split XSDs).

## 2. Generator Pipeline

- [x] 2.1 Convert all twelve `commands/*.md` contracts to `commands/*.xml`.
- [x] 2.2 Author `command-to-markdown.xsl`.
- [x] 2.3 Add `CommandMarkdownRenderer` and `CommandMarkdownGenerator`.
- [x] 2.4 Wire `exec-maven-plugin` at `process-classes` (agents pattern).
- [x] 2.5 Update `CommandIndexes` for `.xml` inventory → `.md` mapping; remove hand-authored Markdown from `src/`.

## 3. Examples and Documentation

- [x] 3.1 Replace OpenSpec XSD mirror with `examples/xsd/pml/0.9.0/commands.xsd`.
- [x] 3.2 Update valid/invalid examples to no-namespace `commands.xsd`.
- [x] 3.3 Realign `design.md`, `proposal.md`, `spec.md`, `MIGRATION.md`, and `examples/README.md`.

## 4. Validation

- [x] 4.1 `./mvnw clean verify -pl plinth-commands-generator -am`
- [x] 4.2 `xmllint --noout --schema …/commands.xsd …/commands/*.xml`
- [x] 4.3 Confirm `CommandIndexesTest` passes against generated Markdown
