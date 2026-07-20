## 1. Define the generated BDD skill

- [ ] 1.1 Add `plinth-skills-generator/src/main/resources/skill-references/058-design-bdd.xml` as an example-driven Gherkin syntax reference using PML `<examples>` rather than procedural `<steps>`; cover document structure, step semantics, Background, Scenario Outline and Examples, Data Tables, Doc Strings, tags, comments, and localization derived from `https://cucumber.io/docs/gherkin/reference/`, retaining the URL as upstream provenance without runtime access.
- [ ] 1.2 Add `plinth-skills-generator/src/main/resources/skill-indexes/058-skill.xml` with interactive metadata, BDD trigger phrases, constraints, workflow summary, output format, and the local reference dependency.
- [ ] 1.3 Register id `058`, explicit skill id `058-design-bdd`, and reference `058-design-bdd` in `plinth-skills-generator/src/main/resources/skills.xml`.
- [ ] 1.4 Validate every edited XML source with `xmllint --noout`.

## 2. Add acceptance coverage and documentation

- [ ] 2.1 Add `plinth-skills-generator/src/test/resources/gherkin/skills/058-design-bdd.feature` covering sanitized behavior facts, concrete examples, shared-language Given/When/Then formulation, bundled Gherkin guidance with no external runtime access, an independent self-contained outcome, and explicit unresolved items.
- [ ] 2.2 Add the matching `execute @plinth-skills-generator/src/test/resources/gherkin/skills/058-design-bdd.feature` entry to `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`, grouped under skill id `058-design-bdd`.
- [ ] 2.3 Add `058-design-bdd` to the Design skills section of `documentation/guides/INVENTORY-SKILLS-JAVA.md`, including its interactive prompt and independent interaction boundary.

## 3. Generate and verify

- [ ] 3.1 Run `./mvnw clean install -pl plinth-skills-generator -am` and verify `.agents/skills/058-design-bdd/SKILL.md` plus its detailed reference are generated without unresolved include markers or broken local paths.
- [ ] 3.2 Execute only the new `058-design-bdd` acceptance prompt listed in `plinth-skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`; record any skipped prompt with its reason and correct generator XML before promotion if it fails.
- [ ] 3.3 Run `jbang markdown-validator/src/main/java/info/jab/mv/MarkdownValidator.java .` and inspect changed local links in the Java skill inventory.
- [ ] 3.4 Run `openspec validate --all` from `documentation/` and resolve validation failures before implementation is promoted.
- [ ] 3.5 Review `git diff` to confirm `.cursor/rules/`, `.agents/skills/`, public `skills/`, and `docs/` were not directly edited and that issue #1047 remains a one-way source.
