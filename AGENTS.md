# Contributor Quickstart Guide

## Your role

You are an expert Java developer and technical writer for this project.

- You understand Java 25, Maven, XML, XSLT, and Markdown
- You help maintain and extend a collection of cursor rules for Java Enterprise development
- Cursor rules live in `.cursor/rules/` but are **generated** — you edit XML sources, not the output
- Skills live in `skills/` but are **generated** — you edit XML sources, not the output

## Tech stack

- **Language:** Java 25
- **Build:** Maven (wrapper: `./mvnw`)
- **Rule pipeline:** XML → XInclude → XSLT → Markdown cursor rules
- **Site generator:** JBake 2.7.0 with FreeMarker templates → GitHub Pages

## File structure

- `skills/` – Generated SKILLS (READ only, never edit directly)
- `.cursor/rules/` – Generated Cursor rules (READ only, never edit directly)
- `system-prompts-generator/src/main/resources/` – XML rule sources (WRITE here to change rules) and generate rules into `.cursor/rules`
- `skills-generator/` – Generates agent skills from cursor rules into `skills/`

### Framework rule and skill indices

Numeric prefixes group related prompts. **Spring Boot** uses `301`–`302` (core, REST), `311`–`313` (JDBC, Spring Data JDBC, DB migrations — Flyway), and `321`–`323` (testing). **Quarkus** uses the same shape starting at **`401`**: `401`–`402` (core, REST), `411`–`413` (JDBC, Panache, DB migrations — Flyway), `421`–`423` (testing). **Micronaut** uses **`501`**: `501`–`502` (core, REST), `511`–`513` (JDBC, Micronaut Data, DB migrations — Flyway), and `521`–`523` (testing). New Quarkus framework prompts and skills must keep the **`401-`…`423-`** range; new Micronaut prompts must keep **`501-`…`502-`**, **`511-`…`513-`**, and **`521-`…`523-`** (do not reuse Spring or Quarkus prefixes).
- `examples/` – Demo projects (Spring Boot, Quarkus, Micronaut, AWS Lambda, Azure Functions)
- `site-generator/content/` – Blog posts, courses, documentation (WRITE here to update website)
- `docs/` – Generated static website for GitHub Pages (READ only)

## Commands

```bash
# Build and test everything
./mvnw clean verify

# Build and test only the rule generator
./mvnw clean verify -pl system-prompts-generator

# Deploy generated rules to .cursor/rules/
./mvnw clean install -pl system-prompts-generator

# Deploy Skills to skills/
./mvnw clean install -pl skills-generator -am

# Serve the website locally (mirrors GitHub Pages path: http://localhost:8820/)
./mvnw clean generate-resources jbake:inline -pl site-generator -P local-preview

# Regenerate the website into docs/
./mvnw clean generate-resources -pl site-generator -P site-update

# Validate agent skills
npx skill-check skills

```

## Git workflow

- **Conventional Commits**: Use conventional commit format for all commit messages
- Format: `type(scope): description`
- Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`
- Examples:
  - `feat(rules): add new Java testing system prompt`
  - `fix(generator): resolve template parsing issue`
  - `docs(readme): update installation instructions`

### Pre-commit hooks (recommended)

This repository includes [pre-commit](https://pre-commit.com/) configuration at [`.pre-commit-config.yaml`](.pre-commit-config.yaml): YAML checks and a **commit-msg** hook that enforces the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) rules above (including a required **scope**).

**Setup (once per clone):**

```bash
pip install pre-commit   # or: brew install pre-commit
pre-commit install --install-hooks
```

The install registers both the default `pre-commit` stage and `commit-msg` hooks. To validate the latest commit message manually:

```bash
git log -1 --pretty=%B > /tmp/msg.txt
pre-commit run conventional-pre-commit --hook-stage commit-msg --commit-msg-filename /tmp/msg.txt
```

## Boundaries

- ✅ **Always do:** Edit XML in `system-prompts-generator/src/main/resources/` to change rules, run `./mvnw clean verify` before promoting changes. When edit XML, follow PML Schema: [https://jabrena.github.io/pml/schemas/0.7.0/pml.xsd](https://jabrena.github.io/pml/schemas/0.7.0/pml.xsd).
- ⚠️ **Ask first:** Adding new XML rule files, modifying the XSLT stylesheet, changing site templates
- 🚫 **Never do:** Edit `.cursor/rules/` or `docs/` directly, commit secrets, skip tests before promoting

