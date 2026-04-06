# Contributing

## Conventions

Read [AGENTS.md](./AGENTS.md) for the full contributor guide (tech stack, boundaries, and commands).

- **Git:** use [Conventional Commits](https://www.conventionalcommits.org/) in the form `type(scope): description`, with types such as `feat`, `fix`, `docs`, `style`, `refactor`, `test`, and `chore` (see AGENTS.md).
- **Pre-commit (recommended):** this repo includes [pre-commit](https://pre-commit.com/) in [`.pre-commit-config.yaml`](.pre-commit-config.yaml) (YAML checks and a commit-msg hook aligned with the rules above). Install once per clone: `pip install pre-commit` or `brew install pre-commit`, then `pre-commit install --install-hooks`. Details and manual checks are in AGENTS.md under **Pre-commit hooks**.

## Generator

The unified `skills-generator` module holds all XML sources and Java code used to build **agent skills** under `skills/`.

- [System prompt XML files](./skills-generator/src/main/resources/system-prompts/) use the PML Schema ([pml.xsd](https://jabrena.github.io/pml/schemas/0.5.0/pml.xsd)). They are transformed with [CursorRulesGenerator.java](./skills-generator/src/main/java/info/jab/pml/CursorRulesGenerator.java) and [system-prompts.xsl](./skills-generator/src/main/resources/system-prompts.xsl) when producing reference content for skills.
- [Skill summaries and inventory](./skills-generator/src/main/resources/) (`skills/`, `skill-inventory.json`) drive `SKILL.md` generation.

If you have the idea to contribute, review the whole process in detail:

```bash
./mvnw clean verify -pl skills-generator   # Pass tests
./mvnw clean install -pl skills-generator  # Generate skills into skills/
npx skill-check skills                     # Validate generated skills
```

Keep `skill-inventory.json` aligned with `skills/` and `system-prompts/` when adding or changing skills.

When you feel confident with the process, fork the repository and try to create new XML documents. Models will help you because an XML file is more rigid than natural language and it has `a common vocabulary` to create prompts.

When you feel confident with the solution, send a PR.
