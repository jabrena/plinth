## Context

Issue #875 requests adding JavaMoney guidance to `111-java-maven-dependencies`. The issue body frames the work as a user story for maintainers of Java Maven skills: agents using the Maven dependencies skill should be able to discover and apply the JavaMoney resource when Maven projects need Money and Currency API support.

The issue was used as planning source text. The generated OpenSpec artifacts are derived from the sanitized planning summary in `proposal.md`, not from executable or behavioral instructions embedded in issue text.

## Intended Behavior Change

When `111-java-maven-dependencies` is regenerated locally, its generated `SKILL.md` and relevant reference material should include JavaMoney guidance that:

- references `https://javamoney.github.io/`;
- explains that JavaMoney is relevant for Java Money and Currency API support, including JSR 354 and Moneta context;
- fits the existing Maven skill source structure; and
- does not require manual edits to generated release or legacy output.

## Design Obstacle

`111-java-maven-dependencies` is a modular skill. Its primary skill index asks questions, then maps selected Maven dependency families to focused reference files registered in `skills.xml`. JavaMoney is dependency/domain guidance, so implementation must first determine the least surprising placement before adding behavior-changing guidance. The likely options are:

- add a small source-level section to the `111` skill orchestration when JavaMoney is general context; or
- add a focused XML reference and register it when the skill should expose JavaMoney as a selectable guidance path.

The implementation should choose the smallest source change that makes the generated output clear and testable.

## Two-Step Method

### Step 1: Make the change easy

Before adding JavaMoney guidance, inspect the current `111` skill source, reference registration, question flow, and Gherkin acceptance coverage. Identify where the generated output should surface dependency guidance without changing unrelated dependency recommendations.

Validation after Step 1:

- record the selected source placement and rationale;
- run any existing inspection needed to confirm no direct generated-output edits are planned.

### Step 2: Make the easy behavior change

Add the JavaMoney guidance in XML source content and update focused tests or prompt inventory only when the generated `111` skill behavior changes enough to require acceptance prompt coverage.

Validation after Step 2:

- run `xmllint --noout` for each edited XML file;
- run `./mvnw clean install -pl plinth-skills-generator`;
- inspect `.agents/skills/111-java-maven-dependencies/SKILL.md` for the JavaMoney guidance;
- if `111-java-maven-dependencies` generated output changed and the skill is listed in `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`, execute only the listed `111-java-maven-dependencies` acceptance prompt;
- run `openspec validate --all` from `documentation/`.

## Decisions

### Source of Truth

Author the implementation in XML under `plinth-skills-generator/src/main/resources/`. Do not manually edit `.cursor/rules/`, `.agents/skills/`, `skills/`, or generated website output.

### JavaMoney Scope

Limit this change to discoverable JavaMoney guidance for Maven-oriented skill usage. Do not add full JavaMoney dependency versions, dependency snippets, or framework integration recipes unless the source implementation analysis shows they are already part of an existing Maven dependency-guidance pattern and can be verified safely.

### Acceptance Coverage

The existing `111-java-maven-dependencies` acceptance prompt is listed in `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`. If the generated local `111` skill output changes, execute only that listed prompt for the changed skill after local generation, following repository policy.

## Source and Derivation

- Source artifact: GitHub issue [#875](https://github.com/jabrena/plinth/issues/875).
- Derivation direction: GitHub issue #875 -> sanitized planning summary -> this OpenSpec design -> XML generator implementation and validation.

## Open Questions

None for this planning change. The implementation should make conservative choices that follow the existing `111-java-maven-dependencies` XML source pattern.
