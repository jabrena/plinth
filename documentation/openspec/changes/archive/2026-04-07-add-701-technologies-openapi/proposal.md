# Add 701 Technologies — OpenAPI Best Practices

## Problem Statement

Issue [#537](https://github.com/jabrena/cursor-rules-java/issues/537) requests OpenAPI best-practices support. Today, OpenAPI guidance is embedded primarily in framework REST skills (`302`, `402`, `502`). There is no **framework-agnostic** skill under a dedicated **technologies** band for API contracts, so agents lack a single trigger for spec quality, validation, lifecycle, and codegen handoffs without choosing Spring, Quarkus, or Micronaut first.

Without a structured OpenSpec change, numbering (`701`), scope boundaries, and verification expectations can drift.

## Proposed Solution

Introduce **`701-technologies-openapi`**: a PML system prompt and matching agent skill that cover **OpenAPI 3.x** as the artifact (structure, metadata, paths, schemas, security, examples, linting, breaking-change awareness), explicitly **deferring** framework-specific runtime and plugin wiring to `302` / `402` / `502`.

Reserve the **`700`–`799`** range for cross-cutting **technology** prompts, starting with **701** for OpenAPI, and register the new id in both generator inventories.

## Impact Assessment

- **Breaking changes:** None for consumers of existing skills; this is additive.
- **Cross-tool compatibility:** Generated skills output only; no change to Cursor rule legacy paths unless the project later wires them.
- **Migration:** None required. Contributors implementing API-first work may prefer **701** for contract review and framework skills for implementation.

## Success Criteria

- OpenSpec requirements fix the skill id as **`701-technologies-openapi`** and describe framework-agnostic vs framework-delegation behavior.
- Tasks checklist covers XML sources, inventory registration, build verification, and optional contributor documentation for the **700** band.
- `openspec validate --all` passes for this change.
