## Context

`SkillsGenerator` currently returns generated `SKILL.md` and reference Markdown content. `SkillsGeneratorTest` writes that output into `target/skills`, which Maven then copies to `.agents/skills` or, with the release profile, `skills/`. XInclude currently expands the five scoped resources directly into reference Markdown.

## Goals / Non-Goals

**Goals:**

- Represent skill-owned files explicitly in generated output.
- Package only the five resources owned by skills `151`, `161`, and `703`.
- Preserve source content and make generated shell scripts executable.
- Replace embedded bodies with relative links that resolve from `references/`.

**Non-Goals:**

- General discovery or packaging of every XInclude resource.
- Packaging Markdown templates, command definitions, agent definitions, or question files.
- Refactoring the overall test-driven generation lifecycle.
- Refreshing public `skills/` release output.

## Decisions

### Declare resources in the skill inventory

Add a `resource-list` to relevant `skills.xml` entries. Each resource declares its classpath `source` and skill-root-relative `target`. `SkillIndexes` parses and validates this metadata, and `SkillsGenerator` loads the resources supplied by each descriptor.

This keeps skill ownership in the existing inventory instead of hardcoding skill identifiers and paths in Java. It also remains explicit, preventing unrelated XIncludes from becoming packaged resources.

Alternative considered: hardcode an explicit per-skill map in `SkillsGenerator`. This was rejected because it duplicates inventory information in Java and requires generator code changes for every resource assignment.

Alternative considered: scan XInclude declarations automatically. This was rejected because not every include is a runtime resource and automatic scanning would broaden issue #801 beyond its approved scope.

### Load resources from the classpath

Read the existing files under `skill-references/scripts/` and `skill-references/assets/docker/` through the generator classloader. This preserves the existing XML resource sources and avoids duplicate source files.

Alternative considered: copy resources with a broad Maven resources rule. This was rejected because it cannot express per-skill ownership without duplicating configuration and risks copying every global asset.

### Keep packaging in the existing target writer

Write `SkillOutput` resources alongside `SKILL.md` and `references/` in the existing target generation step. Set executable permissions for paths under `scripts/`.

Alternative considered: introduce a new production packaging service. This would be cleaner in isolation but is a larger architectural refactor than the scoped issue requires.

### Replace XInclude bodies with Markdown links

The three XML references will link from generated `references/*.md` files using `../scripts/...` and `../assets/...`. The instructional text and safeguards remain unchanged.

## Risks / Trade-offs

- [Risk] Invalid inventory paths could write outside a generated skill directory. -> Reject absolute targets, parent traversal, missing attributes, and duplicate targets while parsing `skills.xml`.
- [Risk] Executable permissions behave differently on non-POSIX filesystems. -> Set the executable bit when supported and test the generated file on the current build platform.
- [Risk] A resource path can become stale if a source file moves. -> Fail generation when a mapped classpath resource cannot be loaded.

## Migration Plan

1. Add resource declarations to `skills.xml` and extend the inventory descriptor.
2. Update the three XML references to use relative links.
3. Generate local skills and verify layouts and links.
4. Roll back by reverting the generator mapping and restoring the XIncludes if validation fails.

## Open Questions

None for the scoped implementation.
