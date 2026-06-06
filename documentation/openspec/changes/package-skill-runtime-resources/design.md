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

### Use an explicit per-skill resource map

Extend `SkillOutput` with a map from skill-root-relative output path to UTF-8 resource content. A fixed mapping keeps ownership deterministic and prevents unrelated skills from receiving resources.

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

- [Risk] The explicit mapping requires code changes when another skill gains a runtime resource. -> Keep this issue intentionally scoped and consider a declarative inventory in a future change.
- [Risk] Executable permissions behave differently on non-POSIX filesystems. -> Set the executable bit when supported and test the generated file on the current build platform.
- [Risk] A resource path can become stale if a source file moves. -> Fail generation when a mapped classpath resource cannot be loaded.

## Migration Plan

1. Add the explicit resource mapping and output model.
2. Update the three XML references to use relative links.
3. Generate local skills and verify layouts and links.
4. Roll back by reverting the generator mapping and restoring the XIncludes if validation fails.

## Open Questions

None for the scoped implementation.
