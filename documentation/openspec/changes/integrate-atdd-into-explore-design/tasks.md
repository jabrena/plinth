## 1. Command Acceptance Contract

- [ ] 1.1 Update `plinth-commands-generator/src/test/resources/gherkin/commands/explore-design.feature` so OpenSpec refinement invokes `059-design-atdd` as the final alignment gate and covers both `ready` and `changes-requested` outcomes.
- [ ] 1.2 Preserve the existing issue-only, design-skill routing, create-spec-first, no-implementation, and no-source-artifact-mutation assertions in `explore-design.feature`.
- [ ] 1.3 Update `plinth-commands-generator/src/test/java/info/jab/pml/CommandIndexesTest.java` to assert that the generated `/explore-design` command lists `059-design-atdd`, reports its alignment outcome, and does not approve unresolved findings.

## 2. Canonical Command Source

- [ ] 2.1 Update `plinth-commands-generator/src/main/resources/commands/explore-design.xml` to list `059-design-atdd` for reviewable OpenSpec inputs without changing existing input syntax or design-skill routing.
- [ ] 2.2 Add the post-refinement ATDD gate to the command workflow, including `ready` progression, `changes-requested` handling, maintainer-approved revision, and rerun behavior.
- [ ] 2.3 Update the command output and safeguards so alignment evidence is reported, unresolved findings cannot be called approved, and OpenSpec criteria or tasks are never silently rewritten.
- [ ] 2.4 Validate the edited XML with `xmllint --noout plinth-commands-generator/src/main/resources/commands/explore-design.xml`.

## 3. Focused Generation and Verification

- [ ] 3.1 Run `./mvnw clean verify -pl plinth-commands-generator` and inspect the generated `/explore-design` Markdown for the expected ATDD routing and approval boundary.
- [ ] 3.2 Execute the existing prompt `execute @plinth-commands-generator/src/test/resources/gherkin/commands/explore-design.feature` from `acceptance-tests-prompts-commands.md` and resolve any failed acceptance behavior before promotion.
- [ ] 3.3 Confirm `059-design-atdd` generator sources and generated skill output remain unchanged and that `.claude/commands/`, `.cursor/commands/`, `.agents/skills/`, public `skills/`, and `docs/` were not edited directly.
- [ ] 3.4 Run `openspec validate --all` from `documentation/` and report source traceability, validation evidence, and remaining risks before implementation handoff.
