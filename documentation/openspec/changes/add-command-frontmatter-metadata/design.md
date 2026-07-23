## Context

The command pipeline currently treats XML under `plinth-commands-generator/src/main/resources/commands/` as authoritative, validates it with the no-namespace `commands.xsd`, renders Markdown with `command-to-markdown.xsl`, and bridges the generated files into the `004-commands-installation` skill. Generated command files currently start with `# <command-id>` and contain no YAML frontmatter.

Issue [#1075](https://github.com/jabrena/plinth/issues/1075), its complete Functional Specification, and its approved acceptance criteria require every inventoried command to carry Cursor-compatible frontmatter while preserving the existing Markdown body. The maintainer-confirmed build boundary is `.agents/skills/004-commands-installation/assets/commands` after `./mvnw clean verify -pl plinth-skills-generator -am`; installing those assets into a selected tool directory remains the responsibility of `004-commands-installation`.

## Goals / Non-Goals

**Goals:**

- Represent `description`, `argument-hint`, `model`, `agent`, and a repeatable tool list inside an agents-style `metadata` element in every authoritative command XML document.
- Render valid YAML frontmatter before the existing Markdown heading.
- Map XML `tools/tools-list/tool` entries to YAML `tools` sequence entries.
- Preserve XML tool order in the generated YAML sequence.
- Preserve every command's existing Markdown content byte-for-byte after the frontmatter.
- Verify that every generated command contains the maintainer-approved metadata declared by its XML source.
- Package one frontmatter-enabled asset for every inventoried command in the generated `004-commands-installation` skill.
- Verify schema validity, rendering, inventory coverage, propagation, and body preservation through the existing Maven reactor build.

**Non-Goals:**

- Change command file names, inventory order, workflow behavior, ownership, routing, safeguards, or Markdown body semantics.
- Change the interactive installation behavior of `004-commands-installation`.
- Claim that Cursor frontmatter fields have identical semantics in every supported command framework.
- Refresh public `skills/`; local generation continues to target `.agents/skills` unless the release profile is explicitly selected.
- Select a new command architecture or replace the existing XML → XSLT → Markdown pipeline.

## Change Boundary Assessment

This is one atomic change. Schema representation, YAML-frontmatter rendering, XML-source migration, and installer-asset propagation share one user-visible outcome, build, ownership boundary, and rollback path. Splitting the work by XSD, XSLT, or Maven module would create technical-layer changes with no independently useful result.

## Decisions

### Add a required XML metadata subtree before the narrative contract

`commands.xsd` will follow the `agents.xsd` convention by extending the root command sequence with a required global `metadata` element reference before `goal`. Its children are composed through global element references and use this source shape:

```xml
<metadata>
    <description>One sentence describing what this command does.</description>
    <argument-hint>[optional arguments]</argument-hint>
    <model>inherit</model>
    <agent>robot-architect</agent>
    <tools>
        <tools-list>
            <tool>Read</tool>
            <tool>Edit</tool>
        </tools-list>
    </tools>
</metadata>
```

All five metadata fields are required because the issue requires every generated command to follow the approved structure. `tools-list` contains one or more `tool` elements so the XML model represents the YAML sequence explicitly.

Alternative considered: make the entire subtree optional for incremental migration. Rejected because it would permit a successful build with commands that still lack the accepted metadata and would weaken inventory-wide coverage.

### Keep metadata values extensible and command-specific

The XSD will enforce element structure and repetition but will not enumerate model, agent, or tool values. Each command source owns its description, argument hint, model selection, owning Plinth agent, and applicable tools. Initial migration will use `model: inherit`; each `agent` value will use the exact agent id declared by the command's owning-agent contract and registered in `plinth-agents-generator/src/main/resources/agents.xml`.

Alternative considered: define one global tool list in the inventory or stylesheet. Rejected because `tools/tools-list/tool` is part of each command source and command capabilities can differ.

### Prepend frontmatter without re-rendering the existing body differently

The root XSLT template will render an opening delimiter, metadata keys and tool sequence, a closing delimiter, and then the existing heading/body templates. The established rendering of `goal`, `constraints`, `steps`, `output-format`, and `safeguards` remains unchanged.

One named XSLT template will emit metadata scalars in a YAML-safe single-quoted representation and escape embedded apostrophes by doubling them. The stylesheet will iterate over `tools/tools-list/tool` in document order so the YAML sequence is deterministic and preserves the XML order. Tests will cover YAML-significant punctuation and whitespace boundaries instead of relying only on simple values.

Generated YAML frontmatter will be parsed as YAML 1.2 with SnakeYAML Engine in test scope. Tests will assert the parsed keys and values against the XML `metadata` source, including the ordered tools sequence. The parser is verification infrastructure only and adds no production runtime responsibility.

Alternative considered: embed raw YAML in `goal` CDATA. Rejected because metadata would not be schema-validated, would duplicate formatting across fourteen sources, and would make the tools list opaque to XML processing.

### Reuse the existing command-to-skill bridge

No new packaging path is introduced. `plinth-commands-generator` continues to generate Markdown during `process-classes`; `plinth-skills-generator` continues to bridge those outputs during `generate-resources`; and the generated skill continues to expose them under `004-commands-installation/assets/commands`.

The bridge and installer parity tests will verify content propagation, not merely file presence. Direct output into `.cursor/commands` is outside this build stage.

### Validate at schema, renderer, bundle, and propagation boundaries

Verification will cover:

- all command XML sources validate against the updated XSD;
- every inventoried generated Markdown file starts with valid frontmatter;
- every parsed metadata value matches the maintainer-approved value in the corresponding XML source;
- each XML tool value appears in the corresponding YAML `tools` sequence in the same order;
- the bytes following the closing delimiter and its required separator match the previous generated command Markdown byte-for-byte;
- `004-commands-installation/assets/commands` contains exactly one corresponding asset for every inventory entry; and
- `./mvnw clean verify -pl plinth-skills-generator -am` exercises the integrated path.

## Source Authority and Derivation

| Concern | Authority | Direction |
|---|---|---|
| Problem, value, and frontmatter example | Issue #1075 description | Issue to OpenSpec |
| Root cause, schema constraint, quality attributes | Functional Specification comment | Comment to design and specs |
| Build command and installer-asset outcomes | Approved acceptance-criteria comment | Comment to specs and tasks |
| Current component and Maven boundaries | Repository XML, XSD, XSLT, POMs, and tests | Repository to design |

Later approved acceptance criteria supersede the earlier direct `.cursor/commands` build-path description. No derivation flows back to the issue automatically.

## Risks / Trade-offs

- **Incorrect YAML escaping could create frontmatter that looks plausible but is invalid** → parse generated metadata with a test-scoped YAML 1.2 parser and include apostrophes, colons, hashes, brackets, backslashes, and whitespace boundaries in renderer tests.
- **Command-specific metadata may drift from command behavior** → keep metadata in the same XML source and add per-command contract assertions where routing or tool access matters.
- **A partial migration could leave installer output inconsistent** → require XML `metadata` in the XSD and verify every inventory entry.
- **Prepending metadata could alter existing command bodies through whitespace changes** → compare the content after the closing delimiter and required separator with the previous generated body byte-for-byte.
- **Cursor-compatible metadata may be ignored or interpreted differently by other installer destinations** → preserve the body and document that this change guarantees the approved Cursor format only.

## Migration Plan

1. Extend `commands.xsd` with the required agents-style `metadata` subtree and repeatable tool representation.
2. Add reviewed metadata to every XML source declared by `commands.xml`.
3. Update the XSLT to prepend YAML frontmatter while preserving the existing body templates.
4. Add focused schema, YAML parsing, approved-metadata, ordered-tool, body-parity, inventory, bridge, and installer-asset tests.
5. Run XML validation and `./mvnw clean verify -pl plinth-skills-generator -am`.
6. Inspect `.agents/skills/004-commands-installation/assets/commands` for complete, valid output.

Rollback removes the XML `metadata` subtree, YAML-frontmatter renderer prefix, migrated metadata, and associated tests together; the prior Markdown body generation and Maven bridge then remain intact.

## Open Questions

There are no remaining design questions. The maintainer will approve the exact metadata values for each of the fourteen commands during migration, and generated YAML will be verified with SnakeYAML Engine in test scope.
