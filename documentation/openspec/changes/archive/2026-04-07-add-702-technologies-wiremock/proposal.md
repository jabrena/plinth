# Add 702 Technologies — WireMock Best Practices

## Problem Statement

Issue [#544](https://github.com/jabrena/cursor-rules-java/issues/544) requests WireMock best-practices support. Today, WireMock guidance is embedded primarily in integration-test–oriented skills (`132`, `322`, `422`, `522`). There is no **framework-agnostic** skill under the **technologies** band (`700`–`799`) for HTTP stubbing, mapping lifecycle, and verification patterns, so agents lack a single trigger for portable WireMock work without choosing Spring Boot, Quarkus, or Micronaut first.

Without a structured OpenSpec change, numbering (`702`), scope boundaries, and verification expectations can drift.

## Proposed Solution

Introduce **`702-technologies-wiremock`**: a PML system prompt and matching agent skill that cover **WireMock** as the artifact (stub design, request matching, response fixtures, isolation, lifecycle, verification, common pitfalls), explicitly **deferring** framework-specific test bootstrap and extension wiring to `132` / `322` / `422` / `522`.

Reserve **`702`** for cross-cutting **WireMock** guidance alongside **`701`** for OpenAPI (`701-technologies-openapi`), and register the new id in both generator inventories.

## Impact Assessment

- **Breaking changes:** None for consumers of existing skills; this is additive.
- **Cross-tool compatibility:** Generated skills output only; no change to legacy Cursor rule paths unless the project later wires them.
- **Migration:** None required. Contributors implementing stubs in tests may prefer **702** for WireMock patterns and framework integration skills for full-stack test setup.

## Success Criteria

- OpenSpec requirements fix the skill id as **`702-technologies-wiremock`** and describe framework-agnostic vs framework-delegation behavior.
- Tasks checklist covers XML sources, inventory registration, build verification.
- `openspec validate --all` passes for this change.
