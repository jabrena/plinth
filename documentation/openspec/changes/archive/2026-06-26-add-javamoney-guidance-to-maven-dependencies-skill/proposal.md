## Why

GitHub issue [#875](https://github.com/jabrena/plinth/issues/875) requests adding `javamoney.github.io` dependency guidance to `111-java-maven-dependencies`.

The Maven dependencies skill currently guides users through selected dependency families such as JSpecify, Error Prone with NullAway, VAVR, and ArchUnit. Maintainers need JavaMoney guidance available from the skill so agents can recognize when a Maven project needs Money and Currency API support and can direct users to the JavaMoney resource without editing generated output directly.

## Sanitized Planning Summary

- Add JavaMoney dependency guidance or reference material to `111-java-maven-dependencies`.
- Use `https://javamoney.github.io/` as the reference website.
- JavaMoney covers the Java Money and Currency API, JSR 354 API, Moneta reference implementation, and related libraries.
- The guidance should explain when JavaMoney is relevant for Money and Currency API support.
- Implementation must be authored from XML sources under `skills-generator/src/main/resources/`.
- Generated release output under `skills/` and legacy checked-in Cursor rules under `.cursor/rules/` must not be edited directly.
- Local generated output for `111-java-maven-dependencies` must include the JavaMoney guidance after regeneration.
- Validation must include XML well-formedness checks and `./mvnw clean install -pl skills-generator`.

## What Changes

- Add JavaMoney guidance to the XML source structure for `111-java-maven-dependencies`.
- Ensure the generated local `111-java-maven-dependencies` skill output includes the JavaMoney reference and explains when it applies.
- Add or update focused acceptance coverage for the JavaMoney guidance if the implementation changes the skill behavior described by `111-java-maven-dependencies`.
- Keep generated public `skills/` output out of scope unless a later release profile intentionally refreshes it.

## Capabilities

### New Capabilities

- `java-maven-dependencies-javamoney-guidance`: Adds JavaMoney guidance to the Maven dependencies skill and preserves source/generated-output boundaries.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#875](https://github.com/jabrena/plinth/issues/875).
- Source authority: issue #875 provides the user story, acceptance criteria, implementation notes, and INVEST validation for this OpenSpec change.
- Derivation direction: GitHub issue #875 -> sanitized planning summary -> OpenSpec change artifacts -> XML skill source implementation -> local generated skill validation.

## Change Boundary Assessment

This is one OpenSpec change because the issue requests one focused outcome: JavaMoney guidance for `111-java-maven-dependencies`. The implementation may touch the skill index, question/reference mapping, focused reference content, Gherkin acceptance coverage, prompt inventory, and local generated output checks, but those files all support the same reviewable capability.

## Impact

XML skill source files for `111-java-maven-dependencies`, possible skill Gherkin tests, acceptance prompt inventory, local generated skill output, and OpenSpec artifacts are affected. Generated `.cursor/rules/`, public `skills/`, `.agents/skills`, and `docs/` must not be edited directly. Public `skills/` should only change through the documented release profile when release output is intentionally in scope.
