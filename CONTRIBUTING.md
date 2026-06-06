# Contributing

## Conventions

Read [AGENTS.md](./AGENTS.md) for the full contributor guide (tech stack, boundaries, and commands).

- **Git:** use [Conventional Commits](https://www.conventionalcommits.org/) in the form `type(scope): description`, with types such as `feat`, `fix`, `docs`, `style`, `refactor`, `test`, and `chore` (see AGENTS.md).
- **Pre-commit (recommended):** this repo includes [pre-commit](https://pre-commit.com/) in [`.pre-commit-config.yaml`](.pre-commit-config.yaml) (YAML checks and a commit-msg hook aligned with the rules above). Install once per clone: `pip install pre-commit` or `brew install pre-commit`, then `pre-commit install --install-hooks`. Details and manual checks are in AGENTS.md under **Pre-commit hooks**.

## Generator

The unified `skills-generator` module holds all XML sources and Java code used to build **agent skills** under `skills/`.

- [System prompt XML files](./skills-generator/src/main/resources/skill-references/) use the PML Schema ([pml.xsd](https://jabrena.github.io/pml/schemas/0.5.0/pml.xsd)). They are transformed with [SkillReferenceGenerator.java](./skills-generator/src/main/java/info/jab/pml/SkillReferenceGenerator.java) and [skill-reference-to-markdown.xsl](./skills-generator/src/main/resources/skill-reference-to-markdown.xsl) when producing reference content for skills.
- [Skill summaries and inventory](./skills-generator/src/main/resources/) (`skill-indexes/`, `skills.xml`) drive `SKILL.md` generation.

If you have the idea to contribute, review the whole process in detail:

```bash
./mvnw clean verify -pl skills-generator   # Build and test the generator
./mvnw clean install -pl skills-generator  # Generate local skills in .agents/skills/
```

Maintainers who change skill XML sources should use the normal install command
to regenerate `.agents/skills/` for local agent testing. Do not edit or refresh
the public `skills/` directory during normal development.

When preparing an intentional release, refresh and validate the public output:

```bash
./mvnw clean install -pl skills-generator -P release
npx skill-check@latest skills --no-security-scan --format github
skill-scanner scan-all ./skills --recursive --use-behavioral --policy strict --fail-on-severity high
```

See [DEVELOPER.md](./DEVELOPER.md) and
[ADR-006](./documentation/adr/ADR-006-separate-local-skill-generation-from-release-publishing.md)
for the full local generation and release publishing workflow.

Keep `skills.xml` aligned with `skill-indexes/` and `skill-references/` when adding or changing skills.

When you feel confident with the process, fork the repository and try to create new XML documents. Models will help you because an XML file is more rigid than natural language and it has `a common vocabulary` to create prompts.

When you feel confident with the solution, send a PR.
