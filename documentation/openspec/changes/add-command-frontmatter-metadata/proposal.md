## Why

Generated Plinth command Markdown currently begins with the command heading and omits the YAML frontmatter required for consistent Cursor command metadata. Issue [#1075](https://github.com/jabrena/plinth/issues/1075) establishes that the XML source model, generator, and embedded installer assets must carry that metadata without changing the existing command body.

## What Changes

- Extend the command XML contract so each command can declare a required `metadata` element, including a structured `tools/tools-list/tool` list used to render YAML frontmatter.
- Emit valid YAML frontmatter before the existing Markdown heading for every command declared by the command inventory.
- Preserve the existing Markdown command content after the generated frontmatter.
- Propagate the generated command files through the existing Maven bridge into `.agents/skills/004-commands-installation/assets/commands`.
- Add schema, rendering, approved-metadata, inventory-coverage, propagation, and byte-for-byte body-preservation verification.
- Preserve XML tool-list order in the generated YAML sequence and validate the resulting frontmatter with a test-scoped YAML 1.2 parser.
- No command-body or file-name breaking change is intended; the prepended metadata is guaranteed for Cursor compatibility only.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `pml-commands-schema`: Add structured command metadata to the authoritative XML source contract and generated YAML-frontmatter behavior.
- `commands-generator-module`: Require the complete frontmatter-enabled command bundle to propagate into the generated `004-commands-installation` skill assets.

## Impact

- **Source model:** `plinth-commands-generator/src/main/resources/commands.xsd` and every XML command source in the inventory.
- **Rendering:** `command-to-markdown.xsl` and command renderer/generator tests.
- **Skill packaging:** the existing `plinth-skills-generator` command bridge, `skills.xml` resource mappings, and generated `004-commands-installation/assets/commands` verification.
- **Build:** `./mvnw clean verify -pl plinth-skills-generator -am` remains the integrated verification command.
- **Migration:** all existing command XML documents must acquire the agreed metadata while preserving their generated Markdown bodies.
- **Cross-tool compatibility:** the frontmatter format is based on the Cursor-compatible structure approved in issue #1075; no claim is made that every field is portable to every command framework.

## Source Artifacts and Derivation

| Source | Authority | Derivation direction |
|---|---|---|
| [Issue #1075](https://github.com/jabrena/plinth/issues/1075) | Problem, value, proposed frontmatter structure, and inventory-wide scope | Issue to OpenSpec |
| [Functional Specification comment](https://github.com/jabrena/plinth/issues/1075#issuecomment-5051702405) | Root cause, constraints, context, assumptions, and quality priorities | Comment to OpenSpec |
| [Acceptance Criteria comment](https://github.com/jabrena/plinth/issues/1075#issuecomment-5051757491) | Approved observable build and packaging behavior | Comment to OpenSpec |
| Current command XML, XSD, XSLT, and Maven bridge | Existing implementation boundaries and compatibility baseline | Repository to OpenSpec design |

This is one-way derivation. This change does not update or synchronize the source issue or its comments.

## Questions and Conflict Resolution

- The issue shows all YAML frontmatter fields, while the Functional Specification leaves XML required/optional/default cardinalities unresolved. The design resolves this by requiring the complete XML `metadata` subtree for every command so partial metadata cannot pass schema validation.
- The Functional Specification mentions direct `.cursor/commands` consumption, while the later approved acceptance criteria establishes the build boundary at `.agents/skills/004-commands-installation/assets/commands`. The later acceptance criteria governs this change; installation into a selected tool directory remains the installer skill's responsibility.
- The Functional Specification leaves global versus command-specific tool values unresolved. The design resolves this by requiring each command source to declare its own repeatable `tools/tools-list/tool` entries without assuming that all commands use different values.
- Exact per-command descriptions, argument hints, model, agent, and ordered tool values require maintainer approval during migration and are verified against each command source.
- Generated frontmatter is parsed in tests with a test-scoped YAML 1.2 parser; this introduces no production runtime dependency.
