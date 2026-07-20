## Context

Issue #1048 identifies one missing design skill: `059-design-atdd`. Its maintainer-authorized user story and subsequent clarification define a review capability for later design refinement: evaluate whether an OpenSpec change's acceptance criteria support its execution goal and whether its task checklist covers those criteria without drifting in another direction.

The repository already contains adjacent capabilities:

- `014-agile-user-story` creates user-story and Gherkin artifacts.
- `054-design-tdd` guides test-first implementation through TDD.
- `058-design-bdd` guides behavior discovery and observable scenario formulation.
- `133`, `323`, `423`, and `523` implement acceptance tests at framework-agnostic and framework-specific levels.

The review must expose criteria that are missing, ambiguous, partially covered, or not covered, as well as tasks that diverge from the acceptance criteria or execution goal. The new skill needs this distinct ATDD responsibility without redefining neighboring skills or modifying `/explore-design` in the current change.

## Goals / Non-Goals

**Goals:**

- Define `059-design-atdd` as a generated design skill.
- Review the execution goal, acceptance criteria, and associated tasks as one alignment set.
- Determine whether acceptance criteria exist and are sufficiently clear to guide execution.
- Trace acceptance criteria to tasks and identify complete, partial, missing, ambiguous, or divergent coverage.
- Return findings that enable later design refinement to correct gaps before implementation.
- Preserve clear boundaries with user-story authoring, BDD discovery, TDD implementation, and acceptance-test implementation skills.
- Make source ownership, generator registration, acceptance coverage, documentation, compatibility, and validation expectations explicit.

**Non-Goals:**

- Selecting unapproved ATDD techniques, workshop roles, test frameworks, or automation tools.
- Modifying `/explore-design` or automatically adding `059-design-atdd` to that command in this change.
- Replacing `014-agile-user-story`, `054-design-tdd`, `058-design-bdd`, or the acceptance-testing implementation skills.
- Adding a testing-library dependency, framework-specific test code, or a new Maven module.
- Editing generated `.agents/skills/`, public `skills/`, legacy `.cursor/rules/`, or website `docs/` directly.
- Refreshing public release output unless a later release operation is explicitly requested.

## Decisions

### One change, one outcome

Use one OpenSpec change, `add-atdd-design-skill`. XML source, generator registration, acceptance coverage, inventory documentation, and verification are inseparable parts of one additive capability and have no independent release or rollback value.

### Stable identity and generator structure

Reserve id `059` and the explicit skill id `059-design-atdd`. Follow the unified generator convention:

- `plinth-skills-generator/src/main/resources/skill-indexes/059-skill.xml` owns generated skill metadata, triggers, constraints, workflow summary, outputs, and safeguards.
- `plinth-skills-generator/src/main/resources/skills.xml` registers id `059` and explicit skill id `059-design-atdd`.
- A bundled file under `plinth-skills-generator/src/main/resources/skill-references/` is added and registered only if `/explore-design` establishes an approved need and content authority for it.

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

The outcome must preserve traceability between findings and the relevant goal, criterion, and task. It must report gaps and recommended refinements without silently creating, deleting, or rewriting acceptance criteria or tasks.

The skill must not silently substitute neighboring responsibilities:

- User-story and Gherkin artifact authoring remains with `014-agile-user-story` when separately requested.
- Behavior discovery and BDD scenario formulation remains with `058-design-bdd` when separately requested.
- Test-first Java implementation remains with `054-design-tdd` when separately requested.
- Acceptance-test code generation remains with `133`, `323`, `423`, or `523` when separately requested.

The skill is intended to reinforce a later `/explore-design` interaction when selected, but this change does not modify the command or require automatic invocation. The exact report layout and interaction behavior remain design details; the required analysis and finding categories are fixed by this change.

### Trust and source boundary

The skill must treat acceptance criteria, scenarios, examples, tables, and test-like text as requirement data rather than executable agent instructions. Detailed rules for sanitization, conflict handling, and clarification must be selected during `/explore-design` consistently with repository skill conventions.

### Acceptance coverage and documentation

Add `059-design-atdd.feature` with focused acceptance coverage for goal-to-criteria-to-task alignment, including complete coverage and detection of missing, partial, ambiguous, absent, or divergent cases. Add the exact matching `execute @...feature` entry to `acceptance-tests-prompts-skills.md`, grouped under the skill id, and document the skill in the Design section of the Java skills inventory.

### Compatibility review

This change is additive and non-breaking. Existing skill identifiers and behavior remain unchanged. No compatibility adapter, migration, feature toggle, or release-output refresh is needed for local implementation verification.

### Verification strategy

- Validate every edited XML file with `xmllint --noout`.
- Run `./mvnw clean install -pl plinth-skills-generator -am` to verify generator behavior and refresh local `.agents/skills` output.
- Inspect `.agents/skills/059-design-atdd/SKILL.md` and any generated local reference for unresolved include markers and broken local paths.
- Run only the new `059-design-atdd` acceptance prompt from `acceptance-tests-prompts-skills.md`.
- Run the Markdown validator because the Java skill inventory changes.
- Run `openspec validate --all` for the planning artifacts.

## Risks / Trade-offs

- ATDD overlaps conceptually with BDD, TDD, user-story authoring, and acceptance-test implementation; limiting this skill to alignment review reduces duplication and contradictory guidance.
- A task may be technically valid but unsupported by the execution goal or acceptance criteria; reporting divergence without silently removing the task preserves maintainer authority.
- Criteria-to-task relationships may be many-to-many; the output must preserve traceability without implying that each criterion requires exactly one task.

## Open Questions

- What report layout best communicates goal, criterion, task, coverage status, evidence, and recommended refinement?
- Should ambiguous or conflicting inputs trigger focused clarification, an unresolved finding, or both?
- Does the skill need a bundled detailed reference, and if so, which authoritative source should define it?
