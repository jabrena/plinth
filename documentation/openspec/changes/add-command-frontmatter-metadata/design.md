## Context

The command pipeline currently treats XML under `plinth-commands-generator/src/main/resources/commands/` as authoritative, validates it with the no-namespace `commands.xsd`, renders Markdown with `command-to-markdown.xsl`, and bridges the generated files into the `004-commands-installation` skill. Generated command files currently start with `# <command-id>` and contain no YAML frontmatter.

Issue [#1075](https://github.com/jabrena/plinth/issues/1075), its complete Functional Specification, and its approved acceptance criteria require every inventoried command to carry Cursor-compatible frontmatter while preserving the existing Markdown body. The maintainer-confirmed build boundary is `.agents/skills/004-commands-installation/assets/commands` after `./mvnw clean verify -pl plinth-skills-generator -am`; installing those assets into a selected tool directory remains the responsibility of `004-commands-installation`.

## Goals / Non-Goals

**Goals:**

- Represent `description`, `argument-hint`, `model`, `agent`, and a repeatable tool list in every authoritative command XML document.
- Render valid YAML frontmatter before the existing Markdown heading.
- Map XML `tools/list-tools/tool` entries to YAML `tools` sequence entries.
- Preserve every command's existing Markdown content after the frontmatter.
- Package one frontmatter-enabled asset for every inventoried command in the generated `004-commands-installation` skill.
- Verify schema validity, rendering, inventory coverage, propagation, and body preservation through the existing Maven reactor build.

**Non-Goals:**

- Change command file names, inventory order, workflow behavior, ownership, routing, safeguards, or Markdown body semantics.
- Change the interactive installation behavior of `004-commands-installation`.
- Claim that Cursor frontmatter fields have identical semantics in every supported command framework.
- Refresh public `skills/`; local generation continues to target `.agents/skills` unless the release profile is explicitly selected.
- Select a new command architecture or replace the existing XML → XSLT → Markdown pipeline.

## Change Boundary Assessment

This is one atomic change. Schema representation, frontmatter rendering, XML-source migration, and installer-asset propagation share one user-visible outcome, build, ownership boundary, and rollback path. Splitting the work by XSD, XSLT, or Maven module would create technical-layer changes with no independently useful result.

## Decisions

### Add a required frontmatter subtree before the narrative contract

`commands.xsd` will extend the root command sequence with a required `frontmatter` element before `goal`. The subtree will use this source shape:

```xml
<frontmatter>
    <description>One sentence describing what this command does.</description>
    <argument-hint>[optional arguments]</argument-hint>
    <model>inherit</model>
    <agent>inherit</agent>
    <tools>
        <list-tools>
            <tool>Read</tool>
            <tool>Edit</tool>
        </list-tools>
    </tools>
</frontmatter>
```

All five metadata fields are required because the issue requires every generated command to follow the approved structure. `list-tools` contains one or more `tool` elements so the XML model represents the YAML sequence explicitly.

Alternative considered: make the entire subtree optional for incremental migration. Rejected because it would permit a successful build with commands that still lack the accepted metadata and would weaken inventory-wide coverage.

### Keep metadata values extensible and command-specific

The XSD will enforce element structure and repetition but will not enumerate model, agent, or tool values. Each command source owns its description, argument hint, inherited model/agent selection, and applicable tools. Initial migration will use the approved `model: inherit` and `agent: inherit` values while command-specific descriptions, hints, and tools are reviewed against each command contract.

Alternative considered: define one global tool list in the inventory or stylesheet. Rejected because `tools/list-tools/tool` is part of each command source and command capabilities can differ.

### Prepend frontmatter without re-rendering the existing body differently

The root XSLT template will render an opening delimiter, metadata keys and tool sequence, a closing delimiter, and then the existing heading/body templates. The established rendering of `goal`, `constraints`, `steps`, `output-format`, and `safeguards` remains unchanged.

Generated scalar values must be emitted in a YAML-safe representation. Verification will parse or otherwise validate the complete frontmatter rather than relying only on delimiter checks.

Alternative considered: embed raw YAML in `goal` CDATA. Rejected because metadata would not be schema-validated, would duplicate formatting across fourteen sources, and would make the tools list opaque to XML processing.

### Reuse the existing command-to-skill bridge

No new packaging path is introduced. `plinth-commands-generator` continues to generate Markdown during `process-classes`; `plinth-skills-generator` continues to bridge those outputs during `generate-resources`; and the generated skill continues to expose them under `004-commands-installation/assets/commands`.

The bridge and installer parity tests will verify content propagation, not merely file presence. Direct output into `.cursor/commands` is outside this build stage.

### Validate at schema, renderer, bundle, and propagation boundaries

Verification will cover:

- all command XML sources validate against the updated XSD;
- every inventoried generated Markdown file starts with valid frontmatter;
- each XML tool value appears in the corresponding YAML `tools` sequence;
- the Markdown following the closing delimiter preserves the previous command content;
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

- **Incorrect YAML escaping could create frontmatter that looks plausible but is invalid** → validate generated metadata as YAML and include representative punctuation in renderer tests.
- **Command-specific metadata may drift from command behavior** → keep metadata in the same XML source and add per-command contract assertions where routing or tool access matters.
- **A partial migration could leave installer output inconsistent** → require frontmatter in the XSD and verify every inventory entry.
- **Prepending metadata could alter existing command bodies through whitespace changes** → compare the content after the closing delimiter with the previous generated body.
- **Cursor-compatible metadata may be ignored or interpreted differently by other installer destinations** → preserve the body and document that this change guarantees the approved Cursor format only.

## Migration Plan

1. Extend `commands.xsd` with the required frontmatter subtree and repeatable tool representation.
2. Add reviewed metadata to every XML source declared by `commands.xml`.
3. Update the XSLT to prepend YAML frontmatter while preserving the existing body templates.
4. Add focused schema, renderer, inventory, bridge, and installer-asset tests.
5. Run XML validation and `./mvnw clean verify -pl plinth-skills-generator -am`.
6. Inspect `.agents/skills/004-commands-installation/assets/commands` for complete, valid output.

Rollback removes the frontmatter subtree, renderer prefix, migrated metadata, and associated tests together; the prior Markdown body generation and Maven bridge then remain intact.

## Open Questions

- Which exact description, argument hint, and tool list is approved for each of the fourteen commands? Implementation must review these values command by command rather than copy one example blindly.
- Which test-scope YAML validation mechanism best proves syntactic validity without adding a production runtime dependency? This is an implementation choice; the requirement to validate generated YAML is fixed.
