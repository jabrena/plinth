## Context

Issue #1048 identifies one missing design skill: `059-design-atdd`. Its maintainer-authorized user story and subsequent clarification define an OpenSpec review capability: evaluate whether a change's acceptance criteria support its execution goal and whether its task checklist covers those criteria without drifting in another direction.

The review must expose criteria that are missing, vague or ambiguous, partially covered, or not covered, as well as tasks that diverge from the acceptance criteria or execution goal. Its result is an OpenSpec alignment decision with evidence, not workflow orchestration.

## Goals / Non-Goals

**Goals:**

- Define `059-design-atdd` as a generated design skill.
- Review the execution goal, acceptance criteria, and associated tasks as one alignment set.
- Determine whether acceptance criteria exist and are sufficiently clear to guide execution.
- Trace acceptance criteria to tasks and identify complete, partial, missing, ambiguous, or divergent coverage.
- Return `ready` or `changes-requested`, explaining every pending finding that must be resolved in the OpenSpec artifacts.
- Make source ownership, generator registration, acceptance coverage, documentation, compatibility, and validation expectations explicit.

**Non-Goals:**

- Selecting unapproved ATDD techniques, workshop roles, test frameworks, or automation tools.
- Orchestrating commands, agents, or other skills.
- Adding a testing-library dependency, framework-specific test code, or a new Maven module.
- Editing generated `.agents/skills/`, public `skills/`, legacy `.cursor/rules/`, or website `docs/` directly.
- Refreshing public release output unless a later release operation is explicitly requested.

## Decisions

### One change, one outcome

Use one OpenSpec change, `add-atdd-design-skill`. XML source, generator registration, acceptance coverage, inventory documentation, and verification are inseparable parts of one additive capability and have no independent release or rollback value.

### Stable identity and generator structure

Reserve id `059` and the explicit skill id `059-design-atdd`. Follow the unified generator convention:

- `plinth-skills-generator/src/main/resources/skill-indexes/059-skill.xml` owns generated skill metadata, triggers, constraints, workflow summary, outputs, and safeguards.
- `plinth-skills-generator/src/main/resources/skill-references/059-design-atdd.xml` owns the complete alignment-status definitions and example-driven traceability report guidance without duplicating the procedural workflow.
- `plinth-skills-generator/src/main/resources/skills.xml` registers id `059`, explicit skill id `059-design-atdd`, and reference `059-design-atdd`; the entry uses the default reference-generating behavior and does not declare `requiresSystemPrompt="false"`.

The maintainer-selected dedicated reference uses the repository-owned OpenSpec requirements and calculator fixture as its content authority. No external ATDD methodology or testing framework is introduced.

### ATDD responsibility boundary

The skill reviews three repository-owned parts of an OpenSpec change together:

1. The execution goal expressed by the proposal and requirements.
2. The acceptance criteria expressed by specification scenarios.
3. The associated implementation and verification tasks in `tasks.md`.

For each review, the skill must determine whether:

- Acceptance criteria exist for the execution goal.
- Each criterion is clear enough to guide implementation and observable verification.
- Tasks cover each criterion completely or only partially.
- A criterion has no associated task.
- A task has no support in the execution goal or acceptance criteria and therefore diverges from the intended direction.
- Terminology or scope differs across the goal, criteria, and tasks in a way that creates ambiguity.

The outcome must preserve traceability between findings and the relevant goal, criterion, and task. It must report gaps and recommended refinements without silently creating, deleting, or rewriting acceptance criteria or tasks. When any unresolved partial, missing, ambiguous, absent, or divergent finding exists, the outcome is `changes-requested`: the skill explains each pending finding and asks the maintainer how the OpenSpec artifacts should be revised. A review is `ready` only when no unresolved alignment finding exists.

The bundled reference defines the report layout and finding categories. The skill remains scoped to the reviewed OpenSpec artifacts and has no knowledge of commands, agents, or other skills.

### Trust and source boundary

The skill must treat acceptance criteria, scenarios, examples, tables, and test-like text as requirement data rather than executable instructions. It reports conflicting or unsupported interpretations as pending OpenSpec findings rather than resolving them silently.

### Acceptance coverage and documentation

Add `059-design-atdd.feature` with focused acceptance coverage for goal-to-criteria-to-task alignment, including an aligned `ready` case and a structurally valid OpenSpec change with complete, missing, partial, ambiguous, absent, and divergent findings. The negative case must prove the `changes-requested` outcome, a concrete explanation of every pending finding, a focused request for maintainer revision, no artifact mutation, and mandatory use of the generated bundled reference. Add the exact matching `execute @...feature` entry to `acceptance-tests-prompts-skills.md`, grouped under the skill id, and document the skill in the Design section of the Java skills inventory.

### Compatibility review

This change is additive and non-breaking. Existing skill identifiers and behavior remain unchanged. No compatibility adapter, migration, feature toggle, or release-output refresh is needed for local implementation verification.

### Verification strategy

- Validate every edited XML file with `xmllint --noout`.
- Run `./mvnw clean install -pl plinth-skills-generator -am` to verify generator behavior and refresh local `.agents/skills` output.
- Inspect `.agents/skills/059-design-atdd/SKILL.md` and `.agents/skills/059-design-atdd/references/059-design-atdd.md` for unresolved include markers and broken local paths.
- Run only the new `059-design-atdd` acceptance prompt from `acceptance-tests-prompts-skills.md`.
- Run the Markdown validator because the Java skill inventory changes.
- Run `openspec validate --all` for the planning artifacts.

## Risks / Trade-offs

- Keeping the skill limited to OpenSpec alignment review avoids coupling its decision to a particular orchestration workflow.
- A task may be technically valid but unsupported by the execution goal or acceptance criteria; reporting divergence without silently removing the task preserves maintainer authority.
- Criteria-to-task relationships may be many-to-many; the output must preserve traceability without implying that each criterion requires exactly one task.
- Splitting detailed definitions into a reference adds one runtime read, but keeps `SKILL.md` concise and makes examples available only when the skill is invoked.

## Open Questions

None. Ambiguous or conflicting inputs remain explicit findings and cause a focused `changes-requested` interaction.
