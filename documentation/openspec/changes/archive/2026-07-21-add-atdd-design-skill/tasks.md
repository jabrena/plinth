## 1. Refine and define the generated ATDD skill

- [x] 1.1 Refine the review presentation and interaction details while preserving the required execution-goal, acceptance-criteria, and task alignment behavior.
- [x] 1.2 Add `plinth-skills-generator/src/main/resources/skill-indexes/059-skill.xml` with metadata, trigger phrases, constraints, workflow summary, output format, and safeguards for detecting complete, partial, missing, ambiguous, absent, and divergent coverage.
- [x] 1.3 Add `plinth-skills-generator/src/main/resources/skill-references/059-design-atdd.xml` with the complete status definitions and example-driven evidence-backed traceability guidance, using the OpenSpec requirements and calculator fixture as content authority without duplicating the skill-index workflow.
- [x] 1.4 Register id `059`, explicit skill id `059-design-atdd`, and reference `059-design-atdd` in `plinth-skills-generator/src/main/resources/skills.xml` using the default reference-generating behavior.
- [x] 1.5 Validate every edited XML source with `xmllint --noout`.

## 2. Add acceptance coverage and documentation

- [x] 2.1 Add `plinth-skills-generator/src/test/resources/gherkin/skills/059-design-atdd.feature` covering an aligned calculator change plus a structurally valid negative OpenSpec change with complete, partial, missing, ambiguous, absent, and divergent findings, the `changes-requested` outcome, explanations of every pending finding, focused maintainer revision, evidence-backed findings, and the no-silent-rewrite boundary.
- [x] 2.2 Add the matching `execute @plinth-skills-generator/src/test/resources/gherkin/skills/059-design-atdd.feature` entry to `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`, grouped under skill id `059-design-atdd`.
- [x] 2.3 Add `059-design-atdd` to the Design skills section of `documentation/guides/INVENTORY-SKILLS-JAVA.md`, including its prompt, alignment-review purpose, result contract, and no-silent-rewrite boundary.

## 3. Generate and verify

- [x] 3.1 Run `./mvnw clean install -pl plinth-skills-generator -am` and verify `.agents/skills/059-design-atdd/SKILL.md` plus `.agents/skills/059-design-atdd/references/059-design-atdd.md` contain no unresolved include markers or broken local paths.
- [x] 3.2 Execute only the new `059-design-atdd` acceptance prompt listed in `acceptance-tests-prompts-skills.md`; record any skipped prompt with its reason and correct generator XML before promotion if it fails.
  Executed `execute @plinth-skills-generator/src/test/resources/gherkin/skills/059-design-atdd.feature` against the generated `.agents/skills/059-design-atdd/SKILL.md` and bundled `references/059-design-atdd.md`: the aligned calculator change produced `ready`; the structurally valid negative change produced evidence-backed complete, partial, missing, ambiguous, absent, and divergent findings, followed by `changes-requested`, explanations of every pending finding, a focused request for maintainer revision, and no artifact mutation.
- [x] 3.3 Run `jbang markdown-validator/src/main/java/info/jab/mv/MarkdownValidator.java .` and inspect changed local links in the Java skill inventory.
- [x] 3.4 Run `openspec validate --all` from `documentation/` and resolve validation failures before implementation is promoted.
- [x] 3.5 Review `git diff` to confirm `.cursor/rules/`, `.agents/skills/`, public `skills/`, and `docs/` were not directly edited and that issue #1048 remains a one-way source.
