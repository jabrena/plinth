## 1. Agent Assets

- [x] 1.1 Add `robot-no-java.md` under canonical agent assets.
- [x] 1.2 Update `robot-tech-lead.md` routing so non-Java issue/plan/spec work delegates to `@robot-no-java`.
- [x] 1.3 Update `implement-issue.md` delegation references to include `@robot-no-java`.

## 2. XML and Inventory

- [x] 2.1 Add `robot-no-java.md` to `005-agents-installation.xml`.
- [x] 2.2 Update embedded agent inventory and skills-list templates to describe the eight-agent bundle.
- [x] 2.3 Update focused generator tests for installer, inventory, and tech-lead routing.

## 3. Validation

- [x] 3.1 Validate edited XML with `xmllint --noout`.
- [x] 3.2 Run focused `plinth-skills-generator` tests or `./mvnw clean verify -pl plinth-skills-generator`.
- [x] 3.3 Run Markdown validation and inspect changed local links.
- [x] 3.4 Run `openspec validate --all`.
- [x] 3.5 Archive `add-non-java-default-agent` after implementation is complete and validation passes.
