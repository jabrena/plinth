# Tasks

## 1. Implementation Checklist

- [ ] 1.1 Confirm the affected skill list from issue #890 against the current XML sources or `SkillTriggerInventoryTest` output.
- [ ] 1.2 Identify the skill index XML files under `skills-generator/src/main/resources/skill-indexes/` for every skill with fewer than five triggers.
- [ ] 1.3 Add or update focused inventory test coverage so any generated skill with fewer than five triggers is reported as a validation failure.
- [ ] 1.4 For each affected skill, add enough domain-specific trigger phrases so the source `<triggers>` section contains at least five meaningful `<trigger>` entries.
- [ ] 1.5 Remove or avoid duplicate/filler trigger wording while preserving the skill's actual scope and routing boundaries.
- [ ] 1.6 Validate every changed XML file with `xmllint --noout`.
- [ ] 1.7 Run the focused trigger inventory test with `./mvnw -pl skills-generator -Dtest=SkillTriggerInventoryTest test`.
- [ ] 1.8 Run `./mvnw clean install -pl skills-generator` to regenerate local `.agents/skills` output without refreshing public `skills/`.
- [ ] 1.9 Inspect representative generated `SKILL.md` files across command, planning, Java core, framework, testing, observability, and technology skill families.
- [ ] 1.10 Check `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`; execute only listed acceptance prompts for changed skills whose generated local `SKILL.md` changes, and record any skipped prompts with reasons.
- [ ] 1.11 Run `./mvnw clean verify -pl skills-generator`.
- [ ] 1.12 Run `openspec validate --all` from `documentation/`.
