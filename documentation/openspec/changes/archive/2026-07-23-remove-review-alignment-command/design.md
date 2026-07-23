## Context

Issue #1084 asks to remove `/review-alignment` and update `robot-business-analyst`. The Functional Specification posted on the issue assumed the change was limited to `review-alignment.xml`, `commands.xml`, `robot-business-analyst.xml`, plus tests, the skills pipeline, and trilingual docs. Spec inspection during this change found two further living OpenSpec capability specs that reference `/review-alignment`:

- `analysis-design-commands/spec.md` lists `/review-alignment` in the command bundle requirement and has a dedicated "Read-only alignment command routing" requirement.
- `implement-spec-command/spec.md`'s "Stop on artifact conflict" scenario routes material artifact conflicts to `/review-alignment`.

This invalidates the Functional Specification's assumption that "no other command or agent besides `robot-business-analyst` depends on the ... mission text" — `implement-spec-command` depends on the command itself for conflict routing.

## Decisions

### Retire the command and its dedicated requirement rather than aliasing it

Remove `/review-alignment` from `analysis-design-commands`'s command bundle requirement and delete its "Read-only alignment command routing" requirement outright, mirroring the precedent set by the `/review-breaking-changes` retirement in the same spec file. An alias would keep a command-level contract the issue explicitly asks to remove.

### Route implement-spec-command's artifact conflicts to robot-business-analyst directly

`implement-spec-command`'s "Stop on artifact conflict" scenario is updated so a material conflict is routed to `robot-business-analyst` for manual assessment, rather than to the now-removed `/review-alignment` command. `robot-business-analyst` itself is not being removed — only its dedicated alignment-review command and goal mission — so naming the agent (without a specific command contract) keeps a responsible owner for conflict escalation without reintroducing the retired command.

This decision was made directly by the implementing agent rather than through further maintainer clarification, at explicit maintainer direction to proceed without additional interactive questions; it is recorded here as an open point for maintainer review rather than silently assumed.

### Remove read-only alignment review from robot-business-analyst's described ownership

`analysis-design-agents/spec.md`'s "Business analyst missions" requirement and its scenario currently list "read-only alignment review" among `robot-business-analyst`'s continuing responsibilities. Both are updated to drop that phrase, since the agent no longer carries the mission or the command that implemented it.

### Preserve archived OpenSpec history unchanged

`documentation/openspec/changes/archive/**` contains point-in-time records that mention `/review-alignment`. These are left untouched; only living specs under `documentation/openspec/specs/` and active generator/test/doc sources are updated.

## Two-Step Sequencing

### Step 1: Update specs and agent/command sources together

Because command, agent, and spec text describe the same retired capability, update `robot-business-analyst.xml`, `commands.xml`, `review-alignment.xml` (removed), and the three OpenSpec capability specs in one step so no intermediate state has sources and specs disagreeing about whether `/review-alignment` exists.

Validation after Step 1:
- Validate changed XML files with `xmllint --noout`.
- Run `openspec validate --all` from `documentation/`.

### Step 2: Update tests, skills pipeline, and documentation

Remove or update command-focused tests and fixtures, the skills pipeline registrations and generated command-installation assets, and the trilingual documentation guides.

Validation after Step 2:
- Run `rg "review-alignment"` (case-sensitive on the literal command name) and confirm remaining matches are limited to `documentation/openspec/changes/archive/**`.
- Run `./mvnw clean verify -pl plinth-commands-generator -pl plinth-agents-generator -pl plinth-skills-generator -am`.
- Regenerate local skills (`./mvnw clean install -pl plinth-skills-generator -am`) and inspect `.agents/skills` output for the absence of `/review-alignment`.

## Risks / Trade-offs

- [Risk] Removing the conflict-routing target in `implement-spec-command` changes real delegation behavior beyond issue #1084's literal request. -> Recorded explicitly as a design decision above for maintainer review rather than silently folded into the command-removal task list.
- [Risk] Trilingual documentation (`_ES`, `_ZH`) drifts from the English source if only `GETTING-STARTED-WORKFLOWS.md` is updated. -> Update all three language variants in the same change, per `CLAUDE.md`.
- [Risk] Command-focused tests assert a specific command count or enumerate commands by name and fail unexpectedly once `/review-alignment` is removed. -> Run the full affected-module test suite before considering the change complete.

## Open Questions

- ~~Should `implement-spec-command`'s conflict-routing scenario name `robot-business-analyst` specifically, or should it drop agent routing entirely and only say implementation stops for maintainer decision?~~ Resolved during `/explore-design`: applying `053-design-simple-rules` in Beck's priority order (correctness, then intention, then duplication, then fewest elements) ranks naming `robot-business-analyst` directly above dropping agent routing (weaker intention) and above leaving the stale `/review-alignment` reference in place (fails correctness outright, since a living spec would still reference a retired command). Recorded as the confirmed design direction.

## Design Review Findings (`/explore-design`)

- **056-design-avoid-breaking-changes** review classified the command and agent-mission removal as intentional `BREAKING` changes (matching issue #1084's explicit request, with no alias — consistent with the prior `/review-breaking-changes` retirement precedent), and found `README.md`, `README_ES.md`, and `README_ZH.md` all list `/review-alignment` in their commands tables. This was missing from the original Functional Specification and the initial version of this OpenSpec change; `proposal.md` and `tasks.md` were refined to add it (task 1.11).
- CHANGELOG.md has no `Unreleased` section — entries are batched at version-bump time — so no changelog task was added; this is out of scope for this change.
- No new ADR is recommended: this change retires an existing command/mission using an established repository precedent (`/review-breaking-changes` retirement) rather than introducing a new architectural decision. The conflict-routing choice above is recorded as a design decision in this document, which is sufficient traceability.
- **059-design-atdd** alignment gate (first pass) returned `changes-requested` with three findings: README not named in any acceptance criterion (only implied by the generic stale-reference sweep), `plinth-commands-generator/src/main/resources/commands/implement-spec.xml` still requesting `/review-alignment` on conflict with no task to fix it, and `plinth-commands-generator/src/main/resources/java-commands-inventory-template.md` still listing `/review-alignment` with no task to fix it. All three were maintainer-approved for the recommended fix: `tasks.md` gained 1.12 and 1.13, `proposal.md`'s scope/impact were extended, and the `analysis-design-commands` spec delta gained an explicit README scenario and named `implement-spec.xml` in its retirement scenario. Rerunning the gate after these fixes found the traceability complete for all goals; alignment outcome is now `ready`.
