## Why

Generated Plinth command Markdown currently begins with the command heading and omits the YAML frontmatter required for consistent Cursor command metadata. Issue [#1075](https://github.com/jabrena/plinth/issues/1075) establishes that the XML source model, generator, and embedded installer assets must carry that metadata without changing the existing command body.

## What Changes

- Extend the command XML contract so each command can declare frontmatter metadata, including a structured `tools/list-tools/tool` list.
- Emit valid YAML frontmatter before the existing Markdown heading for every command declared by the command inventory.
- Preserve the existing Markdown command content after the generated frontmatter.
- Propagate the generated command files through the existing Maven bridge into `.agents/skills/004-commands-installation/assets/commands`.
- Add schema, rendering, inventory-coverage, propagation, and content-preservation verification.
- No breaking consumer change is intended: the command body and file names remain stable, while metadata is prepended for Cursor compatibility.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `pml-commands-schema`: Add structured command-frontmatter metadata to the authoritative XML source contract and generated Markdown behavior.
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

- The issue shows all frontmatter fields, while the Functional Specification leaves XML required/optional/default cardinalities unresolved. The design resolves this by requiring the complete frontmatter subtree for every command so partial metadata cannot pass schema validation.
- The Functional Specification mentions direct `.cursor/commands` consumption, while the later approved acceptance criteria establishes the build boundary at `.agents/skills/004-commands-installation/assets/commands`. The later acceptance criteria governs this change; installation into a selected tool directory remains the installer skill's responsibility.
- The Functional Specification leaves global versus command-specific tool values unresolved. The design resolves this by requiring each command source to declare its own repeatable `tools/list-tools/tool` entries without assuming that all commands use different values.
- Exact per-command descriptions, argument hints, and tool values, plus the test-scope YAML validation mechanism, remain open for implementation review and are tracked in `design.md` and `tasks.md`.
