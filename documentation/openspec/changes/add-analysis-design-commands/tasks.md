## 1. Command Tests

- [x] 1.1 Add command-focused assertions under `skills-generator/src/test/java/info/jab/pml/` for the eight new command filenames.
- [x] 1.2 Add assertions for `create-worktree.md`, the extended `create-feature-branch.md` contract, and exact installer/inventory completeness.

## 2. Command Assets

- [x] 2.1 Add `create-issue.md`, `create-worktree.md`, `explore-design.md`, `create-adr.md`, `create-diagram.md`, `create-plan.md`, `create-spec.md`, and `review-alignment.md` under `skills-generator/src/main/resources/skill-references/assets/commands/`.
- [x] 2.2 Extend `create-feature-branch.md` for issue/change inputs, safe-worktree validation, pre-implementation artifact commits, and no automatic commit.
- [x] 2.3 Define non-destructive `/create-worktree` checks, optional target/base inputs, exact result reporting, and conflict handling.
- [x] 2.4 Keep command prompts concise and delegate substantive behavior to named agents and skills.

## 3. Installer and Inventory

- [x] 3.1 Update `skills-generator/src/main/resources/skill-references/004-commands-installation.xml` with the complete command bundle.
- [x] 3.2 Update `skills-generator/src/main/resources/skill-references/assets/java-commands-inventory-template.md`.
- [x] 3.3 Validate `004-commands-installation.xml` with `xmllint`.

## 4. Change Verification

- [x] 4.1 Run command-focused generator tests.
- [x] 4.2 Run `./mvnw clean verify -pl skills-generator`.
- [x] 4.3 Run local skill generation without the `release` profile and inspect command-related output.
