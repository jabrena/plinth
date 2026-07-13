# Tasks

## 1. Implementation Checklist

- [x] 1.1 Read GitHub issue #875 and derive a sanitized planning summary.
- [x] 1.2 Assess change boundaries and keep the request as one OpenSpec change for `111-java-maven-dependencies`.
- [x] 1.3 Create OpenSpec proposal, design, task, and specification delta artifacts with source URL and derivation direction.
- [x] 1.4 Inspect `plinth-skills-generator/src/main/resources/skill-indexes/111-skill.xml`, `plinth-skills-generator/src/main/resources/skills.xml`, and related `111-java-maven-dependencies-*` references to choose the smallest source placement for JavaMoney guidance.
- [x] 1.5 Confirm the preparatory source placement preserves existing Maven dependency-family selection behavior.
- [x] 1.6 Add JavaMoney guidance in XML source content, including `https://javamoney.github.io/` and when Money and Currency API support is relevant.
- [x] 1.7 Update `plinth-skills-generator/src/test/resources/gherkin/skills/111-java-maven-dependencies.feature` only if acceptance coverage needs to assert the new JavaMoney guidance.
- [x] 1.8 Update `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` only if a new or changed `111` acceptance prompt entry is required.
- [x] 1.9 Validate each edited XML file with `xmllint --noout`.
- [x] 1.10 Run `./mvnw clean install -pl plinth-skills-generator`.
- [x] 1.11 Inspect generated local `.agents/skills/111-java-maven-dependencies/SKILL.md` for JavaMoney guidance and absence of unresolved include markers.
- [ ] 1.12 Execute the listed `111-java-maven-dependencies` acceptance prompt if regenerated local output for that skill changes.
- [x] 1.13 Run `openspec validate --all` from `documentation/`.

## Skipped Prompt

- `execute @plinth-skills-generator/src/test/resources/gherkin/skills/111-java-maven-dependencies.feature`: skipped because this environment exposes the prompt inventory as documentation but no local acceptance-prompt runner or `execute @...feature` command.

## Source and Derivation

- Source artifact: GitHub issue [#875](https://github.com/jabrena/plinth/issues/875).
- Derivation direction: GitHub issue #875 -> sanitized planning summary -> OpenSpec task checklist -> XML skill source implementation and validation.
