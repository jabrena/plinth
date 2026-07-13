# Tasks

## 1. Implementation Checklist

- [x] 1.1 Confirm the affected skill list from issue #890 against the current XML sources or `SkillTriggerInventoryTest` output.
- [x] 1.2 Identify the skill index XML files under `plinth-skills-generator/src/main/resources/skill-indexes/` for every skill with fewer than five triggers.
- [x] 1.3 Add or update focused inventory test coverage so any generated skill with fewer than five triggers is reported as a validation failure.
- [x] 1.4 For each affected skill, add enough domain-specific trigger phrases so the source `<triggers>` section contains at least five meaningful `<trigger>` entries.
- [x] 1.5 Remove or avoid duplicate/filler trigger wording while preserving the skill's actual scope and routing boundaries.
- [x] 1.6 Validate every changed XML file with `xmllint --noout`.
- [x] 1.7 Run the focused trigger inventory test with `./mvnw -pl plinth-skills-generator -Dtest=SkillTriggerInventoryTest test`.
- [x] 1.8 Run `./mvnw clean install -pl plinth-skills-generator` to regenerate local `.agents/skills` output without refreshing public `skills/`.
- [x] 1.9 Inspect representative generated `SKILL.md` files across command, planning, Java core, framework, testing, observability, and technology skill families.
- [x] 1.10 Check `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`; execute only listed acceptance prompts for changed skills whose generated local `SKILL.md` changes, and record any skipped prompts with reasons.
  Skipped listed interactive acceptance prompts because this environment exposes the prompt inventory as documentation but no local `execute @...feature` runner or command. Representative generated local `SKILL.md` files were inspected after regeneration.
- [x] 1.11 Run `./mvnw clean verify -pl plinth-skills-generator`.
- [x] 1.12 Run `openspec validate --all` from `documentation/`.
