---
status: "accepted"
date: 2026-06-04
decision-makers: Juan Antonio Breña Moral
consulted: Repository maintainers
informed: Repository contributors and downstream skill consumers
---

# Separate Local Skill Generation from Release Publishing

## Context and Problem Statement

The repository generates Agent Skills from XML sources in `skills-generator/src/main/resources`.
The generated `skills/` directory is also the public skill registry source. Before this
decision, running the normal Maven install workflow for `skills-generator` copied generated
skills into `skills/`. That made routine XML-side edits produce release-visible changes before
maintainers had intentionally prepared a public skill release.

Maintainers need a local regeneration path for testing changed XML content with agents, but
the public `skills/` directory should only be refreshed when the generated output is ready
for release.

## Decision Drivers

* Avoid accidental public skill registry changes during normal XML editing.
* Preserve a fast local path for validating regenerated skills with agent tooling.
* Keep `skills/` as an explicit release output instead of a side effect of local generation.
* Avoid symbolic links so local agent output works consistently across tools and platforms.
* Make the release refresh command visible and auditable.

## Considered Options

* Option 1: Continue copying generated skills into `skills/` during the normal install workflow.
* Option 2: Copy normal generated skills into `.agents/skills` and add a `release` profile for `skills/`.
* Option 3: Stop copying generated skills anywhere and require maintainers to inspect only `skills-generator/target/skills`.

## Decision Outcome

Chosen option: "Option 2: Copy normal generated skills into `.agents/skills` and add a
`release` profile for `skills/`", because it separates local testing from public release
publishing while preserving both workflows behind the normal Maven `install` lifecycle.

The normal `copy-skills` action copies generated output into `.agents/skills`. When the
`release` profile is active, Maven overrides the copy destination to the repository-level
`skills/` directory and cleans that directory before the install-phase copy runs.

### Consequences

* Good, because XML-side skill edits can be tested locally without creating generated diffs in `skills/`.
* Good, because release output under `skills/` is refreshed only through an explicit profile.
* Good, because `.agents/skills` avoids symbolic links and remains compatible with local agent tooling.
* Bad, because maintainers must remember which command is local and which command is for release.
* Neutral, because CI can continue validating `skills/` as the public release source.

### Confirmation

The implementation is confirmed when:

* `./mvnw clean install -pl skills-generator` generates skills and copies them into `.agents/skills`.
* The normal local workflow does not create, modify, clean, or delete repository-level `skills/`.
* `./mvnw clean install -pl skills-generator -P release` cleans and refreshes `skills/`.
* `AGENTS.md` and `DEVELOPER.md` document the distinction between local generation and release publishing.

## Pros and Cons of the Options

### Option 1: Continue copying to `skills/` during install

* Good, because it keeps the previous command behavior.
* Bad, because normal XML edits can create premature release-visible changes.
* Bad, because local testing and public publishing remain coupled.

### Option 2: Use `.agents/skills` locally and `skills/` for explicit releases

* Good, because local regeneration is safe by default.
* Good, because release publishing remains available and intentional.
* Good, because the workflow maps cleanly to agent-local and registry-release outputs.
* Bad, because it introduces one more Maven profile for maintainers to learn.

### Option 3: Do not copy generated skills automatically

* Good, because it removes generated-copy side effects from Maven install.
* Bad, because it makes local agent testing less ergonomic.
* Bad, because maintainers would need manual copy steps for both local testing and release preparation.

## More Information

* Issue: https://github.com/jabrena/plinth/issues/751
* Prior decisions: [ADR-004](./ADR-004-skill-generation.md), [ADR-005](./ADR-005-drop-cursor-rules-support-in-favor-of-agent-skills.md)
