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

## Change workflow

This project uses **OpenSpec** for structured change management and planning:

**GitHub Issues → OpenSpec → Agents + Skills**

1. **GitHub Issues** - Track problems, features, and architectural decisions
2. **OpenSpec** (`documentation/openspec/`) - Plan and coordinate implementation through:
   - **Proposals** - Problem statements and solution approaches
   - **Specs** - Requirements with Given/When/Then scenarios
   - **Tasks** - Detailed implementation checklists
3. **Agents + Skills** - Generate the actual cursor rules and agent skills

**Key principle**: Complex changes (especially architectural ones) should be planned in OpenSpec before implementation to ensure thorough analysis and stakeholder alignment.

## File structure

- `skills/` – Generated SKILLS (READ only, never edit directly)
- `.cursor/rules/` – Generated Cursor rules (READ only, never edit directly)
- `system-prompts-generator/src/main/resources/` – XML rule sources (WRITE here to change rules) and generate rules into `.cursor/rules`
- `skills-generator/` – Generates agent skills from cursor rules into `skills/` (WRITE)
- `documentation/openspec/` – OpenSpec change management (proposals, specs, tasks) (WRITE)
- `documentation/adr/` – Architecture Decision Records (WRITE)
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

# OpenSpec change management (run from documentation/ directory)
cd documentation/
openspec list                        # List all changes and their progress
openspec show <change-name>          # Show details of a specific change
openspec validate --all              # Validate all changes meet requirements
openspec new change <change-name>    # Create a new change
openspec archive <change-name>       # Archive a completed change

```

## Git workflow

- **Conventional Commits**: Use conventional commit format for all commit messages
- Format: `type(scope): description`

| Type | Typical use |
|------|-------------|
| **feat** | New behavior / user-facing capability |
| **fix** | Bug fix |
| **docs** | Documentation only |
| **style** | Formatting, whitespace, etc. (no logic change) |
| **refactor** | Internal change, same external behavior |
| **perf** | Performance improvement |
| **test** | Tests only |
| **build** | Build system or dependencies (e.g. Maven, Gradle) |
| **ci** | CI config (workflows, pipelines) |
| **chore** | Maintenance, tooling, meta (when nothing else fits) |
| **revert** | Reverts a previous commit |

The [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) spec allows other types if your team agrees. **This repository’s commit-msg hook** accepts the types in the table above and requires a **scope** (see [`.pre-commit-config.yaml`](.pre-commit-config.yaml)).

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

- ✅ **Always do:** Edit XML in `system-prompts-generator/src/main/resources/` to change rules, run `./mvnw clean verify` before promoting changes. When edit XML, follow PML Schema: [https://jabrena.github.io/pml/schemas/0.7.0/pml.xsd](https://jabrena.github.io/pml/schemas/0.7.0/pml.xsd). For complex changes, create OpenSpec proposals first.
- ⚠️ **Ask first:** Adding new XML rule files, modifying the XSLT stylesheet, changing site templates, architectural changes (use OpenSpec for planning)
- 🚫 **Never do:** Edit `.cursor/rules/` or `docs/` directly, commit secrets, skip tests before promoting, bypass OpenSpec for major changes
