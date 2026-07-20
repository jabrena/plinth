## 1. Refine and define the generated ATDD skill

- [ ] 1.1 Refine the review presentation and interaction details while preserving the required execution-goal, acceptance-criteria, and task alignment behavior and without modifying `/explore-design`.
- [ ] 1.2 Add `plinth-skills-generator/src/main/resources/skill-indexes/059-skill.xml` with metadata, trigger phrases, constraints, workflow summary, output format, and safeguards for detecting complete, partial, missing, ambiguous, absent, and divergent coverage.
- [ ] 1.3 Add any approved bundled reference under `plinth-skills-generator/src/main/resources/skill-references/` and register it only when the design establishes its need and content authority.
- [ ] 1.4 Register id `059`, explicit skill id `059-design-atdd`, and every approved reference in `plinth-skills-generator/src/main/resources/skills.xml`.
- [ ] 1.5 Validate every edited XML source with `xmllint --noout`.

## 2. Add acceptance coverage and documentation

- [ ] 2.1 Add `plinth-skills-generator/src/test/resources/gherkin/skills/059-design-atdd.feature` covering goal-to-criteria-to-task traceability, complete and partial coverage, criteria without tasks, ambiguous or absent criteria, divergent tasks, evidence-backed findings, and the no-silent-rewrite boundary.
- [ ] 2.2 Add the matching `execute @plinth-skills-generator/src/test/resources/gherkin/skills/059-design-atdd.feature` entry to `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`, grouped under skill id `059-design-atdd`.
- [ ] 2.3 Add `059-design-atdd` to the Design skills section of `documentation/guides/INVENTORY-SKILLS-JAVA.md`, including its prompt, alignment-review purpose, later `/explore-design` use, and no-silent-rewrite boundary.

## 3. Generate and verify

- [ ] 3.1 Run `./mvnw clean install -pl plinth-skills-generator -am` and verify `.agents/skills/059-design-atdd/SKILL.md` plus any generated reference contain no unresolved include markers or broken local paths.
- [ ] 3.2 Execute only the new `059-design-atdd` acceptance prompt listed in `acceptance-tests-prompts-skills.md`; record any skipped prompt with its reason and correct generator XML before promotion if it fails.
- [ ] 3.3 Run `jbang markdown-validator/src/main/java/info/jab/mv/MarkdownValidator.java .` and inspect changed local links in the Java skill inventory.
- [ ] 3.4 Run `openspec validate --all` from `documentation/` and resolve validation failures before implementation is promoted.
- [ ] 3.5 Review `git diff` to confirm `.cursor/rules/`, `.agents/skills/`, public `skills/`, and `docs/` were not directly edited and that issue #1048 remains a one-way source.
